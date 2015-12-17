package com.squirrelplugin.folding;

import com.intellij.codeInsight.folding.CodeFoldingSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.CustomFoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UnfairTextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.squirrelplugin.psi.*;

import static com.squirrelplugin.SquirrelTokenTypes.*;
import static com.squirrelplugin.SquirrelTokenTypesSets.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SquirrelFoldingBuilder extends CustomFoldingBuilder implements DumbAware {
    protected void buildLanguageFoldRegions(@NotNull final List<FoldingDescriptor> descriptors,
                                            @NotNull final PsiElement root,
                                            @NotNull final Document document,
                                            final boolean quick) {
        final SquirrelFile dartFile = (SquirrelFile) root;
        final TextRange fileHeaderRange = foldFileHeader(descriptors, dartFile, document); // 1. File header

        final Collection<PsiElement> psiElements = PsiTreeUtil.collectElementsOfType(root, new Class[]{PsiComment.class});
        foldComments(descriptors, psiElements, fileHeaderRange);
        foldClassBodies(descriptors, dartFile);
        foldFunctionBodies(descriptors, dartFile);
    }

    @Nullable
    @Override
    protected String getLanguagePlaceholderText(@NotNull final ASTNode node, @NotNull final TextRange range) {
        final IElementType elementType = node.getElementType();
        final PsiElement psiElement = node.getPsi();

        if (psiElement instanceof SquirrelFile) return "/.../";
        if (elementType == MULTI_LINE_DOC_COMMENT) return "/**...*/";
        if (elementType == MULTI_LINE_COMMENT) return "/*...*/";
        if (elementType == SINGLE_LINE_COMMENT) return "//...";
        if (psiElement instanceof SquirrelClassBody) return "{...}";
        if (psiElement instanceof SquirrelFunctionBody) return "{...}";
        return "...";
    }

    protected boolean isRegionCollapsedByDefault(@NotNull final ASTNode node) {
        final IElementType elementType = node.getElementType();
        final PsiElement psiElement = node.getPsi();
        final CodeFoldingSettings settings = CodeFoldingSettings.getInstance();

        if (psiElement instanceof SquirrelFile)
            return settings.COLLAPSE_FILE_HEADER;

        if (elementType == MULTI_LINE_DOC_COMMENT) {
            return settings.COLLAPSE_DOC_COMMENTS;
        }

        if (elementType == MULTI_LINE_COMMENT || elementType == SINGLE_LINE_COMMENT) {
            return settings.COLLAPSE_DOC_COMMENTS;
        }

        if (psiElement instanceof SquirrelFunctionBody) {
            return settings.COLLAPSE_METHODS;
        }

        return false;
    }

    @Nullable
    private static TextRange foldFileHeader(@NotNull final List<FoldingDescriptor> descriptors,
                                            @NotNull final SquirrelFile squirrelFile,
                                            @NotNull final Document document) {
        PsiElement firstComment = squirrelFile.getFirstChild();
        if (firstComment instanceof PsiWhiteSpace) {
            firstComment = firstComment.getNextSibling();
        }

        if (!(firstComment instanceof PsiComment)) return null;

        boolean containsCustomRegionMarker = false;
        PsiElement nextAfterComments = firstComment;
        while (nextAfterComments instanceof PsiComment || nextAfterComments instanceof PsiWhiteSpace) {
            containsCustomRegionMarker |= isCustomRegionElement(nextAfterComments);
            nextAfterComments = nextAfterComments.getNextSibling();
        }

        if (nextAfterComments == null) return null;

        final TextRange fileHeaderCommentsRange = new UnfairTextRange(firstComment.getTextOffset(), nextAfterComments.getTextOffset() - 1);
        if (fileHeaderCommentsRange.getLength() > 1 &&
                document.getLineNumber(fileHeaderCommentsRange.getEndOffset()) >
                        document.getLineNumber(fileHeaderCommentsRange.getStartOffset())) {
            if (!containsCustomRegionMarker) {
                descriptors.add(new FoldingDescriptor(squirrelFile, fileHeaderCommentsRange));
            }
            return fileHeaderCommentsRange;
        }

        return null;
    }

    private static void foldComments(@NotNull final List<FoldingDescriptor> descriptors,
                                     @NotNull final Collection<PsiElement> psiElements,
                                     @Nullable final TextRange fileHeaderRange) {
        PsiElement psiElement;
        for (Iterator<PsiElement> iter = psiElements.iterator(); iter.hasNext(); ) {
            psiElement = iter.next();
            if (!(psiElement instanceof PsiComment)) {
                continue;
            }
            if (fileHeaderRange != null && fileHeaderRange.intersects(psiElement.getTextRange())) {
                continue;
            }

            final IElementType elementType = psiElement.getNode().getElementType();
            if ((elementType == MULTI_LINE_DOC_COMMENT || elementType == MULTI_LINE_COMMENT) && !isCustomRegionElement(psiElement)) {
                descriptors.add(new FoldingDescriptor(psiElement, psiElement.getTextRange()));
            } else if (elementType == SINGLE_LINE_COMMENT) {
                PsiElement lastCommentInSequence = psiElement;
                PsiElement nextElement = psiElement;
                boolean containsCustomRegionMarker = isCustomRegionElement(nextElement);
                while (iter.hasNext() && (nextElement = nextElement.getNextSibling()) != null &&
                        (nextElement instanceof PsiWhiteSpace || nextElement.getNode().getElementType() == elementType)) {
                    if (nextElement.getNode().getElementType() == elementType) {
                        // advance iterator to skip processed comments sequence
                        iter.next();
                        lastCommentInSequence = nextElement;
                        containsCustomRegionMarker |= isCustomRegionElement(nextElement);
                    }
                }

                if (lastCommentInSequence != psiElement && !containsCustomRegionMarker) {
                    final TextRange range =
                            TextRange.create(psiElement.getTextOffset(), lastCommentInSequence.getTextRange().getEndOffset());
                    descriptors.add(new FoldingDescriptor(psiElement, range));
                }
            }
        }
    }

    private static void foldClassBodies(@NotNull final List<FoldingDescriptor> descriptors, @NotNull final SquirrelFile squirrelFile) {
        for (SquirrelClass squirrelClass : PsiTreeUtil.findChildrenOfType(squirrelFile, SquirrelClass.class)) {
            final SquirrelClassBody body = squirrelClass.getClassBody();
            if (body != null && body.getTextLength() > 2) {
                descriptors.add(new FoldingDescriptor(body, body.getTextRange()));
            }
        }
    }

    private static void foldFunctionBodies(@NotNull final List<FoldingDescriptor> descriptors, @NotNull final SquirrelFile squirrelFile) {
        for (SquirrelFunction squirrelFunction : PsiTreeUtil.findChildrenOfType(squirrelFile, SquirrelFunction.class)) {
            final SquirrelFunctionBody body = squirrelFunction.getFunctionBody();
            if (body != null && body.getTextLength() > 2) {
                descriptors.add(new FoldingDescriptor(body, body.getTextRange()));
            }
        }
    }
}
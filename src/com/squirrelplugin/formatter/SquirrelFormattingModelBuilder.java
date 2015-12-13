package com.squirrelplugin.formatter;

import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.squirrelplugin.SquirrelLanguage;
import com.squirrelplugin.psi.SquirrelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static com.squirrelplugin.SquirrelTokenTypes.*;

public class SquirrelFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(@NotNull final PsiElement element, @NotNull final CodeStyleSettings settings) {
        // element can be SquirrelFile, SquirrelEmbeddedContent, SquirrelExpressionCodeFragment
        final PsiFile psiFile = element.getContainingFile();
        final ASTNode rootNode = psiFile instanceof SquirrelFile ? psiFile.getNode() : element.getNode();
        final SquirrelBlock rootBlock = new SquirrelBlock(rootNode, null, null, settings);
        return new DocumentBasedFormattingModel(rootBlock, element.getProject(), settings, psiFile.getFileType(), psiFile);
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
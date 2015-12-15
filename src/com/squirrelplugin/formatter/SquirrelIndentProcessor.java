package com.squirrelplugin.formatter;

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.util.UsefulPsiTreeUtil;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.squirrelplugin.SquirrelTokenTypes.*;
import static com.squirrelplugin.SquirrelTokenTypesSets.*;

public class SquirrelIndentProcessor {

    private final CommonCodeStyleSettings settings;

    public SquirrelIndentProcessor(CommonCodeStyleSettings settings) {
        this.settings = settings;
    }

    public Indent getChildIndent(final ASTNode node) {
        final IElementType elementType = node.getElementType();
        final ASTNode prevSibling = UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(node);
        final IElementType prevSiblingType = prevSibling == null ? null : prevSibling.getElementType();
        final ASTNode parent = node.getTreeParent();
        final IElementType parentType = parent != null ? parent.getElementType() : null;
        final ASTNode superParent = parent == null ? null : parent.getTreeParent();
        final IElementType superParentType = superParent == null ? null : superParent.getElementType();

        final int braceStyle = superParentType == FUNCTION_BODY ? settings.METHOD_BRACE_STYLE : settings.BRACE_STYLE;

        // TODO: tables

        if (COMMENTS.contains(elementType) && prevSiblingType == LBRACE && parentType == CLASS_BODY) {
            return Indent.getNormalIndent();
        }

        if (elementType == SEMICOLON && FormatterUtil.isPrecededBy(node, LINE_COMMENT, WHITE_SPACES)) {
            return Indent.getContinuationIndent();
        }

        if (elementType == LBRACE || elementType == RBRACE) {
            switch (braceStyle) {
                case CommonCodeStyleSettings.END_OF_LINE:
                    if (elementType == LBRACE && FormatterUtil.isPrecededBy(parent, LINE_COMMENT, WHITE_SPACES)) {
                        // Use Nystrom style rather than Allman.
                        return Indent.getContinuationIndent();
                    } // FALL THROUGH
                case CommonCodeStyleSettings.NEXT_LINE:
                case CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED:
                    return Indent.getNoneIndent();
                case CommonCodeStyleSettings.NEXT_LINE_SHIFTED:
                case CommonCodeStyleSettings.NEXT_LINE_SHIFTED2:
                    return Indent.getNormalIndent();
                default:
                    return Indent.getNoneIndent();
            }
        }

        if (parentType == BLOCK) {
            final PsiElement psi = node.getPsi();
            if (psi.getParent() instanceof PsiFile) {
                return Indent.getNoneIndent();
            }
            return Indent.getNormalIndent();
        }

        // --- Expressions

        if (parentType == PARENTHESIZED_EXPRESSION) {
            if (elementType == LPAREN || elementType == RPAREN) {
                return Indent.getNoneIndent();
            }
            return Indent.getContinuationIndent();
        }
        if (BINARY_EXPRESSIONS.contains(parentType) && prevSibling != null) { // TODO: Check if all operators are added to binary set
            return Indent.getContinuationIndent();
        }
        if (parentType == TERNARY_EXPRESSION && (elementType == COLON || elementType == QUESTION || prevSiblingType == COLON || prevSiblingType == QUESTION)) {
            return Indent.getContinuationIndent();
        }
        if (elementType == CALL_EXPRESSION) {
            if (FormatterUtil.isPrecededBy(node, ASSIGNMENT_OPERATOR)) {
                return Indent.getContinuationIndent();
            }
        }
        if ((elementType == REFERENCE_EXPRESSION || BINARY_EXPRESSIONS.contains(elementType)) &&
                (FormatterUtil.isPrecededBy(node, ASSIGNMENT_OPERATOR) || FormatterUtil.isPrecededBy(node, EQ))) {
            return Indent.getContinuationIndent();
        }
        if (elementType == DOT || prevSiblingType == DOT) {
            return Indent.getContinuationIndent();
        }

        // --- Statements

        if (parentType == ENUM_DECLARATION && isBetweenBraces(node)) {
            return Indent.getNormalIndent();
        }

        if (parentType == FUNCTION_BODY && elementType != BLOCK) {
            return Indent.getNormalIndent();
        }

        if (elementType == LPAREN && parentType == ARGUMENTS) {
            return Indent.getContinuationWithoutFirstIndent();
        }
        if (elementType == RPAREN && parentType == ARGUMENTS) {
            if (prevSiblingType == ARGUMENT_LIST) {
                ASTNode[] childs = prevSibling.getChildren(null);
                int n = childs.length;
                if (n > 2 && childs[n - 1] instanceof PsiErrorElement && childs[n-2].getElementType() == COMMA) {
                    return Indent.getContinuationWithoutFirstIndent();
                }
            }
        }
        if (parentType == ARGUMENT_LIST) {
            return Indent.getContinuationWithoutFirstIndent();
        }
        if (parentType == PARAMETER_LIST) {
            return Indent.getContinuationWithoutFirstIndent();
        }

        if (elementType == CLASS_MEMBER) {
            return Indent.getNormalIndent();
        }

        if (parentType == FOR_STATEMENT && elementType != BLOCK && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }
        if (parentType == FOREACH_STATEMENT && elementType != BLOCK && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }
        if (parentType == WHILE_STATEMENT && elementType != BLOCK && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }
        if (parentType == DO_WHILE_STATEMENT && elementType != BLOCK && prevSiblingType == DO) {
            return Indent.getNormalIndent();
        }
        if (parentType == SWITCH_STATEMENT && (elementType == SWITCH_CASE || elementType == DEFAULT_CASE)) {
            return Indent.getNormalIndent();
        }
        if ((parentType == SWITCH_CASE || parentType == DEFAULT_CASE) && elementType == STATEMENT) {
            return Indent.getNormalIndent();
        }
        if (parentType == IF_STATEMENT && elementType != BLOCK &&
                (prevSiblingType == RPAREN || (prevSiblingType == ELSE && elementType != IF_STATEMENT))) {
            return Indent.getNormalIndent();
        }
        if (parentType == TRY_STATEMENT && elementType != BLOCK && prevSiblingType == TRY) {
            return Indent.getNormalIndent();
        }
        if (parentType == CATCH_PART && elementType != BLOCK && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }

        return Indent.getNoneIndent();
    }

    private static boolean isBetweenBraces(@NotNull final ASTNode node) {
        final IElementType elementType = node.getElementType();
        if (elementType == LBRACE || elementType == RBRACE) return false;

        for (ASTNode sibling = node.getTreePrev(); sibling != null; sibling = sibling.getTreePrev()) {
            if (sibling.getElementType() == LBRACE) return true;
        }

        return false;
    }
}
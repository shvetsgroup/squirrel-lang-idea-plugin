package com.sqide.formatter;

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.sqide.formatter.settings.SquirrelCodeStyleSettings;
import com.sqide.util.SquirrelFormatterUtil;
import com.sqide.util.UsefulPsiTreeUtil;

import static com.sqide.SquirrelTokenTypes.*;
import static com.sqide.SquirrelTokenTypesSets.*;

public class SquirrelIndentProcessor {
    public static Indent getChildIndent(final ASTNode node, CommonCodeStyleSettings cmSettings, SquirrelCodeStyleSettings sqSettings) {
        final IElementType elementType = node.getElementType();
        final ASTNode prevSibling = UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(node);
        final IElementType prevSiblingType = prevSibling == null ? null : prevSibling.getElementType();
        final ASTNode parent = node.getTreeParent();
        final IElementType parentType = parent != null ? parent.getElementType() : null;
        final ASTNode superParent = parent == null ? null : parent.getTreeParent();
        final IElementType superParentType = superParent == null ? null : superParent.getElementType();

        // COMMENTS

        if (parent == null || parent.getTreeParent() == null) {
            return Indent.getNoneIndent();
        }

        if (elementType == MULTI_LINE_COMMENT_BODY) {
            return Indent.getContinuationIndent();
        }
        if (elementType == DOC_COMMENT_LEADING_ASTERISK || elementType == MULTI_LINE_COMMENT_END) {
            return Indent.getSpaceIndent(1, true);
        }

        if (cmSettings.KEEP_FIRST_COLUMN_COMMENT && (elementType == SINGLE_LINE_COMMENT || elementType == MULTI_LINE_COMMENT)) {
            final ASTNode previousNode = node.getTreePrev();
            if (previousNode != null && previousNode.getElementType() == WHITE_SPACE && previousNode.getText().endsWith("\n")) {
                return Indent.getAbsoluteNoneIndent();
            }
        }

        if (COMMENTS.contains(elementType) && prevSiblingType == LBRACE && parentType == CLASS_BODY) {
            return Indent.getNormalIndent();
        }

        if (elementType == SEMICOLON && FormatterUtil.isPrecededBy(node, SINGLE_LINE_COMMENT, WHITE_SPACES)) {
            return Indent.getContinuationIndent();
        }

        // BRACES & BLOCKS

        final int braceStyle = superParentType == CLASS_BODY ? cmSettings.CLASS_BRACE_STYLE :
                (superParentType == FUNCTION_BODY ? cmSettings.METHOD_BRACE_STYLE : cmSettings.BRACE_STYLE);

        if (elementType == LBRACE || elementType == RBRACE) {
            switch (braceStyle) {
                case CommonCodeStyleSettings.END_OF_LINE:
                    if (elementType == LBRACE && FormatterUtil.isPrecededBy(parent, SINGLE_LINE_COMMENT, WHITE_SPACES)) {
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
        if (BINARY_EXPRESSIONS.contains(parentType) && prevSibling != null) {
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

        if (parentType == ENUM_DECLARATION && SquirrelFormatterUtil.isBetweenBraces(node)) {
            return Indent.getNormalIndent();
        }
        if (parentType == TABLE_EXPRESSION && SquirrelFormatterUtil.isBetweenBraces(node)) {
            return Indent.getNormalIndent();
        }
        if (parentType == FUNCTION_BODY && SquirrelFormatterUtil.isSimpleStatement(node)) {
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

        if (parentType == FOR_STATEMENT && SquirrelFormatterUtil.isSimpleStatement(node) && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }
        if (parentType == FOREACH_STATEMENT && SquirrelFormatterUtil.isSimpleStatement(node) && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }
        if (parentType == WHILE_STATEMENT && SquirrelFormatterUtil.isSimpleStatement(node) && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }
        if (parentType == DO_WHILE_STATEMENT && SquirrelFormatterUtil.isSimpleStatement(node) && prevSiblingType == DO) {
            return Indent.getNormalIndent();
        }
        if (parentType == SWITCH_STATEMENT && (elementType == SWITCH_CASE || elementType == DEFAULT_CASE)) {
            return Indent.getNormalIndent();
        }
        if (STATEMENTS.contains(elementType) &&  (parentType == SWITCH_CASE || parentType == DEFAULT_CASE)) {
            return Indent.getNormalIndent();
        }
        if (parentType == IF_STATEMENT && SquirrelFormatterUtil.isSimpleStatement(node) && (prevSiblingType == RPAREN || prevSiblingType == ELSE)) {
            return Indent.getNormalIndent();
        }
        if (parentType == TRY_STATEMENT && SquirrelFormatterUtil.isSimpleStatement(node) && prevSiblingType == TRY) {
            return Indent.getNormalIndent();
        }
        if (parentType == CATCH_PART && SquirrelFormatterUtil.isSimpleStatement(node) && prevSiblingType == RPAREN) {
            return Indent.getNormalIndent();
        }

        return Indent.getNoneIndent();
    }
}
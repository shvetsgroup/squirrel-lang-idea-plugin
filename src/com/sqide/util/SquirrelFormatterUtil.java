package com.sqide.util;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.sqide.SquirrelTokenTypes.*;
import static com.sqide.SquirrelTokenTypesSets.*;

public class SquirrelFormatterUtil {
    /**
     * True of element is not block. Returns true even for "if (true) {};"
     */
    public static boolean isSimpleStatement(ASTNode element) {
        if (element.getElementType() == SEMICOLON) {
            ASTNode prev = element.getTreePrev();
            return prev != null && prev.getElementType() != BLOCK;
        }

        return STATEMENTS.contains(element.getElementType()) && element.getElementType() != BLOCK;
    }

    /**
     * True if block, class body, table or enum has only one child.
     */
    public static boolean isSimpleBlock(ASTNode element) {
        if (element.getElementType() == ENUM_DECLARATION) {
            return element.getChildren(TokenSet.create(ENUM_ITEM)).length <= 1;
        }
        else if (element.getElementType() == TABLE_EXPRESSION) {
            return element.getChildren(TokenSet.create(TABLE_ITEM)).length <= 1;
        }
        else if (element.getElementType() == CLASS_BODY) {
            return element.getChildren(TokenSet.create(CLASS_MEMBER)).length <= 1;
        }
        else if (element.getElementType() == BLOCK) {
            return element.getChildren(STATEMENTS).length <= 1;
        }
        return false;
    }

    /**
     * True if element inside block, class body, table or enum.
     */
    public static boolean isBetweenBraces(@NotNull final ASTNode node) {
        final IElementType elementType = node.getElementType();
        if (elementType == LBRACE || elementType == RBRACE) return false;

        for (ASTNode sibling = node.getTreePrev(); sibling != null; sibling = sibling.getTreePrev()) {
            if (sibling.getElementType() == LBRACE) return true;
        }

        return false;
    }

    public static TextRange getTextRangeFromTheBeginningOfElement(ASTNode element, ASTNode child) {
        return new TextRange(element.getStartOffset(), child.getTextRange().getEndOffset());
    }
}
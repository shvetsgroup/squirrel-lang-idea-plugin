package com.squirrelplugin.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.SortedList;
import com.squirrelplugin.formatter.settings.SquirrelCodeStyleSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.squirrelplugin.SquirrelTokenTypes.*;
import static com.squirrelplugin.SquirrelTokenTypesSets.*;

public class SquirrelSpacingProcessor {
    private static final TokenSet TOKENS_WITH_SPACE_AFTER = TokenSet
            .create(STATIC);

    private static final TokenSet KEYWORDS_WITH_SPACE_BEFORE =
            TokenSet.create(EXTENDS);

    private static final TokenSet REFERENCE_EXPRESSION_SET = TokenSet.create(REFERENCE_EXPRESSION);
    private static final TokenSet ID_SET = TokenSet.create(ID);
    private static final TokenSet SIMPLE_LITERAL_SET =
            TokenSet.create(LITERAL_EXPRESSION, INT, FLOAT, STRING, TRUE, FALSE, NULL); // TODO: table expression?
    private static final TokenSet SKIP_COMMA = TokenSet.create(COMMA);

    private final ASTNode myNode;
    private final CommonCodeStyleSettings cmSettings;
    private final SquirrelCodeStyleSettings sqSettings;

    public SquirrelSpacingProcessor(ASTNode node, CommonCodeStyleSettings cmSettings, SquirrelCodeStyleSettings sqSettings) {
        myNode = node;
        this.cmSettings = cmSettings;
        this.sqSettings = sqSettings;
    }

    public Spacing getSpacing(final Block child1, final Block child2) {
        if (!(child1 instanceof AbstractBlock) || !(child2 instanceof AbstractBlock)) {
            return null;
        }

        final IElementType elementType = myNode.getElementType();
        final IElementType parentType = myNode.getTreeParent() == null ? null : myNode.getTreeParent().getElementType();
        final ASTNode node1 = ((AbstractBlock) child1).getNode();
        final ASTNode node2 = ((AbstractBlock) child2).getNode();
        final IElementType type1 = node1.getElementType();
        final IElementType type2 = node2.getElementType();

        Spacing x;

        x = setSpacingBeforeParentheses(type1, type2, elementType);
        if (x != null) return x;

        x = setSpacingAroundOperators(type1, type2, elementType);
        if (x != null) return x;

        x = setSpacingBeforeLeftBrace(type1, type2, elementType, parentType, child1, child2);
        if (x != null) return x;

        x = setSpacingBeforeKeywords(type1, type2, elementType);
        if (x != null) return x;

        x = setSpacingWithin(type1, type2, elementType);
        if (x != null) return x;

        x = setSpacingInTernaryOperator(type1, type2, elementType);
        if (x != null) return x;

        x = setSpacingOther(type1, type2, elementType, parentType);
        if (x != null) return x;

//        x = setSpacingMultiline(type1, type2, elementType, parentType);
//        if (x != null) return x;

        // TODO: New lines?
//        if (type1 == LINE_COMMENT) {
//            int spaces = 0;
//            int lines = 1;
//            if (elementType == SQUIRREL_FILE && !isDirectlyPrecededByNewline(child1)) {
//                lines = 2;
//            }
//            return Spacing.createSpacing(spaces, spaces, lines, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
//        }

//        if (FUNCTION_DEFINITION.contains(type2)) {
//            boolean needsBlank = needsBlankLineBeforeFunction(elementType);
//            if (needsBlank && !cmSettings.KEEP_LINE_BREAKS) {
//                if (parentType == CLASS_BODY || elementType == SQUIRREL_FILE) {
//                    if (type1 == SEMICOLON || hasEmptyBlock(node1)) {
//                        needsBlank = false;
//                    }
//                }
//            }
//            final int lineFeeds = COMMENTS.contains(type1) || !needsBlank ? 1 : 2;
//            return Spacing.createSpacing(0, 0, lineFeeds, needsBlank, cmSettings.KEEP_BLANK_LINES_IN_CODE);
//        }
//        if (BLOCKS.contains(elementType)) {
//            boolean topLevel = elementType == SQUIRREL_FILE;
//            int lineFeeds = 1;
//            int spaces = 0;
//            int blanks = cmSettings.KEEP_BLANK_LINES_IN_CODE;
//            boolean keepBreaks = false;
//            if (!COMMENTS.contains(type1) && (elementType == CLASS_MEMBERS || topLevel && DECLARATIONS.contains(type2))) {
//                if (type1 == SEMICOLON && type2 == VAR_DECLARATION_LIST) {
//                    final ASTNode node1TreePrev = node1.getTreePrev();
//                    if (node1TreePrev == null || node1TreePrev.getElementType() != VAR_DECLARATION_LIST) {
//                        lineFeeds = 2;
//                    }
//                }
//                else {
//                    if (type2 == VAR_DECLARATION_LIST && hasEmptyBlock(node1)) {
//                        lineFeeds = 1;
//                    }
//                    else {
//                        lineFeeds = 2;
//                    }
//                }
//            }
//            else if (type1 == LBRACE && type2 == RBRACE) {
//                if (parentType == FUNCTION_BODY) {
//                    if ((myNode.getTreeParent().getTreeParent() != null) &&
//                            (myNode.getTreeParent().getTreeParent().getElementType() == METHOD_DECLARATION) &&
//                            cmSettings.KEEP_SIMPLE_METHODS_IN_ONE_LINE) {
//                        lineFeeds = 0; // Empty method.
//                        keepBreaks = cmSettings.KEEP_LINE_BREAKS;
//                        blanks = keepBreaks ? cmSettings.KEEP_BLANK_LINES_IN_CODE : 0;
//                    }
//                    else if (cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                        lineFeeds = 0; // Empty function, either top-level or statement.
//                        keepBreaks = cmSettings.KEEP_LINE_BREAKS;
//                        blanks = keepBreaks ? cmSettings.KEEP_BLANK_LINES_IN_CODE : 0;
//                    }
//                }
//                else if (parentType == IF_STATEMENT && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                    lineFeeds = 0;
//                }
//                else if (parentType == FOR_STATEMENT && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                    lineFeeds = 0;
//                }
//                else if (parentType == WHILE_STATEMENT && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                    lineFeeds = 0;
//                }
//                else if (parentType == DO_WHILE_STATEMENT && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                    lineFeeds = 0;
//                }
//                else if (parentType == TRY_STATEMENT && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                    lineFeeds = 0;
//                }
//                else if (parentType == FUNCTION_BODY && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) { // FUNCTION_EXPRESSION_BODY
//                    lineFeeds = 0;
//                }
//                else if (parentType == STATEMENT && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) {
//                    lineFeeds = 0;
//                }
//            }
//            else if (topLevel && COMMENTS.contains(type2)) {
//                lineFeeds = 0;
//                spaces = 1;
//                keepBreaks = true;
//            }
//            else if (type1 != LBRACE && isEmbeddedComment(type2, child2)) {
//                lineFeeds = 0;
//                spaces = 1;
//                keepBreaks = false;
//            }
//            else if ((type1 == LBRACE && type2 == STATEMENT) || (type2 == RBRACE && type1 == STATEMENT)) {
//                lineFeeds = 1;
//                keepBreaks = false;
//                blanks = 0;
//            }
//            else if (type1 == LBRACE && type2 == LINE_COMMENT) {
//                lineFeeds = 1;
//                keepBreaks = false;
//                blanks = 0;
//            }
//            return Spacing.createSpacing(spaces, spaces, lineFeeds, keepBreaks, blanks);
//        }
//
//
//
//        if (elementType == IF_STATEMENT) {
//            if (type1 == RPAREN && cmSettings.BRACE_STYLE == CommonCodeStyleSettings.END_OF_LINE) {
//                // Always have a single space following the closing paren of an if-condition.
//                int nsp = cmSettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0;
//                return Spacing.createSpacing(nsp, nsp, 0, type2 == BLOCK ? false : cmSettings.KEEP_LINE_BREAKS, 0);
//            }
//            if (type1 == SEMICOLON && type2 == ELSE) {
//                // If the then-part is on the line with the condition put the else-part on the next line.
//                return Spacing.createSpacing(0, 0, 1, false, 0);
//            }
//        }
//
//        //
//        //Other
//        //
//
//        if (type1 == ELSE) {
//            if (type2 == IF_STATEMENT) {
//                return Spacing.createSpacing(1, 1, cmSettings.SPECIAL_ELSE_IF_TREATMENT ? 0 : 1, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
//            }
//            if (type2 != LBRACE) {
//                // Keep single-statement else-part on same line.
//                return Spacing.createSpacing(1, 1, 0, type2 == BLOCK ? false : cmSettings.KEEP_LINE_BREAKS, 0);
//            }
//        }
//
//        if (type1 == LBRACE && type2 == RBRACE) {
//            // Empty class.
//            if (elementType == CLASS_BODY && cmSettings.KEEP_SIMPLE_CLASSES_IN_ONE_LINE) return noSpace();
//            // Empty MAP_LITERAL_EXPRESSION or LIST_LITERAL_EXPRESSION.
//            if (cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE) return noSpace();
//        }
//        boolean isBraces = type1 == LBRACE || type2 == RBRACE;
//        if (/*(isBraces && elementType != NAMED_FORMAL_PARAMETERS && elementType != MAP_LITERAL_EXPRESSION) ||*/
//                BLOCKS.contains(type1) ||
//                FUNCTION_DEFINITION.contains(type1)) {
//            return addLineBreak();
//        }
//        if (COMMENTS.contains(type1)) {
//            if (isBraces || parentType == SQUIRREL_FILE || type2 == SEMICOLON) {
//                return addLineBreak();
//            }
//            if (type2 == RBRACKET) {
//                return addLineBreak();
//            }
//            if (type2 == ARGUMENT_LIST) {
//                if (type1 == MULTI_LINE_COMMENT && isEmbeddedComment(type1, child1)) {
//                    if (!hasNewlineInText(node1)) {
//                        return addSingleSpaceIf(true);
//                    }
//                }
//                return addLineBreak();
//            }
//        }
//
//
//        if (type1 == COMMA) {
//            if (type2 == RBRACKET) {
//                TextRange range = myNode.getTextRange();
//                return Spacing.createDependentLFSpacing(0, 0, range, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
//            }
//            return addSingleSpaceIf(cmSettings.SPACE_AFTER_COMMA && type2 != RBRACE && type2 != RBRACKET);
//        }
//
//        //todo: customize in settings
//
//        if (type1 == IF_STATEMENT ||
//                type1 == SWITCH_STATEMENT ||
//                type1 == TRY_STATEMENT ||
//                type1 == DO_WHILE_STATEMENT ||
//                type1 == FOR_STATEMENT ||
//                type1 == FOREACH_STATEMENT ||
//                type1 == SWITCH_CASE ||
//                type1 == DEFAULT_CASE ||
//                type1 == WHILE_STATEMENT) {
//            return addLineBreak();
//        }
//
//        if (COMMENTS.contains(type2)) {
//            return Spacing.createSpacing(1, 1, 0, true, cmSettings.KEEP_BLANK_LINES_IN_CODE);
//        }
//
//        if (TOKENS_WITH_SPACE_AFTER.contains(type1) || KEYWORDS_WITH_SPACE_BEFORE.contains(type2)) {
//            return addSingleSpaceIf(true);
//        }
//
//        if (elementType == FOR_LOOP_PARTS && type1 == SEMICOLON) {
//            return addSingleSpaceIf(true);
//        }
//
//        // TODO: references
////        if (elementType == VALUE_EXPRESSION && type2 == CASCADE_REFERENCE_EXPRESSION) {
////            if (type1 == CASCADE_REFERENCE_EXPRESSION) {
////                if (cascadesAreSameMethod(((AbstractBlock)child1).getNode(), ((AbstractBlock)child2).getNode())) {
////                    return Spacing.createSpacing(0, 0, 0, false, 0);
////                }
////            }
////            else if (type1 == REFERENCE_EXPRESSION || isSimpleLiteral(type1)) {
////                CompositeElement elem = (CompositeElement)myNode;
////                ASTNode[] childs = elem.getChildren(CASCADE_REFERENCE_EXPRESSION_SET);
////                if (childs.length == 1) {
////                    return Spacing.createDependentLFSpacing(0, 0, myNode.getTextRange(), true, 0);
////                }
////                if (allCascadesAreSameMethod(childs)) {
////                    return Spacing.createSpacing(0, 0, 0, false, 0);
////                }
////            }
////            return addLineBreak();
////        }
//
//        if (elementType == ARRAY_EXPRESSION && type2 == RBRACKET) {
//            return Spacing.createDependentLFSpacing(0, 0, node1.getTextRange(), cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
//        }
//
//        if (elementType == FUNCTION_DEFAULT_PARAMETER) {
//            if (type1 == EQ) {
//                return addSingleSpaceIf(true);
//            }
//            if (type2 == EQ) {
//                return noSpace();
//            }
//        }
//
//        if (type2 == RBRACKET && type1 == ARRAY_EXPRESSION) {
//            return noSpace();
//        }
//
//        if (elementType == REFERENCE_EXPRESSION && type2 == DOT) {
//            return createSpacingForCallChain(collectSurroundingMessageSends(), node2);
//        }
//        if (type1 == DOT) {
//            return noSpace(); // Seems odd that no plugin has a setting for spaces around DOT -- need a Lisp mode!
//        }
//
//        if (type1 == RETURN && type2 != SEMICOLON) {
//            return addSingleSpaceIf(true);
//        }

        return Spacing.createSpacing(0, 1, 0, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
    }

    @Nullable
    private Spacing setSpacingBeforeParentheses(IElementType type1, IElementType type2, IElementType elementType) {
        if (type2 == ARGUMENTS && elementType == CALL_EXPRESSION) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES);
        }
        else if (type2 == PARAMETERS && FUNCTION_DEFINITION.contains(elementType)) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_METHOD_PARENTHESES);
        }
        else if (type2 == PARAMETERS && elementType == FUNCTION_EXPRESSION) {
            return addSingleSpaceIf(sqSettings.SPACE_BEFORE_FUNCTION_EXPRESSION_PARENTHESES);
        }
        // Always remove spaces after lambda @ sign
        else if (type2 == PARAMETERS && elementType == LAMBDA_FUNCTION_EXPRESSION) {
            return noSpace();
        }
        else if (type2 == LPAREN) {
            if (elementType == IF_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_IF_PARENTHESES);
            }
            else if (elementType == FOR_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_FOR_PARENTHESES);
            }
            else if (elementType == FOREACH_STATEMENT) {
                return addSingleSpaceIf(sqSettings.SPACE_BEFORE_FOREACH_PARENTHESES);
            }
            else if (elementType == WHILE_STATEMENT || elementType == DO_WHILE_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_WHILE_PARENTHESES);
            }
            else if (elementType == SWITCH_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_SWITCH_PARENTHESES);
            }
            else if (elementType == CATCH_PART) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_CATCH_PARENTHESES);
            }
        }
        return null;
    }

    @Nullable
    private Spacing setSpacingAroundOperators(IElementType type1, IElementType type2, IElementType elementType) {
        if (Arrays.asList(CONST_DECLARATION, ENUM_ITEM, DEFAULT_PARAMETER, TABLE_ITEM, CLASS_MEMBER).contains(elementType) &&
                (type1 == EQ || type2 == EQ)) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
        }
        else if ((type1 == EQ && elementType == VAR_INIT) || type2 == VAR_INIT) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
        }
        else if ((type1 == ASSIGNMENT_OPERATOR || type2 == ASSIGNMENT_OPERATOR) && elementType == ASSIGN_EXPRESSION) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
        }
        else if (LOGIC_OPERATORS.contains(type1) || LOGIC_OPERATORS.contains(type2)) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_LOGICAL_OPERATORS);
        }
        else if (type1 == EQUALITY_OPERATOR || type2 == EQUALITY_OPERATOR) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_EQUALITY_OPERATORS);
        }
        else if (type1 == RELATIONAL_OPERATOR || type2 == RELATIONAL_OPERATOR) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_RELATIONAL_OPERATORS);
        }
        else if (BITWISE_OPERATORS.contains(type1) || BITWISE_OPERATORS.contains(type2)) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_BITWISE_OPERATORS);
        }
        else if ((type1 == ADDITIVE_OPERATOR || type2 == ADDITIVE_OPERATOR) && elementType != PREFIX_EXPRESSION && elementType != UNARY_EXPRESSION) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_ADDITIVE_OPERATORS);
        }
        else if (type1 == MULTIPLICATIVE_OPERATOR || type2 == MULTIPLICATIVE_OPERATOR) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS);
        }
        else if (type1 == SHIFT_OPERATOR || type2 == SHIFT_OPERATOR) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_SHIFT_OPERATORS);
        }
        else if (type1 == PREFIX_OPERATOR || type2 == PREFIX_OPERATOR) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_UNARY_OPERATOR);
        }
        else if (type1 == UNARY_OPERATOR) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_UNARY_OPERATOR);
        }
        return null;
    }

    @Nullable
    private Spacing setSpacingBeforeLeftBrace(IElementType type1, IElementType type2, IElementType elementType, IElementType parentType, final Block child1, final Block child2) {
        // TODO: tests with wrap (child1 range should be probably changed to something else)
        // TODO: tests with brace styles
        if ((elementType == CLASS_DECLARATION || elementType == CLASS_EXPRESSION) && type2 == CLASS_BODY) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_CLASS_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == ENUM_DECLARATION && type2 == LBRACE) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_CLASS_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if ((FUNCTION_DEFINITION.contains(elementType) || elementType == FUNCTION_EXPRESSION) && type2 == FUNCTION_BODY) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_METHOD_LBRACE, cmSettings.METHOD_BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == LAMBDA_FUNCTION_EXPRESSION && type1 == PARAMETERS) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_METHOD_LBRACE, cmSettings.METHOD_BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == IF_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_IF_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == IF_STATEMENT && type1 == ELSE) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_ELSE_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == SWITCH_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_SWITCH_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == WHILE_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_WHILE_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == DO_WHILE_STATEMENT && type1 == DO) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_DO_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == FOR_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_FOR_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == FOREACH_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(sqSettings.SPACE_BEFORE_FOREACH_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == TRY_STATEMENT && type1 == TRY) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_TRY_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        else if (elementType == CATCH_PART && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_CATCH_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
        }
        return null;
    }

    @Nullable
    private Spacing setSpacingBeforeKeywords(IElementType type1, IElementType type2, IElementType elementType) {
        if (type2 == ELSE) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_ELSE_KEYWORD, cmSettings.ELSE_ON_NEW_LINE);
        }
        else if (type2 == WHILE) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_WHILE_KEYWORD, cmSettings.WHILE_ON_NEW_LINE);
        }
        else if (type2 == CATCH_PART) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_CATCH_KEYWORD, cmSettings.CATCH_ON_NEW_LINE);
        }
        return null;
    }

    @Nullable
    private Spacing setSpacingWithin(IElementType type1, IElementType type2, IElementType elementType) {
        if (type1 == LBRACKET || type2 == RBRACKET) {
            if (type1 == LBRACKET && type2 == RBRACKET) {
                return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACKETS);
            }
            else {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_BRACKETS);
            }
        }
        else if (type1 == LBRACE || type2 == RBRACE) {
            if (type1 == LBRACE && type2 == RBRACE) {
                return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACES);
            }
            else {
                if (cmSettings.SPACE_WITHIN_BRACES) {
                    return Spacing.createSpacing(1, 1, 0, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
                }
                else {
                    // We should keep an optional spaces in it is already there.
                    return Spacing.createSpacing(0, 1, 0, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
                }
            }
        }
        else if (type1 == LPAREN || type2 == RPAREN) {
            if (elementType == IF_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_IF_PARENTHESES);
            }
            else if (elementType == FOR_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_FOR_PARENTHESES);
            }
            else if (elementType == FOREACH_STATEMENT) {
                return addSingleSpaceIf(sqSettings.SPACE_WITHIN_FOREACH_PARENTHESES);
            }
            else if (elementType == WHILE_STATEMENT || elementType == DO_WHILE_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_WHILE_PARENTHESES);
            }
            else if (elementType == SWITCH_STATEMENT) {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_SWITCH_PARENTHESES);
            }
            else if (elementType == CATCH_PART) {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_CATCH_PARENTHESES);
            }
            else if (elementType == PARAMETERS) {
                if (type1 == LPAREN && type2 == RPAREN) {
                    return addSingleSpaceIf(cmSettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES);
                }
                else {
                    final boolean newLineNeeded = type1 == LPAREN ? cmSettings.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE : cmSettings.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE;

                    if (newLineNeeded || cmSettings.SPACE_WITHIN_METHOD_PARENTHESES) {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_METHOD_PARENTHESES, newLineNeeded);
                    }
                    return Spacing.createSpacing(0, 0, 0, false, 0);
                }
            }
            else if (elementType == ARGUMENTS) {
                if (type1 == LPAREN && type2 == RPAREN) {
                    return addSingleSpaceIf(cmSettings.SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES);
                }
                else {
                    final boolean newLineNeeded =
                            type1 == LPAREN ? cmSettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE : cmSettings.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE;

                    return addSingleSpaceIf(cmSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, newLineNeeded);
                }
            }
            else if (elementType == PARENTHESIZED_EXPRESSION) {
                final boolean newLineNeeded =
                        type1 == LPAREN ? cmSettings.PARENTHESES_EXPRESSION_LPAREN_WRAP : cmSettings.PARENTHESES_EXPRESSION_RPAREN_WRAP;
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_PARENTHESES, newLineNeeded);
            }
        }
        return null;
    }

    @Nullable
    private Spacing setSpacingInTernaryOperator(IElementType type1, IElementType type2, IElementType elementType) {
        if (elementType == TERNARY_EXPRESSION) {
            if (type2 == QUESTION) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_QUEST);
            }
            else if (type2 == COLON) {
                return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COLON);
            }
            else if (type1 == QUESTION) {
                return addSingleSpaceIf(cmSettings.SPACE_AFTER_QUEST);
            }
            else if (type1 == COLON) {
                return addSingleSpaceIf(cmSettings.SPACE_AFTER_COLON);
            }
        }
        return null;
    }

    @Nullable
    private Spacing setSpacingOther(IElementType type1, IElementType type2, IElementType elementType, IElementType parentType) {
        // COMMAS
        if (type2 == COMMA) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COMMA);
        }
        else if (type1 == COMMA) {
            return addSingleSpaceIf(cmSettings.SPACE_AFTER_COMMA);
        }
        // SEMICOLONS
        else if ((elementType == FOR_LOOP_PARTS || elementType == CLASS_MEMBERS) && type2 == SEMICOLON) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_SEMICOLON);
        }
        else if ((elementType == FOR_LOOP_PARTS || elementType == CLASS_MEMBERS) && type1 == SEMICOLON) {
            return addSingleSpaceIf(cmSettings.SPACE_AFTER_SEMICOLON);
        }
        // Remove whitespace around regular semicolons (except new lines)
        else if (type1 == SEMICOLON || type2 == SEMICOLON) {
            return Spacing.createSpacing(0, 0, 0, true, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        // Always separate table items with no delimiter
        else if (type1 == TABLE_ITEM && type2 == TABLE_ITEM) {
            return oneSpace();
        }
        else if (type1 == CLASS_MEMBER && type2 == CLASS_MEMBER) {
            return oneSpace();
        }
        else if ((type1 == RETURN || type1 == YIELD || type1 == THROW) && !SEMICOLONS.contains(type2)) {
            return addSingleSpaceIf(true);
        }

        return null;
    }

    @Nullable
    private Spacing setSpacingMultiline(IElementType type1, IElementType type2, IElementType elementType, IElementType parentType) {
        if (!COMMENTS.contains(type2) && parentType == BLOCK) {
            return addLineBreak();
        }
        else if (elementType == STATEMENT && (parentType == SWITCH_CASE || parentType == DEFAULT_CASE)) {
            return Spacing.createSpacing(0, 0, 1, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        // No blank line before closing brace in switch statement.
        if (type2 == RBRACE && (type1 == SWITCH_CASE || type1 == DEFAULT_CASE)) {
            return Spacing.createSpacing(0, 0, 1, false, 0);
        }
        // No blank line before first statement of a case.
        if (type1 == COLON && (elementType == SWITCH_CASE || elementType == DEFAULT_CASE)) {
            return Spacing.createSpacing(0, 0, 1, false, 0);
        }
        // No blank line before first case of a switch.
        if (elementType == SWITCH_STATEMENT && type1 == LBRACE) {
            return Spacing.createSpacing(0, 0, 1, false, 0);
        }


        if (type1 == STATEMENT || type2 == STATEMENT) {
            return addLineBreak();
        }

        if (type1 == CLASS_MEMBERS || type2 == CLASS_MEMBERS) {
            if (type1 == MULTI_LINE_COMMENT) {
                return addSingleSpaceIf(true, false);
            }
            else {
                return addSingleSpaceIf(false, true);
            }
        }

        return null;
    }







    private Spacing addLineBreak() {
        return Spacing.createSpacing(0, 0, 1, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
    }

    private Spacing addSingleSpaceIf(boolean condition) {
        return addSingleSpaceIf(condition, false);
    }

    private Spacing addSingleSpaceIf(boolean condition, boolean linesFeed) {
        final int spaces = condition ? 1 : 0;
        final int lines = linesFeed ? 1 : 0;
        return Spacing.createSpacing(spaces, spaces, lines, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
    }

    private Spacing noSpace() {
        return Spacing.createSpacing(0, 0, 0, cmSettings.KEEP_LINE_BREAKS, 0);
    }

    private Spacing oneSpace() {
        return Spacing.createSpacing(1, 1, 0, cmSettings.KEEP_LINE_BREAKS, 0);
    }

    private Spacing setBraceSpace(boolean needSpaceSetting,
                                  @CommonCodeStyleSettings.BraceStyleConstant int braceStyleSetting,
                                  TextRange textRange) {
        final int spaces = needSpaceSetting ? 1 : 0;
        if (braceStyleSetting == CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED && textRange != null) {
            return Spacing.createDependentLFSpacing(spaces, spaces, textRange, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        else {
            final int lineBreaks =
                    braceStyleSetting == CommonCodeStyleSettings.END_OF_LINE || braceStyleSetting == CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED
                            ? 0
                            : 1;
            return Spacing.createSpacing(spaces, spaces, lineBreaks, false, 0);
        }
    }

    private static boolean doesMessageHaveArguments(ASTNode node) {
        // node is a DOT
        ASTNode parent = node.getTreeParent().getTreeParent();
        if (parent == null) return false;
        if (parent.getElementType() != CALL_EXPRESSION) return false;
        ASTNode args = parent.getLastChildNode();
        if (args == null) return false;
        return args.getElementType() == ARGUMENTS;
    }

    private static Comparator<ASTNode> textRangeSorter() {
        return new Comparator<ASTNode>() {
            @Override
            public int compare(ASTNode o1, ASTNode o2) {
                return o1.getTextRange().getStartOffset() - o2.getTextRange().getStartOffset();
            }
        };
    }

    private CallChain collectSurroundingMessageSends() {
        CallChain calls = new CallChain();
        collectPredecessorMessageSends(calls);
        collectSuccessorMessageSends(calls);
        return calls;
    }

    private void collectPredecessorMessageSends(CallChain calls) {
        ASTNode node = myNode;
        while (node != null) {
            IElementType type = node.getElementType();
            if (type == REFERENCE_EXPRESSION) {
                collectDotIfMessageSend(calls, node);
                node = node.getTreeParent();
            } else if (type == CALL_EXPRESSION) {
                if (hasMultilineFunctionArgument(node)) {
                    calls.isFollowedByHardNewline = true;
                    break;
                }
                node = node.getTreeParent();
            } else {
                break;
            }
        }
    }

    private void collectSuccessorMessageSends(CallChain calls) {
        ASTNode node = myNode;
        while (node != null) {
            IElementType type = node.getElementType();
            if (type == CALL_EXPRESSION) {
                if (hasMultilineFunctionArgument(node)) {
                    calls.isPrecededByHardNewline = true;
                    break;
                }
                node = node.getFirstChildNode();
            } else if (type == REFERENCE_EXPRESSION) {
                collectDotIfMessageSend(calls, node);
                node = node.getFirstChildNode();
            } else {
                break;
            }
        }
    }

    private static void collectDotIfMessageSend(CallChain calls, ASTNode node) {
        ASTNode child = node.getFirstChildNode();
        child = FormatterUtil.getNextNonWhitespaceSibling(child);
        if (child != null) {
            IElementType childType = child.getElementType();
            if (childType == DOT) {
                calls.add(child);
            }
        }
    }

    private static boolean hasMultilineFunctionArgument(ASTNode node) {
        ASTNode args = node.getLastChildNode();
        ASTNode first = args == null ? null : args.getFirstChildNode();
        args = first == null ? null : first.getTreeNext();
        if (args != null && args.getElementType() == ARGUMENT_LIST) {
            ASTNode arg = args.getFirstChildNode();
            int n = 1;
            while (arg != null) {
                // TODO Max 9 args is totally arbitrary, possibly not even desirable.
                if (n++ == 10 || arg.getElementType() == FUNCTION_EXPRESSION) {
                    if (arg.getText().indexOf('\n') >= 0) {
                        return true;
                    }
                }
                arg = arg.getTreeNext();
            }
        }
        return false;
    }

    private Spacing createSpacingForCallChain(CallChain calls, ASTNode node2) {
        // The rules involving call chains, like m.a.b().c.d(), are complex.
        if (calls.list.size() < 2) {
            return noSpace();
        }
        //if (calls.isPrecededByHardNewline) {
        //  // Rule: allow an inline chain before a hard newline but not after.
        //  return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        //}
        boolean isAllProperties = true;
        boolean mustSplit = false;
        boolean mustStopAtNextMethod = false;
        List<TextRange> ranges = new ArrayList<TextRange>();
        for (ASTNode node : calls.list) {
            if (doesMessageHaveArguments(node)) {
                if (mustStopAtNextMethod) {
                    return Spacing.createDependentLFSpacing(0, 0, ranges, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
                }
                isAllProperties = false;
            }
            else {
                if (!isAllProperties) {
                    // Rule: split properties in a method chain.
                    mustSplit = true;
                }
            }
            TextRange range = node.getTextRange();
            ranges.add(new TextRange(range.getStartOffset() - 1, range.getEndOffset()));
            if (node2 == node && isAllProperties) {
                // Rule: do not split leading properties (unless too long to fit).
                mustStopAtNextMethod = true;
            }
        }
        // Not sure how to implement rule: split before all properties if they don't fit on two lines. TWO lines !?
        if (isAllProperties && ranges.size() > 7) {
            mustSplit = true;
        }
        if (mustSplit) {
            return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        } else {
            return Spacing.createDependentLFSpacing(0, 0, ranges, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
    }

    private static boolean allCascadesAreSameMethod(ASTNode[] children) {
        for (int i = 1; i < children.length; i++) {
            if (!cascadesAreSameMethod(children[i - 1], children[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean cascadesAreSameMethod(ASTNode child1, ASTNode child2) {
        ASTNode call1 = child1.getLastChildNode();
        if (call1.getElementType() == CALL_EXPRESSION) {
            ASTNode call2 = child2.getLastChildNode();
            if (call2.getElementType() == CALL_EXPRESSION) {
                String name1 = getImmediateCallName(call1);
                if (name1 != null) {
                    String name2 = getImmediateCallName(call2);
                    if (name1.equals(name2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String getImmediateCallName(ASTNode callNode) {
        ASTNode[] childs = callNode.getChildren(REFERENCE_EXPRESSION_SET);
        if (childs.length != 1) return null;
        ASTNode child = childs[0];
        childs = child.getChildren(ID_SET);
        if (childs.length != 1) return null;
        child = childs[0];
        return child.getText();
    }

    private static boolean isSpaceNeededBetweenPrefixOps(ASTNode node1, ASTNode node2) {
        String op1 = node1.getText();
        String op2 = node2.getText();
        return op1.endsWith(op2.substring(op2.length() - 1));
    }

    private static boolean isSimpleLiteral(IElementType nodeType) {
        // Literals that can be cascade receivers, excluding map and list.
        return SIMPLE_LITERAL_SET.contains(nodeType);
    }

    private static boolean needsBlankLineBeforeFunction(IElementType elementType) {
        return elementType == SQUIRREL_FILE ||
                elementType == CLASS_MEMBERS;
    }

    private static boolean isEmbeddedComment(IElementType type, Block block) {
        return COMMENTS.contains(type) && (!isDirectlyPrecededByNewline(block) || isDirectlyPrecededByBlockComment(block));
    }

    private static boolean isDirectlyPrecededByNewline(Block child) {
        // The child is a line comment whose parent is the SQUIRREL_FILE.
        // Return true if it is (or will be) at the beginning of the line, or
        // following a block comment that is at the beginning of the line.
        ASTNode node = ((SquirrelBlock)child).getNode();
        return isDirectlyPrecededByNewline(node);
    }

    private static boolean isDirectlyPrecededByNewline(ASTNode node) {
        while ((node = node.getTreePrev()) != null) {
            if (WHITE_SPACES.contains(node.getElementType())) {
                if (node.getText().contains("\n")) return true;
                continue;
            }
            if (node.getElementType() == MULTI_LINE_COMMENT) {
                if (node.getTreePrev() == null) {
                    return true;
                }
                continue;
            }
            break;
        }
        return false;
    }

    private static boolean isDirectlyPrecededByBlockComment(Block child) {
        ASTNode node = ((SquirrelBlock)child).getNode();
        return isDirectlyPrecededByBlockComment(node);
    }

    private static boolean isDirectlyPrecededByBlockComment(ASTNode node) {
        while ((node = node.getTreePrev()) != null) {
            if (WHITE_SPACES.contains(node.getElementType())) {
                if (node.getText().contains("\n")) return false;
                continue;
            }
            if (node.getElementType() == MULTI_LINE_COMMENT) {
                return true;
            }
            break;
        }
        return false;
    }

    private TextRange textRangeFollowingMetadata() {
        TextRange range = myNode.getTextRange();
        ASTNode child = myNode.getFirstChildNode();
        return range;
    }

    // squirrel_style recognizes three forms of "empty block":
    //   e() {}
    //   f() => expr;
    //   M() : super();
    private static boolean hasEmptyBlock(ASTNode node) {
        if (node.getElementType() == CLASS_DECLARATION) return false;
        ASTNode child = node;
        while (true) {
            child = child.getLastChildNode();
            if (child == null) return false;
            if (WHITE_SPACES.contains(child.getElementType())) child = FormatterUtil.getPreviousNonWhitespaceSibling(child);
            if (child == null) return false;
            // TODO: lambda
//            if (child.getElementType() == FUNCTION_BODY) {
//                ASTNode next = child.getLastChildNode();
//                if (WHITE_SPACES.contains(next.getElementType())) next = FormatterUtil.getPreviousNonWhitespaceSibling(next);
//                if (next != null && next.getElementType() == SEMICOLON) {
//                    next = FormatterUtil.getPreviousNonWhitespaceSibling(next);
//                    if (next != null && EXPRESSIONS.contains(next.getElementType())) {
//                        ASTNode arrow = FormatterUtil.getPreviousNonWhitespaceSibling(next);
//                        if (arrow != null && arrow.getElementType() == EXPRESSION_BODY_DEF) {
//                            return true;
//                        }
//                    }
//                    return false;
//                }
//                // Look inside the function body.
//                continue;
//            }
            if (child.getElementType() != BLOCK) continue;
            child = child.getLastChildNode();
            if (child == null) return false;
            if (WHITE_SPACES.contains(child.getElementType())) child = FormatterUtil.getPreviousNonWhitespaceSibling(child);
            if (child == null) return false;
            if (child.getElementType() != RBRACE) return false;
            child = FormatterUtil.getPreviousNonWhitespaceSibling(child);
            return child != null && child.getElementType() == LBRACE;
        }
    }

    private static boolean hasNewlineInText(ASTNode node) {
        String comment = node.getText();
        return comment.indexOf('\n') > 0;
    }

    private static boolean isBlankLineAfterComment(ASTNode node) {
        // Assumes whitespace has been normalized.
        ASTNode next = node.getTreeNext();
        if (next == null || !WHITE_SPACES.contains(next.getElementType())) return false;
        String comment = next.getText();
        int n = comment.indexOf('\n');
        return comment.indexOf('\n', n + 1) > 0;
    }

    private static class CallChain {
        SortedList<ASTNode> list = new SortedList<ASTNode>(textRangeSorter());
        boolean isPrecededByHardNewline = false;
        boolean isFollowedByHardNewline = false;

        void add(ASTNode node) {
            if (!list.contains(node)) {
                list.add(node);
            }
        }
    }
}
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
    public static Spacing getSpacing(final Block child1, final Block child2, ASTNode myNode, CommonCodeStyleSettings
            cmSettings, SquirrelCodeStyleSettings sqSettings) {

        if (!(child1 instanceof AbstractBlock) || !(child2 instanceof AbstractBlock)) {
            return null;
        }

        final IElementType elementType = myNode.getElementType();
        final IElementType parentType = myNode.getTreeParent() == null ? null : myNode.getTreeParent().getElementType();
        final ASTNode node1 = ((AbstractBlock) child1).getNode();
        final ASTNode node2 = ((AbstractBlock) child2).getNode();
        final IElementType type1 = node1.getElementType();
        final IElementType type2 = node2.getElementType();

        class SpacingHelper {
            Spacing get() {
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

                return Spacing.createSpacing(0, 1, 0, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
            }

            Spacing setSpacingBeforeParentheses(IElementType type1, IElementType type2, IElementType elementType) {
                if (type2 == ARGUMENTS && elementType == CALL_EXPRESSION) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES);
                } else if (type2 == PARAMETERS && FUNCTION_DEFINITION.contains(elementType)) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_METHOD_PARENTHESES);
                } else if (type2 == PARAMETERS && elementType == FUNCTION_EXPRESSION) {
                    return addSingleSpaceIf(sqSettings.SPACE_BEFORE_FUNCTION_EXPRESSION_PARENTHESES);
                }
                // Always remove spaces after lambda @ sign
                else if (type2 == PARAMETERS && elementType == LAMBDA_FUNCTION_EXPRESSION) {
                    return noSpace();
                } else if (type2 == LPAREN) {
                    if (elementType == IF_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_IF_PARENTHESES);
                    } else if (elementType == FOR_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_FOR_PARENTHESES);
                    } else if (elementType == FOREACH_STATEMENT) {
                        return addSingleSpaceIf(sqSettings.SPACE_BEFORE_FOREACH_PARENTHESES);
                    } else if (elementType == WHILE_STATEMENT || elementType == DO_WHILE_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_WHILE_PARENTHESES);
                    } else if (elementType == SWITCH_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_SWITCH_PARENTHESES);
                    } else if (elementType == CATCH_PART) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_CATCH_PARENTHESES);
                    }
                }
                return null;
            }

            Spacing setSpacingAroundOperators(IElementType type1, IElementType type2, IElementType elementType) {
                if (Arrays.asList(CONST_DECLARATION, ENUM_ITEM, DEFAULT_PARAMETER, TABLE_ITEM, CLASS_MEMBER).contains
                        (elementType) &&
                        (type1 == EQ || type2 == EQ)) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
                } else if ((type1 == EQ && elementType == VAR_INIT) || type2 == VAR_INIT) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
                } else if ((type1 == ASSIGNMENT_OPERATOR || type2 == ASSIGNMENT_OPERATOR) && elementType ==
                        ASSIGN_EXPRESSION) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
                } else if (LOGIC_OPERATORS.contains(type1) || LOGIC_OPERATORS.contains(type2)) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_LOGICAL_OPERATORS);
                } else if (type1 == EQUALITY_OPERATOR || type2 == EQUALITY_OPERATOR) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_EQUALITY_OPERATORS);
                } else if (type1 == RELATIONAL_OPERATOR || type2 == RELATIONAL_OPERATOR) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_RELATIONAL_OPERATORS);
                } else if (BITWISE_OPERATORS.contains(type1) || BITWISE_OPERATORS.contains(type2)) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_BITWISE_OPERATORS);
                } else if ((type1 == ADDITIVE_OPERATOR || type2 == ADDITIVE_OPERATOR) && elementType != PREFIX_EXPRESSION

                        && elementType != UNARY_EXPRESSION) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_ADDITIVE_OPERATORS);
                } else if (type1 == MULTIPLICATIVE_OPERATOR || type2 == MULTIPLICATIVE_OPERATOR) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS);
                } else if (type1 == SHIFT_OPERATOR || type2 == SHIFT_OPERATOR) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_SHIFT_OPERATORS);
                } else if (type1 == PREFIX_OPERATOR || type2 == PREFIX_OPERATOR) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_UNARY_OPERATOR);
                } else if (type1 == UNARY_OPERATOR) {
                    return addSingleSpaceIf(cmSettings.SPACE_AROUND_UNARY_OPERATOR);
                }
                return null;
            }

            Spacing setSpacingBeforeLeftBrace(IElementType type1, IElementType type2, IElementType
                    elementType, IElementType parentType, final Block child1, final Block child2) {
                // TODO: tests with wrap (child1 range should be probably changed to something else)
                // TODO: tests with brace styles
                if ((elementType == CLASS_DECLARATION || elementType == CLASS_EXPRESSION) && type2 == CLASS_BODY) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_CLASS_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange());
                } else if (elementType == ENUM_DECLARATION && type2 == LBRACE) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_CLASS_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange());
                } else if ((FUNCTION_DEFINITION.contains(elementType) || elementType == FUNCTION_EXPRESSION) && type2 ==
                        FUNCTION_BODY) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_METHOD_LBRACE, cmSettings.METHOD_BRACE_STYLE, child1
                            .getTextRange());
                } else if (elementType == LAMBDA_FUNCTION_EXPRESSION && type1 == PARAMETERS) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_METHOD_LBRACE, cmSettings.METHOD_BRACE_STYLE, child1
                            .getTextRange());
                } else if (elementType == IF_STATEMENT && type1 == RPAREN) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_IF_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
                } else if (elementType == IF_STATEMENT && type1 == ELSE) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_ELSE_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange
                                    ());
                } else if (elementType == SWITCH_STATEMENT && type1 == RPAREN) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_SWITCH_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange());
                } else if (elementType == WHILE_STATEMENT && type1 == RPAREN) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_WHILE_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange());
                } else if (elementType == DO_WHILE_STATEMENT && type1 == DO) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_DO_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
                } else if (elementType == FOR_STATEMENT && type1 == RPAREN) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_FOR_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
                } else if (elementType == FOREACH_STATEMENT && type1 == RPAREN) {
                    return setBraceSpace(sqSettings.SPACE_BEFORE_FOREACH_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange());
                } else if (elementType == TRY_STATEMENT && type1 == TRY) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_TRY_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange());
                } else if (elementType == CATCH_PART && type1 == RPAREN) {
                    return setBraceSpace(cmSettings.SPACE_BEFORE_CATCH_LBRACE, cmSettings.BRACE_STYLE, child1
                            .getTextRange());
                }
                return null;
            }

            Spacing setSpacingBeforeKeywords(IElementType type1, IElementType type2, IElementType elementType) {
                if (type2 == ELSE) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_ELSE_KEYWORD, cmSettings.ELSE_ON_NEW_LINE);
                } else if (type2 == WHILE) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_WHILE_KEYWORD, cmSettings.WHILE_ON_NEW_LINE);
                } else if (type2 == CATCH_PART) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_CATCH_KEYWORD, cmSettings.CATCH_ON_NEW_LINE);
                }
                return null;
            }

            Spacing setSpacingWithin(IElementType type1, IElementType type2, IElementType elementType) {
                if (type1 == LBRACKET || type2 == RBRACKET) {
                    if (type1 == LBRACKET && type2 == RBRACKET) {
                        return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACKETS);
                    } else {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_BRACKETS);
                    }
                } else if ((type1 == LBRACE || type2 == RBRACE) || (type1 == CLASS_ATTR_START || type2 == CLASS_ATTR_END)) {
                    if ((type1 == LBRACE && type2 == RBRACE) || (type1 == CLASS_ATTR_START && type2 == CLASS_ATTR_END)) {
                        return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACES);
                    } else {
                        if (cmSettings.SPACE_WITHIN_BRACES) {
                            return Spacing.createSpacing(1, 1, 0, cmSettings.KEEP_LINE_BREAKS, cmSettings
                                    .KEEP_BLANK_LINES_IN_CODE);
                        } else {
                            // We should keep an optional spaces in it is already there.
                            return Spacing.createSpacing(0, 1, 0, cmSettings.KEEP_LINE_BREAKS, cmSettings
                                    .KEEP_BLANK_LINES_IN_CODE);
                        }
                    }
                } else if (type1 == LPAREN || type2 == RPAREN) {
                    if (elementType == IF_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_IF_PARENTHESES);
                    } else if (elementType == FOR_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_FOR_PARENTHESES);
                    } else if (elementType == FOREACH_STATEMENT) {
                        return addSingleSpaceIf(sqSettings.SPACE_WITHIN_FOREACH_PARENTHESES);
                    } else if (elementType == WHILE_STATEMENT || elementType == DO_WHILE_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_WHILE_PARENTHESES);
                    } else if (elementType == SWITCH_STATEMENT) {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_SWITCH_PARENTHESES);
                    } else if (elementType == CATCH_PART) {
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_CATCH_PARENTHESES);
                    } else if (elementType == PARAMETERS) {
                        if (type1 == LPAREN && type2 == RPAREN) {
                            return addSingleSpaceIf(cmSettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES);
                        } else {
                            final boolean newLineNeeded = type1 == LPAREN ? cmSettings
                                    .METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE : cmSettings
                                    .METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE;

                            if (newLineNeeded || cmSettings.SPACE_WITHIN_METHOD_PARENTHESES) {
                                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_METHOD_PARENTHESES, newLineNeeded);
                            }
                            return Spacing.createSpacing(0, 0, 0, false, 0);
                        }
                    } else if (elementType == ARGUMENTS) {
                        if (type1 == LPAREN && type2 == RPAREN) {
                            return addSingleSpaceIf(cmSettings.SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES);
                        } else {
                            final boolean newLineNeeded =
                                    type1 == LPAREN ? cmSettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE : cmSettings
                                            .CALL_PARAMETERS_RPAREN_ON_NEXT_LINE;

                            return addSingleSpaceIf(cmSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, newLineNeeded);
                        }
                    } else if (elementType == PARENTHESIZED_EXPRESSION) {
                        final boolean newLineNeeded =
                                type1 == LPAREN ? cmSettings.PARENTHESES_EXPRESSION_LPAREN_WRAP : cmSettings
                                        .PARENTHESES_EXPRESSION_RPAREN_WRAP;
                        return addSingleSpaceIf(cmSettings.SPACE_WITHIN_PARENTHESES, newLineNeeded);
                    }
                }
                return null;
            }

            Spacing setSpacingInTernaryOperator(IElementType type1, IElementType type2, IElementType elementType) {
                if (elementType == TERNARY_EXPRESSION) {
                    if (type2 == QUESTION) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_QUEST);
                    } else if (type2 == COLON) {
                        return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COLON);
                    } else if (type1 == QUESTION) {
                        return addSingleSpaceIf(cmSettings.SPACE_AFTER_QUEST);
                    } else if (type1 == COLON) {
                        return addSingleSpaceIf(cmSettings.SPACE_AFTER_COLON);
                    }
                }
                return null;
            }

            Spacing setSpacingOther(IElementType type1, IElementType type2, IElementType elementType, IElementType
                    parentType) {
                // COMMAS
                if (type2 == COMMA) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COMMA);
                } else if (type1 == COMMA) {
                    return addSingleSpaceIf(cmSettings.SPACE_AFTER_COMMA);
                }
                // SEMICOLONS
                else if ((elementType == FOR_LOOP_PARTS || elementType == CLASS_MEMBERS) && type2 == SEMICOLON) {
                    return addSingleSpaceIf(cmSettings.SPACE_BEFORE_SEMICOLON);
                } else if ((elementType == FOR_LOOP_PARTS || elementType == CLASS_MEMBERS) && type1 == SEMICOLON) {
                    return addSingleSpaceIf(cmSettings.SPACE_AFTER_SEMICOLON);
                }
                // Remove whitespace around regular semicolons (except new lines)
                else if (type1 == SEMICOLON || type2 == SEMICOLON) {
                    return Spacing.createSpacing(0, 0, 0, true, cmSettings.KEEP_BLANK_LINES_IN_CODE);
                }
                // Always separate table items with no delimiter
                else if (type1 == TABLE_ITEM && type2 == TABLE_ITEM) {
                    return oneSpace();
                } else if (type1 == CLASS_MEMBER && type2 == CLASS_MEMBER) {
                    return oneSpace();
                } else if ((type1 == RETURN || type1 == YIELD || type1 == THROW) && !SEMICOLONS.contains(type2)) {
                    return addSingleSpaceIf(true);
                }

                return null;
            }

            Spacing setSpacingMultiline(IElementType type1, IElementType type2, IElementType elementType, IElementType
                    parentType) {
                if (!COMMENTS.contains(type2) && parentType == BLOCK) {
                    return addLineBreak();
                } else if (elementType == STATEMENT && (parentType == SWITCH_CASE || parentType == DEFAULT_CASE)) {
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
                    } else {
                        return addSingleSpaceIf(false, true);
                    }
                }

                return null;
            }


            Spacing addLineBreak() {
                return Spacing.createSpacing(0, 0, 1, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
            }

            Spacing addSingleSpaceIf(boolean condition) {
                return addSingleSpaceIf(condition, false);
            }

            Spacing addSingleSpaceIf(boolean condition, boolean linesFeed) {
                final int spaces = condition ? 1 : 0;
                final int lines = linesFeed ? 1 : 0;
                return Spacing.createSpacing(spaces, spaces, lines, cmSettings.KEEP_LINE_BREAKS, cmSettings
                        .KEEP_BLANK_LINES_IN_CODE);
            }

            Spacing noSpace() {
                return Spacing.createSpacing(0, 0, 0, cmSettings.KEEP_LINE_BREAKS, 0);
            }

            Spacing oneSpace() {
                return Spacing.createSpacing(1, 1, 0, cmSettings.KEEP_LINE_BREAKS, 0);
            }

            Spacing setBraceSpace(boolean needSpaceSetting,
                                  @CommonCodeStyleSettings.BraceStyleConstant int braceStyleSetting,
                                  TextRange textRange) {
                final int spaces = needSpaceSetting ? 1 : 0;
                if (braceStyleSetting == CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED && textRange != null) {
                    return Spacing.createDependentLFSpacing(spaces, spaces, textRange, cmSettings.KEEP_LINE_BREAKS,
                            cmSettings.KEEP_BLANK_LINES_IN_CODE);
                } else {
                    final int lineBreaks =
                            braceStyleSetting == CommonCodeStyleSettings.END_OF_LINE || braceStyleSetting ==
                                    CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED
                                    ? 0
                                    : 1;
                    return Spacing.createSpacing(spaces, spaces, lineBreaks, false, 0);
                }
            }
        }

        return new SpacingHelper().get();
    }
}
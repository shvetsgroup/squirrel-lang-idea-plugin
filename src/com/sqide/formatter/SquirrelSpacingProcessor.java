package com.sqide.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.sqide.util.SqFormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.sqide.formatter.settings.SquirrelCodeStyleSettings;

import java.util.Arrays;

import static com.sqide.SquirrelTokenTypes.*;
import static com.sqide.SquirrelTokenTypesSets.*;

public class SquirrelSpacingProcessor {
    private Block child1;
    private Block child2;
    private ASTNode myNode;
    private CommonCodeStyleSettings cmSettings;
    private SquirrelCodeStyleSettings sqSettings;

    public SquirrelSpacingProcessor(final Block child1, final Block child2, ASTNode myNode, CommonCodeStyleSettings
            cmSettings, SquirrelCodeStyleSettings sqSettings) {
        this.child1 = child1;
        this.child2 = child2;
        this.myNode = myNode;
        this.cmSettings = cmSettings;
        this.sqSettings = sqSettings;
    }

    public Spacing getSpacing() {

        if (!(child1 instanceof AbstractBlock) || !(child2 instanceof AbstractBlock)) {
            return null;
        }

        final IElementType elementType = myNode.getElementType();
        final ASTNode parent = myNode.getTreeParent();
        final IElementType parentType = myNode.getTreeParent() == null ? null : myNode.getTreeParent().getElementType();
        final ASTNode node1 = ((AbstractBlock) child1).getNode();
        final ASTNode node2 = ((AbstractBlock) child2).getNode();
        final IElementType type1 = node1.getElementType();
        final IElementType type2 = node2.getElementType();

        // TODO Add dependent brace style
        // TODO add control statement on one line option
        // TODO wrap parameters
        // TODO wrap arguments
        // TODO wrap array initizliaer
        // TODO binary expressions wrap
        // TODO assignment expression sign
        // todo ternary expression wrap

        // @formatter:off

        if (BLOCKS_WITH_BRACES.contains(elementType)) {
            if (type1 == LBRACE || type2 == RBRACE) {
                if (type1 == LBRACE && type2 == RBRACE) {
                    return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACES, getBraceSettingsForElement(parent) == CommonCodeStyleSettings.NEXT_LINE);
                }
                else if (SqFormatterUtil.isSimpleBlock(myNode) && getKeepSimpleInOneLineSettingsForElement(parent)) {
                    return Spacing.createDependentLFSpacing(cmSettings.SPACE_WITHIN_BRACES ? 1 : 0, 1, myNode.getTextRange(), false, 0);
                }
                else {
                    return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
                }
            }
        }

        if ((STATEMENTS.contains(type1) && STATEMENTS.contains(type2)) || (type1 == SEMICOLON && STATEMENTS.contains(type2))) {
            return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        if (STATEMENTS.contains(type1) && type2 == SEMICOLON) {
            return noSpace();
        }

        // TODO: Add class members wrap
        // TODO: Wrap if class members wrap == always
        // Always add a new line between a class attributes and a class member.
        if (type1 == CLASS_ATTRIBUTE) {
            return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }

        if ((type1 == CLASS_MEMBER && type2 == CLASS_MEMBER) || (type1 == SEMICOLON && type2 == CLASS_MEMBER)) {
            return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        if (type1 == CLASS_MEMBER && type2 == SEMICOLON) {
            return noSpace();
        }

        // TODO: Add table items wrap
        if (((type1 == TABLE_ITEM && type2 == TABLE_ITEM) || (type1 == COMMA && type2 == TABLE_ITEM)) && elementType != CLASS_ATTRIBUTE) {
            return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        if (type1 == TABLE_ITEM && type2 == COMMA) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COMMA);
        }

        // TODO: Add enum items wrap
        if ((type1 == ENUM_ITEM && type2 == ENUM_ITEM)  || (type1 == COMMA && type2 == ENUM_ITEM)) {
            return Spacing.createSpacing(0, 0, 1, cmSettings.KEEP_LINE_BREAKS, cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        if (type1 == ENUM_ITEM && type2 == COMMA) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COMMA);
        }


        // TODO: test again when new lines in braces are ready.
        if (!COMMENTS.contains(type1) && FUNCTION_DEFINITION.contains(type2)) {
            return addLineBreak(2);
        }
        if (!COMMENTS.contains(type1) && type2 == CLASS_DECLARATION) {
            return addLineBreak(2);
        }



        // Spacing Before Parentheses
        // --------------------------

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

        // Spacing Around Operators
        // ------------------------

        if (Arrays.asList(CONST_DECLARATION, ENUM_ITEM, DEFAULT_PARAMETER, TABLE_ITEM, CLASS_MEMBER).contains(elementType) && (type1 == EQ || type2 == EQ)) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
        }
        else if ((type1 == EQ && elementType == VAR_INIT) || type2 == VAR_INIT) {
            return addSingleSpaceIf(cmSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS);
        }
        else if ((type1 == ASSIGNMENT_OPERATOR || type2 == ASSIGNMENT_OPERATOR) && elementType ==
                ASSIGN_EXPRESSION) {
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

        // Spacing Before Left Brace
        // -------------------------

        if ((elementType == CLASS_DECLARATION || elementType == CLASS_EXPRESSION) && type2 == CLASS_BODY) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_CLASS_LBRACE, cmSettings.CLASS_BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == ENUM_DECLARATION && type2 == LBRACE) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_CLASS_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if ((FUNCTION_DEFINITION.contains(elementType) || elementType == FUNCTION_EXPRESSION) && type2 == FUNCTION_BODY) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_METHOD_LBRACE, cmSettings.METHOD_BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == LAMBDA_FUNCTION_EXPRESSION && type1 == PARAMETERS) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_METHOD_LBRACE, cmSettings.METHOD_BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == IF_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_IF_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == IF_STATEMENT && type1 == ELSE) {
            if (type2 == IF_STATEMENT) {
                return Spacing.createSpacing(1, 1, cmSettings.SPECIAL_ELSE_IF_TREATMENT ? 0 : 1, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
            }
            return setBraceSpace(cmSettings.SPACE_BEFORE_ELSE_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == SWITCH_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_SWITCH_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == WHILE_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_WHILE_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == DO_WHILE_STATEMENT && type1 == DO) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_DO_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == FOR_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_FOR_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == FOREACH_STATEMENT && type1 == RPAREN) {
            return setBraceSpace(sqSettings.SPACE_BEFORE_FOREACH_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == TRY_STATEMENT && type1 == TRY) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_TRY_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }
        else if (elementType == CATCH_PART && type1 == RPAREN) {
            return setBraceSpace(cmSettings.SPACE_BEFORE_CATCH_LBRACE, cmSettings.BRACE_STYLE, child1.getTextRange(), node2);
        }

        // Spacing Before Keywords
        // -----------------------

        if (type2 == ELSE) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_ELSE_KEYWORD, cmSettings.ELSE_ON_NEW_LINE, SqFormatterUtil.isSimpleStatement(node1));
        }
        else if (type2 == WHILE) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_WHILE_KEYWORD, cmSettings.WHILE_ON_NEW_LINE, SqFormatterUtil.isSimpleStatement(node1));
        }
        else if (type2 == CATCH_PART) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_CATCH_KEYWORD, cmSettings.CATCH_ON_NEW_LINE, SqFormatterUtil.isSimpleStatement(node1));
        }

        // Spacing Within
        // --------------

        if (type1 == LBRACKET || type2 == RBRACKET) {
            if (type1 == LBRACKET && type2 == RBRACKET) {
                return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACKETS);
            }
            else {
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_BRACKETS);
            }
        }
        else if (type1 == CLASS_ATTR_START || type2 == CLASS_ATTR_END) {
            if (type1 == CLASS_ATTR_START && type2 == CLASS_ATTR_END) {
                return addSingleSpaceIf(sqSettings.SPACE_WITHIN_EMPTY_BRACES);
            }
            else {
                return oneSpace();
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
                    final boolean newLineNeeded = type1 == LPAREN ? cmSettings
                            .METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE : cmSettings
                            .METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE;

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
                            type1 == LPAREN ? cmSettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE : cmSettings
                                    .CALL_PARAMETERS_RPAREN_ON_NEXT_LINE;

                    return addSingleSpaceIf(cmSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, newLineNeeded);
                }
            }
            else if (elementType == PARENTHESIZED_EXPRESSION) {
                final boolean newLineNeeded =
                        type1 == LPAREN ? cmSettings.PARENTHESES_EXPRESSION_LPAREN_WRAP : cmSettings
                                .PARENTHESES_EXPRESSION_RPAREN_WRAP;
                return addSingleSpaceIf(cmSettings.SPACE_WITHIN_PARENTHESES, newLineNeeded);
            }
        }

        // Spacing In Ternary Operator
        // ---------------------------

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

        // Spacing Other
        // -------------

        // COMMAS
        if (type2 == COMMA) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_COMMA);
        }
        else if (type1 == COMMA) {
            return addSingleSpaceIf(cmSettings.SPACE_AFTER_COMMA);
        }
        // SEMICOLONS
        else if ((elementType == FOR_LOOP_PARTS || elementType == CLASS_BODY) && type2 == SEMICOLON) {
            return addSingleSpaceIf(cmSettings.SPACE_BEFORE_SEMICOLON);
        }
        else if ((elementType == FOR_LOOP_PARTS || elementType == CLASS_BODY) && type1 == SEMICOLON) {
            return addSingleSpaceIf(cmSettings.SPACE_AFTER_SEMICOLON);
        }
        // Remove whitespace around regular semicolons (except new lines)
        else if (type2 == SEMICOLON) {
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


        // Spacing Multiline
        // -----------------

        if (STATEMENTS.contains(elementType) && (parentType == SWITCH_CASE || parentType == DEFAULT_CASE)) {
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

        // @formatter:on


        return null;
    }

    public Spacing addLineBreak() {
        return Spacing.createSpacing(0, 0, 1, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
    }

    public Spacing addLineBreak(int number) {
        return Spacing.createSpacing(0, 0, number, false, cmSettings.KEEP_BLANK_LINES_IN_CODE);
    }

    public Spacing addSingleSpaceIf(boolean condition) {
        return addSingleSpaceIf(condition, false);
    }

    public Spacing addSingleSpaceIf(boolean condition, boolean linesFeed) {
        final int spaces = condition ? 1 : 0;
        final int lines = linesFeed ? 1 : 0;
        return Spacing.createSpacing(spaces, spaces, lines, cmSettings.KEEP_LINE_BREAKS, cmSettings
                .KEEP_BLANK_LINES_IN_CODE);
    }

    public Spacing addSingleSpaceIf(boolean condition, boolean linesFeed, boolean keepLineBreaks) {
        final int spaces = condition ? 1 : 0;
        final int lines = linesFeed ? 1 : 0;
        return Spacing.createSpacing(spaces, spaces, lines, keepLineBreaks, keepLineBreaks ? cmSettings
                .KEEP_BLANK_LINES_IN_CODE : 0);
    }

    public Spacing noSpace() {
        return Spacing.createSpacing(0, 0, 0, cmSettings.KEEP_LINE_BREAKS, 0);
    }

    public Spacing oneSpace() {
        return Spacing.createSpacing(1, 1, 0, cmSettings.KEEP_LINE_BREAKS, 0);
    }

    public Spacing setBraceSpace(boolean needSpaceSetting,
                                 @CommonCodeStyleSettings.BraceStyleConstant int braceStyleSetting,
                                 TextRange textRange, ASTNode child2) {
        final int spaces = needSpaceSetting ? 1 : 0;
        IElementType parentType = child2.getTreeParent().getElementType();

        // Don't wrap simple block event if line breaks set to NEXT_LINE (unless there are manual line breaks)
        if (braceStyleSetting == CommonCodeStyleSettings.NEXT_LINE && SqFormatterUtil.isSimpleBlock(child2) &&
                ((parentType == FUNCTION_BODY && cmSettings.KEEP_SIMPLE_METHODS_IN_ONE_LINE) ||
                        (parentType == CLASS_DECLARATION && cmSettings.KEEP_SIMPLE_CLASSES_IN_ONE_LINE) ||
                        (parentType != FUNCTION_BODY && parentType != CLASS_DECLARATION && cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE))
                ) {
            return Spacing.createSpacing(spaces, spaces, 0, cmSettings.KEEP_LINE_BREAKS,
                    cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }

        else if (braceStyleSetting == CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED && textRange != null) {
            return Spacing.createDependentLFSpacing(spaces, spaces, textRange, cmSettings.KEEP_LINE_BREAKS,
                    cmSettings.KEEP_BLANK_LINES_IN_CODE);
        }
        else {
            final int lineBreaks = (braceStyleSetting == CommonCodeStyleSettings.END_OF_LINE || braceStyleSetting == CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED) ? 0: 1;
            return Spacing.createSpacing(spaces, spaces, lineBreaks, false, 0);
        }
    }

    public int getBraceSettingsForElement(ASTNode element){
        if (element == null) return cmSettings.BRACE_STYLE;
        IElementType elementType = element.getElementType();
        if (elementType == FUNCTION_BODY) return cmSettings.METHOD_BRACE_STYLE;
        else if (elementType == CLASS_DECLARATION) return cmSettings.CLASS_BRACE_STYLE;
        else return cmSettings.BRACE_STYLE;
    }

    public boolean getKeepSimpleInOneLineSettingsForElement(ASTNode element){
        if (element == null) return false;
        IElementType elementType = element.getElementType();
        if (elementType == FUNCTION_BODY) return cmSettings.KEEP_SIMPLE_METHODS_IN_ONE_LINE;
        else if (elementType == CLASS_DECLARATION) return cmSettings.KEEP_SIMPLE_CLASSES_IN_ONE_LINE;
        else return cmSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE;
    }
}

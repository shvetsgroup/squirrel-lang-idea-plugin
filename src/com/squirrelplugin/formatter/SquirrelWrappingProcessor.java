package com.squirrelplugin.formatter;

import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.WrappingUtil;
import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.formatter.settings.SquirrelCodeStyleSettings;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;
import static com.squirrelplugin.SquirrelTokenTypes.*;
import static com.squirrelplugin.SquirrelTokenTypesSets.*;

public class SquirrelWrappingProcessor {
    private static final Key<Wrap> SQUIRREL_TERNARY_EXPRESSION_WRAP_KEY = Key.create("TERNARY_EXPRESSION_WRAP_KEY");
    private static final Key<Wrap> SQUIRREL_EXPRESSION_LIST_WRAP_KEY = Key.create("EXPRESSION_LIST_WRAP_KEY");
    private static final Key<Wrap> SQUIRREL_ARGUMENT_LIST_WRAP_KEY = Key.create("ARGUMENT_LIST_WRAP_KEY");
    private static final Key<Wrap> SQUIRREL_TYPE_LIST_WRAP_KEY = Key.create("TYPE_LIST_WRAP_KEY");

    static Wrap createChildWrap(ASTNode child, Wrap defaultWrap, ASTNode myNode, CommonCodeStyleSettings cmSettings, SquirrelCodeStyleSettings sqSettings) {
        final IElementType childType = child.getElementType();
        final IElementType elementType = myNode.getElementType();
        if (childType == COMMA || childType == SEMICOLON) return defaultWrap;

        if (elementType == IF_STATEMENT && childType == ELSE) {
            return createWrap(cmSettings.ELSE_ON_NEW_LINE);
        }
        if (elementType == DO_WHILE_STATEMENT && childType == WHILE) {
            return createWrap(cmSettings.WHILE_ON_NEW_LINE);
        }
        if (elementType == TRY_STATEMENT && childType == CATCH_PART) {
            return createWrap(cmSettings.CATCH_ON_NEW_LINE);
        }

        /*
        if (elementType == ENUM_DECLARATION) {
            if (childType == ENUM_ITEM && cmSettings.ENUM_CONSTANTS_WRAP != DO_NOT_WRAP) {
                if (myNode.getFirstChildNode() == child) {
                    return createWrap(sqSettings.ENUM_LBRACE_ON_NEXT_LINE);
                }
                if (childType == RBRACE) {
                    return createWrap(sqSettings.ENUM_RBRACE_ON_NEXT_LINE);
                }
                return Wrap.createWrap(WrappingUtil.getWrapType(cmSettings.ENUM_CONSTANTS_WRAP), true);
            }
        }

        if (elementType == TABLE_EXPRESSION) {
            if (childType == TABLE_ITEM && sqSettings.TABLE_WRAP != DO_NOT_WRAP) {
                if (myNode.getFirstChildNode() == child) {
                    return createWrap(sqSettings.TABLE_LBRACE_ON_NEXT_LINE);
                }
                if (childType == RBRACE) {
                    return createWrap(sqSettings.TABLE_RBRACE_ON_NEXT_LINE);
                }
                return Wrap.createWrap(WrappingUtil.getWrapType(sqSettings.TABLE_WRAP), true);
            }
        }

        if (elementType == CLASS_ATTRIBUTE) {
            if (childType == TABLE_ITEM && sqSettings.CLASS_ATTRIBUTES_WRAP != DO_NOT_WRAP) {
                if (myNode.getFirstChildNode() == child) {
                    return createWrap(sqSettings.CLASS_ATTRIBUTE_LBRACE_ON_NEXT_LINE);
                }
                if (childType == RBRACE) {
                    return createWrap(sqSettings.CLASS_ATTRIBUTE_RBRACE_ON_NEXT_LINE);
                }
                return Wrap.createWrap(WrappingUtil.getWrapType(sqSettings.CLASS_ATTRIBUTES_WRAP), true);
            }
        }*/

        if (elementType == PARAMETER_LIST) {
            if (cmSettings.METHOD_PARAMETERS_WRAP != DO_NOT_WRAP) {
                if (myNode.getFirstChildNode() == child) {
                    return createWrap(cmSettings.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE);
                }
                if (childType == RPAREN) {
                    return createWrap(cmSettings.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE);
                }
                return Wrap.createWrap(WrappingUtil.getWrapType(cmSettings.METHOD_PARAMETERS_WRAP), true);
            }
        }

        if (elementType == ARGUMENT_LIST) {
            if (cmSettings.CALL_PARAMETERS_WRAP != DO_NOT_WRAP) {
                Wrap wrap;
                // First, do persistent object management.
                if (child == myNode.getFirstChildNode()) {
                    ASTNode[] childs = myNode.getChildren(EXPRESSIONS);
                    if (childs.length >= 7) { // Approximation; SQUIRREL_style uses dynamic programming with cost-based analysis to choose.
                        wrap = Wrap.createWrap(WrapType.ALWAYS, true);
                    }
                    else {
                        wrap = Wrap.createWrap(WrapType.NORMAL, true); // NORMAL,CHOP_DOWN_IF_LONG
                    }
                    if (myNode.getLastChildNode() != child) {
                        myNode.putUserData(SQUIRREL_ARGUMENT_LIST_WRAP_KEY, wrap);
                    }
                }
                else {
                    wrap = myNode.getUserData(SQUIRREL_ARGUMENT_LIST_WRAP_KEY);

                    if (myNode.getLastChildNode() == child) {
                        myNode.putUserData(SQUIRREL_ARGUMENT_LIST_WRAP_KEY, null);
                    }
                }
                // Second, decide what object to return.
                if (childType == MULTI_LINE_COMMENT || childType == FUNCTION_EXPRESSION) {
                    return Wrap.createWrap(WrapType.NONE, false);
                }
                return wrap != null ? wrap : Wrap.createWrap(WrappingUtil.getWrapType(cmSettings.CALL_PARAMETERS_WRAP), false);
            }
        }

        return defaultWrap;
    }

    private static Wrap createWrap(boolean isNormal) {
        return Wrap.createWrap(isNormal ? WrapType.NORMAL : WrapType.NONE, true);
    }
}
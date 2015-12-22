package com.sqide.formatter;

import com.intellij.psi.codeStyle.*;
import com.sqide.SquirrelLanguage;
import com.sqide.formatter.settings.SquirrelCodeStyleSettings;
import com.intellij.psi.formatter.FormatterTestCase;
import com.sqide.formatter.settings.SquirrelLanguageCodeStyleSettingsProvider;

import java.lang.reflect.Field;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;

public abstract class AbstractSquirrelFormatterTest extends FormatterTestCase {
    public SquirrelCodeStyleSettings getSqSettings() {
        return getSettings().getCustomSettings(SquirrelCodeStyleSettings.class);
    }

    public CommonCodeStyleSettings getCmSettings() {
        return getSettings(SquirrelLanguage.INSTANCE);
    }

    protected void invertSpacingSettings() throws NoSuchFieldException, IllegalAccessException {
        CommonCodeStyleSettings cmSettings = getCmSettings();
        Class<?> cm = cmSettings.getClass();
        for (String s: SquirrelLanguageCodeStyleSettingsProvider.standardSpacingSettings) {
            Field field = cm.getDeclaredField(s);
            field.setBoolean(cmSettings, !field.getBoolean(cmSettings));
        }

        SquirrelCodeStyleSettings sqSettings = getSqSettings();
        Class<?> sq = sqSettings.getClass();
        for (String s: SquirrelLanguageCodeStyleSettingsProvider.customSpacingSettings) {
            Field field = sq.getDeclaredField(s);
            field.setBoolean(sqSettings, !field.getBoolean(sqSettings));
        }

    }

    protected void setDefaultSettings() {
        // Margin for tests.
        getCmSettings().RIGHT_MARGIN = 35;


// Before parentheses
        getCmSettings().SPACE_BEFORE_METHOD_CALL_PARENTHESES = false;
        getCmSettings().SPACE_BEFORE_METHOD_PARENTHESES = false;
        getSqSettings().SPACE_BEFORE_FUNCTION_EXPRESSION_PARENTHESES = false;
        getCmSettings().SPACE_BEFORE_IF_PARENTHESES = true;
        getCmSettings().SPACE_BEFORE_FOR_PARENTHESES = true;
        getSqSettings().SPACE_BEFORE_FOREACH_PARENTHESES = true;
        getCmSettings().SPACE_BEFORE_WHILE_PARENTHESES = true;
        getCmSettings().SPACE_BEFORE_SWITCH_PARENTHESES = true;
        getCmSettings().SPACE_BEFORE_CATCH_PARENTHESES = true;

// Around operators
        getCmSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
        getCmSettings().SPACE_AROUND_LOGICAL_OPERATORS = true;
        getCmSettings().SPACE_AROUND_EQUALITY_OPERATORS = true;
        getCmSettings().SPACE_AROUND_RELATIONAL_OPERATORS = true;
        getCmSettings().SPACE_AROUND_BITWISE_OPERATORS = true;
        getCmSettings().SPACE_AROUND_ADDITIVE_OPERATORS = true;
        getCmSettings().SPACE_AROUND_MULTIPLICATIVE_OPERATORS = true;
        getCmSettings().SPACE_AROUND_SHIFT_OPERATORS = true;
        getCmSettings().SPACE_AROUND_UNARY_OPERATOR = false;

// Before left brace
        getCmSettings().SPACE_BEFORE_CLASS_LBRACE = true;
        getCmSettings().SPACE_BEFORE_METHOD_LBRACE = true;
        getCmSettings().SPACE_BEFORE_IF_LBRACE = true;
        getCmSettings().SPACE_BEFORE_ELSE_LBRACE = true;
        getCmSettings().SPACE_BEFORE_FOR_LBRACE = true;
        getSqSettings().SPACE_BEFORE_FOREACH_LBRACE = true;
        getCmSettings().SPACE_BEFORE_WHILE_LBRACE = true;
        getCmSettings().SPACE_BEFORE_DO_LBRACE = true;
        getCmSettings().SPACE_BEFORE_SWITCH_LBRACE = true;
        getCmSettings().SPACE_BEFORE_TRY_LBRACE = true;
        getCmSettings().SPACE_BEFORE_CATCH_LBRACE = true;

// Before keywords
        getCmSettings().SPACE_BEFORE_ELSE_KEYWORD = true;
        getCmSettings().SPACE_BEFORE_WHILE_KEYWORD = true;
        getCmSettings().SPACE_BEFORE_CATCH_KEYWORD = true;

// Within
        getCmSettings().SPACE_WITHIN_BRACES = false; // In case of braces in one line
        getSqSettings().SPACE_WITHIN_EMPTY_BRACES = false;
        getCmSettings().SPACE_WITHIN_BRACKETS = false;
        getSqSettings().SPACE_WITHIN_EMPTY_BRACKETS = false;
        getCmSettings().SPACE_WITHIN_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_METHOD_CALL_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_METHOD_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_IF_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_FOR_PARENTHESES = false;
        getSqSettings().SPACE_WITHIN_FOREACH_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_WHILE_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_SWITCH_PARENTHESES = false;
        getCmSettings().SPACE_WITHIN_CATCH_PARENTHESES = false;

// Ternary
        getCmSettings().SPACE_BEFORE_QUEST = true;
        getCmSettings().SPACE_AFTER_QUEST = true;
        getCmSettings().SPACE_BEFORE_COLON = true;
        getCmSettings().SPACE_AFTER_COLON = true;

// Other
        getCmSettings().SPACE_BEFORE_COMMA = false; // arrays, parameters, arguments
        getCmSettings().SPACE_AFTER_COMMA = true;
        getCmSettings().SPACE_BEFORE_SEMICOLON = false; // for loop
        getCmSettings().SPACE_AFTER_SEMICOLON = true;


// Wrapping -------------------


        getCmSettings().BRACE_STYLE = END_OF_LINE;
        getCmSettings().CLASS_BRACE_STYLE = END_OF_LINE;
        getCmSettings().METHOD_BRACE_STYLE = END_OF_LINE;

        getCmSettings().ELSE_ON_NEW_LINE = false;
        getCmSettings().WHILE_ON_NEW_LINE = false;
        getCmSettings().CATCH_ON_NEW_LINE = false;
    }
}
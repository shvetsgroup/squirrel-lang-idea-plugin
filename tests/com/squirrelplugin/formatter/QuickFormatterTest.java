package com.squirrelplugin.formatter;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.codeStyle.*;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.squirrelplugin.SquirrelFileType;
import com.squirrelplugin.SquirrelLanguage;
import com.squirrelplugin.formatter.settings.SquirrelCodeStyleSettings;
import com.intellij.psi.formatter.FormatterTestCase;
import com.squirrelplugin.formatter.settings.SquirrelLanguageCodeStyleSettingsProvider;

import java.lang.reflect.Field;

public class QuickFormatterTest extends FormatterTestCase {
    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData";
    }
    protected String getBasePath() {
        return "formatter";
    }
    protected String getFileExtension() { return "nut"; }

    public void testFormatterDefaultSettings() throws Exception {
        defaultSettings();
        doTest("test", "test_after");
    }

    public void testFormatterCustomSettings() throws Exception {
        invertSettings();
        doTest("test_after", "test");
    }

    public SquirrelCodeStyleSettings getSqSettings() {
        return getSettings().getCustomSettings(SquirrelCodeStyleSettings.class);
    }

    public CommonCodeStyleSettings getCmSettings() {
        return getSettings(SquirrelLanguage.INSTANCE);
    }

    protected void invertSettings() throws NoSuchFieldException, IllegalAccessException {
        defaultSettings();

        CommonCodeStyleSettings cmSettings = getCmSettings();
        Class<?> cm = getCmSettings().getClass();
        for (String s: SquirrelLanguageCodeStyleSettingsProvider.standardSpacingSettings) {
            Field field = cm.getDeclaredField(s);
            field.setBoolean(cmSettings, !field.getBoolean(cmSettings));
        }

        SquirrelCodeStyleSettings sqSettings = getSqSettings();
        Class<?> sq = getCmSettings().getClass();
        for (String s: SquirrelLanguageCodeStyleSettingsProvider.customSpacingSettings) {
            Field field = sq.getDeclaredField(s);
            field.setBoolean(sqSettings, !field.getBoolean(sqSettings));
        }

    }

    protected void defaultSettings() {
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
        getCmSettings().SPACE_BEFORE_CATCH_LBRACE = true;

// Before keywords
        getCmSettings().SPACE_BEFORE_ELSE_KEYWORD = true;
        getCmSettings().SPACE_BEFORE_WHILE_KEYWORD = true;
        getCmSettings().SPACE_BEFORE_CATCH_KEYWORD = true;

// Within
        getCmSettings().SPACE_WITHIN_BRACES = false; // In case of braces in one line
        getCmSettings().SPACE_WITHIN_BRACKETS = false;
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

    }
}

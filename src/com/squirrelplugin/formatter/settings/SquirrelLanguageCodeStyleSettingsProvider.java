package com.squirrelplugin.formatter.settings;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.squirrelplugin.SquirrelLanguage;
import org.jetbrains.annotations.NotNull;

public class SquirrelLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    public static String standardSpacingSettings[] = {
            // Before parentheses
            "SPACE_BEFORE_METHOD_CALL_PARENTHESES",
            "SPACE_BEFORE_METHOD_PARENTHESES",
            "SPACE_BEFORE_IF_PARENTHESES",
            "SPACE_BEFORE_FOR_PARENTHESES",
            "SPACE_BEFORE_WHILE_PARENTHESES",
            "SPACE_BEFORE_SWITCH_PARENTHESES",
            "SPACE_BEFORE_CATCH_PARENTHESES",

            // Around operators
            "SPACE_AROUND_ASSIGNMENT_OPERATORS",
            "SPACE_AROUND_LOGICAL_OPERATORS",
            "SPACE_AROUND_EQUALITY_OPERATORS",
            "SPACE_AROUND_RELATIONAL_OPERATORS",
            "SPACE_AROUND_BITWISE_OPERATORS",
            "SPACE_AROUND_ADDITIVE_OPERATORS",
            "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
            "SPACE_AROUND_SHIFT_OPERATORS",
            "SPACE_AROUND_UNARY_OPERATOR",

            // Before left brace
            "SPACE_BEFORE_CLASS_LBRACE",
            "SPACE_BEFORE_METHOD_LBRACE",
            "SPACE_BEFORE_IF_LBRACE",
            "SPACE_BEFORE_ELSE_LBRACE",
            "SPACE_BEFORE_FOR_LBRACE",
            "SPACE_BEFORE_WHILE_LBRACE",
            "SPACE_BEFORE_DO_LBRACE",
            "SPACE_BEFORE_SWITCH_LBRACE",
            "SPACE_BEFORE_TRY_LBRACE",
            "SPACE_BEFORE_CATCH_LBRACE",

            // Before keywords
            "SPACE_BEFORE_ELSE_KEYWORD",
            "SPACE_BEFORE_WHILE_KEYWORD",
            "SPACE_BEFORE_CATCH_KEYWORD",

            // Within
            "SPACE_WITHIN_BRACES", // In case of braces in one line
            "SPACE_WITHIN_BRACKETS",
            "SPACE_WITHIN_PARENTHESES",
            "SPACE_WITHIN_METHOD_CALL_PARENTHESES",
            "SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES",
            "SPACE_WITHIN_METHOD_PARENTHESES",
            "SPACE_WITHIN_EMPTY_METHOD_PARENTHESES",
            "SPACE_WITHIN_IF_PARENTHESES",
            "SPACE_WITHIN_FOR_PARENTHESES",
            "SPACE_WITHIN_WHILE_PARENTHESES",
            "SPACE_WITHIN_SWITCH_PARENTHESES",
            "SPACE_WITHIN_CATCH_PARENTHESES",

            // Ternary
            "SPACE_BEFORE_QUEST",
            "SPACE_AFTER_QUEST",
            "SPACE_BEFORE_COLON",
            "SPACE_AFTER_COLON",

            // Other
            "SPACE_BEFORE_COMMA", // arrays, parameters, arguments
            "SPACE_AFTER_COMMA",
            "SPACE_BEFORE_SEMICOLON", // for loop
            "SPACE_AFTER_SEMICOLON"
    };

    public static String customSpacingSettings[] = {
            "SPACE_BEFORE_FUNCTION_EXPRESSION_PARENTHESES",
            "SPACE_BEFORE_FOREACH_PARENTHESES",
            "SPACE_BEFORE_FOREACH_LBRACE",
            "SPACE_WITHIN_FOREACH_PARENTHESES",
            "SPACE_WITHIN_EMPTY_BRACES",
            "SPACE_WITHIN_EMPTY_BRACKETS"
    };

    public static String standardBlankLinesSettings[] = {
            "KEEP_BLANK_LINES_IN_CODE"
    };

    public static String standardWrappingAndBracesSettings[] = {
            "RIGHT_MARGIN",
            "WRAP_ON_TYPING",

            "KEEP_LINE_BREAKS",
            "KEEP_FIRST_COLUMN_COMMENT",  // todo
////"KEEP_CONTROL_STATEMENT_IN_ONE_LINE",
            "KEEP_SIMPLE_BLOCKS_IN_ONE_LINE",
            "KEEP_SIMPLE_METHODS_IN_ONE_LINE",
            "KEEP_SIMPLE_CLASSES_IN_ONE_LINE",

            "CLASS_BRACE_STYLE",
            "METHOD_BRACE_STYLE",
            "BRACE_STYLE",

            "METHOD_PARAMETERS_WRAP",
            "ALIGN_MULTILINE_PARAMETERS",  // todo
            "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
            "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE",

            "CALL_PARAMETERS_WRAP",
            "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",  // todo
            "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE",
            "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE",

            "ELSE_ON_NEW_LINE",
            "SPECIAL_ELSE_IF_TREATMENT",

            "WHILE_ON_NEW_LINE",

            "CATCH_ON_NEW_LINE",

//            "BINARY_OPERATION_WRAP",  // todo
//            "ALIGN_MULTILINE_BINARY_OPERATION",  // todo
//            "BINARY_OPERATION_SIGN_ON_NEXT_LINE",  // todo
//            "PARENTHESES_EXPRESSION_LPAREN_WRAP",  // todo
//            "PARENTHESES_EXPRESSION_RPAREN_WRAP",  // todo

//            "ASSIGNMENT_WRAP",  // todo
//            "ALIGN_MULTILINE_ASSIGNMENT", // todo
//            "PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE",  // todo

//            "TERNARY_OPERATION_WRAP",  // todo
//            "ALIGN_MULTILINE_TERNARY_OPERATION",  // todo
//            "TERNARY_OPERATION_SIGNS_ON_NEXT_LINE",  // todo
//
//            "ARRAY_INITIALIZER_WRAP", // todo
//            "ALIGN_MULTILINE_ARRAY_INITIALIZER_EXPRESSION", // todo
//            "ARRAY_INITIALIZER_LBRACE_ON_NEXT_LINE", // todo
//            "ARRAY_INITIALIZER_RBRACE_ON_NEXT_LINE", // todo
//
//            "ENUM_CONSTANTS_WRAP", // todo
    };

    public static String customWrappingSettings[] = {
//            "CLASS_MEMBERS_WRAP",
//            "ENUM_WRAP",
//            "TABLE_WRAP",
//            "ENUM_LBRACE_ON_NEXT_LINE",
//            "ENUM_RBRACE_ON_NEXT_LINE",
//            "TABLE_LBRACE_ON_NEXT_LINE",
//            "TABLE_RBRACE_ON_NEXT_LINE",
//            "CLASS_ATTRIBUTE_LBRACE_ON_NEXT_LINE",
//            "CLASS_ATTRIBUTE_RBRACE_ON_NEXT_LINE",
    };



    @NotNull
    @Override
    public Language getLanguage() {
        return SquirrelLanguage.INSTANCE;
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            return SPACING_CODE_SAMPLE;
        }
        if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            return WRAPPING_CODE_SAMPLE;
        }
        if (settingsType == SettingsType.INDENT_SETTINGS) {
            return INDENT_CODE_SAMPLE;
        }
        return BLANK_LINES_CODE_SAMPLE;
    }

    @Override
    public IndentOptionsEditor getIndentOptionsEditor() {
        return new SmartIndentOptionsEditor();
    }

    @Override
    public CommonCodeStyleSettings getDefaultCommonSettings() {
        CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(getLanguage());
        CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
        indentOptions.CONTINUATION_INDENT_SIZE = 4;

        return defaultSettings;
    }

    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions(standardSpacingSettings);
            consumer.showCustomOption(
                    SquirrelCodeStyleSettings.class,
                    "SPACE_BEFORE_FUNCTION_EXPRESSION_PARENTHESES",
                    "Function expression",
                    CodeStyleSettingsCustomizable.SPACES_BEFORE_PARENTHESES,
                    CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
                    "SPACE_BEFORE_METHOD_PARENTHESES"
            );
            consumer.showCustomOption(
                    SquirrelCodeStyleSettings.class,
                    "SPACE_BEFORE_FOREACH_PARENTHESES",
                    "'foreach' parentheses",
                    CodeStyleSettingsCustomizable.SPACES_BEFORE_PARENTHESES,
                    CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
                    "SPACE_BEFORE_FOR_PARENTHESES"
            );
            consumer.showCustomOption(
                    SquirrelCodeStyleSettings.class,
                    "SPACE_BEFORE_FOREACH_LBRACE",
                    "'foreach' left brace",
                    CodeStyleSettingsCustomizable.SPACES_BEFORE_LEFT_BRACE,
                    CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
                    "SPACE_BEFORE_FOR_LBRACE"
            );
            consumer.showCustomOption(
                    SquirrelCodeStyleSettings.class,
                    "SPACE_WITHIN_FOREACH_PARENTHESES",
                    "'foreach' parentheses",
                    CodeStyleSettingsCustomizable.SPACES_WITHIN,
                    CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
                    "SPACE_WITHIN_FOR_PARENTHESES"
            );
            consumer.showCustomOption(
                    SquirrelCodeStyleSettings.class,
                    "SPACE_WITHIN_EMPTY_BRACES",
                    "Empty code braces",
                    CodeStyleSettingsCustomizable.SPACES_WITHIN,
                    CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
                    "SPACE_WITHIN_BRACES"
            );
            consumer.showCustomOption(
                    SquirrelCodeStyleSettings.class,
                    "SPACE_WITHIN_EMPTY_BRACKETS",
                    "Empty brackets",
                    CodeStyleSettingsCustomizable.SPACES_WITHIN,
                    CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
                    "SPACE_WITHIN_BRACKETS"
            );
            consumer.renameStandardOption("SPACE_BEFORE_METHOD_CALL_PARENTHESES", "Function call parentheses");
            consumer.renameStandardOption("SPACE_BEFORE_METHOD_PARENTHESES", "Function declaration parentheses");
            consumer.renameStandardOption("SPACE_BEFORE_METHOD_LBRACE", "Function left brace");
            consumer.renameStandardOption("SPACE_WITHIN_METHOD_CALL_PARENTHESES", "Function call parentheses");
            consumer.renameStandardOption("SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES", "Empty function call parentheses");
            consumer.renameStandardOption("SPACE_WITHIN_METHOD_PARENTHESES", "Function declaration parentheses");
            consumer.renameStandardOption("SPACE_WITHIN_EMPTY_METHOD_PARENTHESES", "Empty function declaration parentheses");
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions(standardBlankLinesSettings);
        } else if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showStandardOptions(standardWrappingAndBracesSettings);

//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "ENUM_WRAP",
//                    "Enums",
//                    null, CodeStyleSettingsCustomizable.WRAP_OPTIONS, CodeStyleSettingsCustomizable.WRAP_VALUES
//            );
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "ENUM_LBRACE_ON_NEXT_LINE",
//                    "New line after '{'",
//                    "Enums"
//            );
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "ENUM_RBRACE_ON_NEXT_LINE",
//                    "Place '}' on new line",
//                    "Enums"
//            );
//
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "TABLE_WRAP",
//                    "Tables",
//                    null, CodeStyleSettingsCustomizable.WRAP_OPTIONS, CodeStyleSettingsCustomizable.WRAP_VALUES
//            );
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "TABLE_LBRACE_ON_NEXT_LINE",
//                    "New line after '{'",
//                    "Tables"
//            );
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "TABLE_RBRACE_ON_NEXT_LINE",
//                    "Place '}' on new line",
//                    "Tables"
//            );
//
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "CLASS_ATTRIBUTES_WRAP",
//                    "Class attributes",
//                    "Classes", CodeStyleSettingsCustomizable.WRAP_OPTIONS, CodeStyleSettingsCustomizable.WRAP_VALUES
//            );
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "CLASS_ATTRIBUTE_LBRACE_ON_NEXT_LINE",
//                    "New line after '</'",
//                    "Classes"
//            );
//            consumer.showCustomOption(
//                    SquirrelCodeStyleSettings.class,
//                    "CLASS_ATTRIBUTE_RBRACE_ON_NEXT_LINE",
//                    "Place '/>' on new line",
//                    "Classes"
//            );
        }
    }

    public static final String SPACING_CODE_SAMPLE = "class Foo {\n" +
            "    tmp = [];\n" +
            "    static function foo(x, z) {\n" +
            "        local a = Foo(x, 2);\n" +
            "        function absSum(a, b) {\n" +
            "            local value = a + b;\n" +
            "            return value > 0 ? value : -value;\n" +
            "        }\n" +
            "        local arr = [\"zero\", \"one\"];\n" +
            "        local y = (x ^ 0x123) << 2;\n" +
            "        foreach (ind,i in tmp) {\n" +
            "            y = (y ^ 0x123) << 2;\n" +
            "        }\n" +
            "        local k = x % 2 == 1 ? 0 : 1;\n" +
            "        do {\n" +
            "            try {\n" +
            "                if (0 < x && x < 10) {\n" +
            "                    while (x != y) {\n" +
            "                        x = absSum(x * 3, 5);\n" +
            "                    }\n" +
            "                    z += 2;\n" +
            "                } else if (x > 20) {\n" +
            "                    z = x << 1;\n" +
            "                } else {\n" +
            "                    z = x | 2;\n" +
            "                }\n" +
            "                switch (k) {\n" +
            "                    case 0:\n" +
            "                        local s1 = \"zero\";\n" +
            "                        break;\n" +
            "                    case 2:\n" +
            "                        local s1 = \"two\";\n" +
            "                        break;\n" +
            "                    default:\n" +
            "                        local s1 = \"other\";\n" +
            "                }\n" +
            "            } catch (e) {\n" +
            "                local message = arr[0];\n" +
            "            }\n" +
            "        } while (x < 0);\n" +
            "    }\n" +
            "\n" +
            "    constructor(n, m) {\n" +
            "        tmp = [];\n" +
            "        for (local i; i < 10; ++i) tmp.push([]);\n" +
            "    }\n" +
            "}";

    public static final String WRAPPING_CODE_SAMPLE = "enum f { a = 2, b = 2, c = 2, d, e, f = 2, g = 2, h = 2, i = \"32\" }\n" +
            "local s = { a = 2, b = 2, c = 2, f = 2, g = 2, h = 2, i = \"32\" }\n" +
            "class zero { </ a = 2/>a = 2;\n" +
            "    </ a = 2, b = 2, c = \"los angeles\", s = 2, l = 2 />\n" +
            "    b = 2; c = function () { k = 2; k = 3 }; g = 2; function s() {} }\n" +
            "class Foo {\n" +
            "    // function fBar (x,y);\n" +
            "    function fOne(argA, argB, argC, argD, argE, argF, argG = 30, argH = 40) {\n" +
            "        local numbers = [\"one\", \"two\", \"three\", \"four\", \"five\", \"six\"];\n" +
            "        local\n" +
            "                x = (\"\" + argA) + argB + argC + argD + argE + argF + argG + argH;\n" +
            "        try {\n" +
            "            this.fTwo(argA, argB, argC, this.fThree(\"\", argE, argF, argG, argH));\n" +
            "        } catch (ignored) {}\n" +
            "        local z = argA == \"Some string\" ? \"yes\" : \"no\";\n" +
            "        local colors = [\"red\", \"green\", \"blue\", \"black\", \"white\", \"gray\"];\n" +
            "        foreach (idx, colorIndex in colors) {\n" +
            "            local colorString = numbers[colorIndex];\n" +
            "        }\n" +
            "        do {\n" +
            "            colors.removeLast();\n" +
            "        } while (colors.length > 0);\n" +
            "    }\n" +
            "\n" +
            "    </ a = 2, b = 2, c = 2, f = 2, g = 2, h = 2, i = \"32\" />\n" +
            "    function fTwo(strA, strB, strC, strD, ...) {\n" +
            "        if (true)\n" +
            "        return strC;\n" +
            "        if (strA == \"one\" ||\n" +
            "        strB == \"two\") {\n" +
            "            return strA + strB;\n" +
            "        } else if (true) return strD;\n" +
            "        else return strB;\n" +
            "        throw strD;\n" +
            "    }\n" +
            "\n" +
            "    function fThree(strA, strB, strC, strD, strE) {\n" +
            "        if (strA) { strC = \"23\"}\n" +
            "        if (strD) { strB = \"23\";\n" +
            "            strC = \"23\";\n" +
            "        }\n" +
            "        return strA + strB + strC + strD + strE;\n" +
            "    }\n" +
            "}";

    public static final String BLANK_LINES_CODE_SAMPLE = "class Foo {\n" +
            "    constructor() {\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "\n" +
            "    function main() {\n" +
            "        ::print(\"Hello!\");\n" +
            "    }\n" +
            "}";
    public static final String INDENT_CODE_SAMPLE = "function test(){\n" +
            "  local i = 4 + ::getTotal() +\n" +
            "  34 - 4 * 32\n" +
            "  ::print(i);\n" +
            "}";
}

package com.sqide.formatter;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;

public class WrappingFormatterTest extends AbstractSquirrelFormatterTest {
    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData";
    }

    protected String getBasePath() {
        return "formatter";
    }

    protected String getFileExtension() {
        return "nut";
    }

    public void testIfElseOnNewLine() throws Exception {
        setDefaultSettings();
        getCmSettings().ELSE_ON_NEW_LINE = true;
        doTextTest("if (true) {\n    ::print(1)\n} else {}",
                "if (true) {\n    ::print(1)\n}\nelse {}");
    }

    public void testIfElseNotOnNewLine() throws Exception {
        setDefaultSettings();
        getCmSettings().ELSE_ON_NEW_LINE = false;

        doTextTest("if (true) {\n    ::print(1)\n}\nelse {}",
                "if (true) {\n    ::print(1)\n} else {}");
    }

    public void testIfElseNotOnNewLineSimple() throws Exception {
        setDefaultSettings();
        getCmSettings().ELSE_ON_NEW_LINE = false;

        doTextTest("if (true) ::print(1);\nelse {}",
                "if (true) ::print(1);\nelse {}");
    }

    public void testIfElseSpecialTreatmentOn() throws Exception {
        setDefaultSettings();
        getCmSettings().SPECIAL_ELSE_IF_TREATMENT = true;
        doTextTest("if (true) return;\nelse if (true) return;\nelse return;",
                "if (true) return;\n" +
                        "else if (true) return;\n" +
                        "else return;");
    }

    public void testIfElseSpecialTreatmentOff() throws Exception {
        setDefaultSettings();
        getCmSettings().SPECIAL_ELSE_IF_TREATMENT = false;
        doTextTest("if (true) ;\nelse if (true) return;\nelse return;",
                "if (true) ;\n" +
                        "else\n" +
                        "    if (true) return;\n" +
                        "    else return;");
    }


    public void testDoWhileOnNewLine() throws Exception {
        setDefaultSettings();
        getCmSettings().WHILE_ON_NEW_LINE = true;
        doTextTest("do {\n    ::print(1)\n} while (true)",
                "do {\n    ::print(1)\n}\nwhile (true)");
    }

    public void testDoWhileNotOnNewLine() throws Exception {
        setDefaultSettings();
        getCmSettings().WHILE_ON_NEW_LINE = false;

        doTextTest("do {\n    ::print(1)\n}\nwhile (true)",
                "do {\n    ::print(1)\n} while (true)");
    }

    public void testCatchOnNewLine() throws Exception {
        setDefaultSettings();
        getCmSettings().CATCH_ON_NEW_LINE = true;
        doTextTest("try {\n    ::print(1)\n} catch (a) {}",
                "try {\n    ::print(1)\n}\ncatch (a) {}");
    }

    public void testCatchNotOnNewLine() throws Exception {
        setDefaultSettings();
        getCmSettings().CATCH_ON_NEW_LINE = false;

        doTextTest("try {\n    ::print(1)\n}\ncatch (a) {}",
                "try {\n    ::print(1)\n} catch (a) {}");
    }

    public void testCatchNotOnNewLineSimple() throws Exception {
        setDefaultSettings();
        getCmSettings().CATCH_ON_NEW_LINE = false;

        doTextTest("try ::print(1)\ncatch (a) {}",
                "try ::print(1)\ncatch (a) {}");
    }

    public void testBraceStyleDefault() throws Exception {
        setDefaultSettings();

        doTest("wrapping", "wrapping_default");
    }

    public void testBraceStyleNewLine() throws Exception {
        setDefaultSettings();

        getCmSettings().BRACE_STYLE = NEXT_LINE;
        getCmSettings().CLASS_BRACE_STYLE = NEXT_LINE;
        getCmSettings().METHOD_BRACE_STYLE = NEXT_LINE;

        doTest("wrapping", "wrapping_new_line");
    }

    public void testBraceStyleDefaultSimpleOnSameLine() throws Exception {
        setDefaultSettings();

        getCmSettings().KEEP_SIMPLE_BLOCKS_IN_ONE_LINE = true;
        getCmSettings().KEEP_SIMPLE_CLASSES_IN_ONE_LINE = true;
        getCmSettings().KEEP_SIMPLE_METHODS_IN_ONE_LINE = true;

        doTest("wrapping", "wrapping_simple_on_same_line");
    }
}

package com.squirrelplugin.formatter;

import com.intellij.psi.codeStyle.CommonCodeStyleSettings;

public class WrappingFormatterTest extends AbstractSquirrelFormatterTest {
    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData";
    }
    protected String getBasePath() {
        return "formatter";
    }
    protected String getFileExtension() { return "nut"; }

    public void testBraceStyleDefault() throws Exception {
        setDefaultSettings();
        doTest("wrapping", "wrapping_default");
    }

    public void testBraceStyleNewLine() throws Exception {
        setDefaultSettings();

        getCmSettings().BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE;
        getCmSettings().CLASS_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE;
        getCmSettings().METHOD_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE;

        doTest("wrapping", "wrapping_new_line");
    }
}

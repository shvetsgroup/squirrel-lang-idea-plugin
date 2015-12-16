package com.squirrelplugin.formatter;

import com.intellij.psi.formatter.FormatterTestCase;

public class IndentFormatterTest extends FormatterTestCase {
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
        doTest("indent", "indent_default");
    }
}

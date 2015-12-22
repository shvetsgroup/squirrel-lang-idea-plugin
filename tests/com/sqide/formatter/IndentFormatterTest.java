package com.sqide.formatter;

public class IndentFormatterTest extends AbstractSquirrelFormatterTest {
    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData";
    }
    protected String getBasePath() {
        return "formatter";
    }
    protected String getFileExtension() { return "nut"; }

    public void testFormatterDefaultSettings() throws Exception {
        setDefaultSettings();
        doTest("indent", "indent_default");
    }
}

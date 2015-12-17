package com.squirrelplugin.formatter;

public class SpacingFormatterTest extends AbstractSquirrelFormatterTest {
    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData";
    }
    protected String getBasePath() {
        return "formatter";
    }
    protected String getFileExtension() { return "nut"; }

    public void testDefaultSettings() throws Exception {
        setDefaultSettings();
        doTest("spacing", "spacing_default");
    }

    public void testInverseSettings() throws Exception {
        setDefaultSettings();
        invertSpacingSettings();
        doTest("spacing_default", "spacing_inverse");
    }
}

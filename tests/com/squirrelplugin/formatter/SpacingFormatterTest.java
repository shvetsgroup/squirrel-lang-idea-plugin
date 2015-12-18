package com.squirrelplugin.formatter;

import com.intellij.psi.codeStyle.CommonCodeStyleSettings;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;

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
        getCmSettings().KEEP_SIMPLE_BLOCKS_IN_ONE_LINE = true;
        getCmSettings().KEEP_SIMPLE_CLASSES_IN_ONE_LINE = true;
        getCmSettings().KEEP_SIMPLE_METHODS_IN_ONE_LINE = true;

        getCmSettings().ARRAY_INITIALIZER_WRAP = DO_NOT_WRAP;
        getCmSettings().METHOD_PARAMETERS_WRAP = DO_NOT_WRAP;
        getCmSettings().CALL_PARAMETERS_WRAP = DO_NOT_WRAP;
//        getSqSettings().CLASS_ATTRIBUTES_WRAP = DO_NOT_WRAP;
//        getSqSettings().TABLE_WRAP = DO_NOT_WRAP;
//        getSqSettings().ENUM_WRAP = DO_NOT_WRAP;
//        getSqSettings().ENUM_RBRACE_ON_NEXT_LINE = false;
        doTest("spacing", "spacing_default");
    }

    public void testInverseSettings() throws Exception {
        setDefaultSettings();
        invertSpacingSettings();

        getCmSettings().KEEP_SIMPLE_BLOCKS_IN_ONE_LINE = true;
        getCmSettings().KEEP_SIMPLE_CLASSES_IN_ONE_LINE = true;
        getCmSettings().KEEP_SIMPLE_METHODS_IN_ONE_LINE = true;

        getCmSettings().ARRAY_INITIALIZER_WRAP = DO_NOT_WRAP;
        getCmSettings().METHOD_PARAMETERS_WRAP = DO_NOT_WRAP;
        getCmSettings().CALL_PARAMETERS_WRAP = DO_NOT_WRAP;

        doTest("spacing_default", "spacing_inverse");
    }
}

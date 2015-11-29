package com.squirrelplugin;

import com.intellij.testFramework.ParsingTestCase;

public class SquirrelParsingTest extends ParsingTestCase {
    public SquirrelParsingTest() {
        super("", "nut", new SquirrelParserDefinition());
    }

    public void testParsingTestData() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "../Squirrel/testData";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}

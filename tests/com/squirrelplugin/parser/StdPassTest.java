package com.squirrelplugin.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.squirrelplugin.SquirrelParserDefinition;

public class StdPassTest extends ParsingTestCase {
    public StdPassTest() {
        super("parser/std_pass", "nut", new SquirrelParserDefinition());
    }

    public void testClass() {
        doTest(true);
    }
    public void testConst() {
        doTest(true);
    }
    public void testEnum() {
        doTest(true);
    }
    public void testFunctions() {
        doTest(true);
    }
    public void testIfs() {
        doTest(true);
    }
    public void testKeywords() {
        doTest(true);
    }
    public void testLocal() {
        doTest(true);
    }
    public void testSwitch() {
        doTest(true);
    }
    public void testTables() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData";
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

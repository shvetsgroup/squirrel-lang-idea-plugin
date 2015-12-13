package com.squirrelplugin.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.squirrelplugin.SquirrelParserDefinition;

public class SamplesTest extends ParsingTestCase {
    public SamplesTest() {
        super("parser/samples", "nut", true, new SquirrelParserDefinition());
    }

    public void testAckermann() {
        doTest(true);
    }
    public void testArray() {
        doTest(true);
    }
    public void testClass() {
        doTest(true);
    }
    public void testClassattributes() {
        doTest(true);
    }
    public void testCoroutines() {
        doTest(true);
    }
    public void testDelegation() {
        doTest(true);
    }
    public void testFibonacci() {
        doTest(true);
    }
    public void testFlow() {
        doTest(true);
    }
    public void testGenerators() {
        doTest(true);
    }
    public void testList() {
        doTest(true);
    }
    public void testLoops() {
        doTest(true);
    }
    public void testMatrix() {
        doTest(true);
    }
    public void testMetamethods() {
        doTest(true);
    }
    public void testMethcall() {
        doTest(true);
    }
    public void testTailstate() {
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

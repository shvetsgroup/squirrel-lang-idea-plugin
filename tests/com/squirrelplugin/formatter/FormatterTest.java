package com.squirrelplugin.formatter;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class FormatterTest extends LightCodeInsightFixtureTestCase {
    public void testFormatter() {
        myFixture.configureByFiles("test.nut");
        CodeStyleSettingsManager.getSettings(getProject()).SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
        new WriteCommandAction.Simple(getProject()) {
            @Override
            protected void run() throws Throwable {
                CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
            }
        }.execute();
        myFixture.checkResultByFile("test_result.nut");
    }

    @Override
    protected String getTestDataPath() {
        return "../squirrel-lang-idea-plugin/testData/formatter";
    }
}

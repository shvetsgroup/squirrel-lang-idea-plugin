package com.squirrelplugin.formatter.settings;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

public class SquirrelCodeStyleSettings extends CustomCodeStyleSettings {

    public SquirrelCodeStyleSettings(CodeStyleSettings container) {
        super("SquirrelCodeStyleSettings", container);
    }
    public boolean SPACE_BEFORE_FUNCTION_EXPRESSION_PARENTHESES = false;
    public boolean SPACE_BEFORE_FOREACH_PARENTHESES = true;
    public boolean SPACE_BEFORE_FOREACH_LBRACE = true;
    public boolean SPACE_WITHIN_FOREACH_PARENTHESES = false;
    public boolean SPACE_WITHIN_EMPTY_BRACES = false;
    public boolean SPACE_WITHIN_EMPTY_BRACKETS = false;
}

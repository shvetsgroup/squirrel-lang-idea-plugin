package com.squirrelplugin.formatter.settings;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;

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

//    public int CLASS_ATTRIBUTES_WRAP = DO_NOT_WRAP;
//    public int ENUM_WRAP = WRAP_ALWAYS;
//    public int TABLE_WRAP = DO_NOT_WRAP;
//
//    public boolean ENUM_LBRACE_ON_NEXT_LINE = false;
//    public boolean ENUM_RBRACE_ON_NEXT_LINE = true;
//
//    public boolean TABLE_LBRACE_ON_NEXT_LINE = false;
//    public boolean TABLE_RBRACE_ON_NEXT_LINE = false;
//
//    public boolean CLASS_ATTRIBUTE_LBRACE_ON_NEXT_LINE = false;
//    public boolean CLASS_ATTRIBUTE_RBRACE_ON_NEXT_LINE = false;
}

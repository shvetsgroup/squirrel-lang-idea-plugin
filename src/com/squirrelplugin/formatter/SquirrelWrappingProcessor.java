package com.squirrelplugin.formatter;

import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.squirrelplugin.formatter.settings.SquirrelCodeStyleSettings;

public class SquirrelWrappingProcessor {
    static Wrap createChildWrap(ASTNode child, Wrap defaultWrap, ASTNode myNode, CommonCodeStyleSettings cmSettings, SquirrelCodeStyleSettings sqSettings) {
        return defaultWrap;
    }
}
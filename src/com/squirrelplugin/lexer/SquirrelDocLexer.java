package com.squirrelplugin.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.TokenSet;

import static com.squirrelplugin.SquirrelTokenTypesSets.MULTI_LINE_COMMENT_BODY;

public class SquirrelDocLexer extends MergingLexerAdapter {

    public SquirrelDocLexer() {
        super(new FlexAdapter(new _SquirrelDocLexer()), TokenSet.create(MULTI_LINE_COMMENT_BODY));
    }
}
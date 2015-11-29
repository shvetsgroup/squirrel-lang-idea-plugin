package com.squirrelplugin;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class SquirrelLexerAdapter extends FlexAdapter {
    public SquirrelLexerAdapter() {
        super(new _SquirrelLexer((Reader) null));
    }
}

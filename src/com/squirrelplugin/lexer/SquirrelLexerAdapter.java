package com.squirrelplugin.lexer;

import com.intellij.lexer.FlexAdapter;
import com.squirrelplugin._SquirrelLexer;

import java.io.Reader;

public class SquirrelLexerAdapter extends FlexAdapter {
    public SquirrelLexerAdapter() {
        super(new _SquirrelLexer((Reader) null));
    }
}

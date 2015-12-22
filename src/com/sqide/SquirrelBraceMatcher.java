package com.sqide;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] BRACE_PAIRS = {
            new BracePair(SquirrelTokenTypes.LBRACE, SquirrelTokenTypes.RBRACE, true),
            new BracePair(SquirrelTokenTypes.LBRACKET, SquirrelTokenTypes.RBRACKET, false),
            new BracePair(SquirrelTokenTypes.LPAREN, SquirrelTokenTypes.RPAREN, false),
            new BracePair(SquirrelTokenTypes.CLASS_ATTR_START, SquirrelTokenTypes.CLASS_ATTR_END, false),
    };

    @Override
    public BracePair[] getPairs() {
        return BRACE_PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
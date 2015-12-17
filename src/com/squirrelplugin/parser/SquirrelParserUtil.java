package com.squirrelplugin.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.squirrelplugin.SquirrelTokenTypes.*;
import static com.squirrelplugin.SquirrelTokenTypesSets.*;

public class SquirrelParserUtil extends GeneratedParserUtilBase {

    public static boolean prevIsBrace(@NotNull PsiBuilder builder_, int level) {
        IElementType type = null;
        for (int i = 1; i < builder_.getCurrentOffset(); i++) {
            type = builder_.rawLookup(-i);
            if (type != WS && type != NL && type != SINGLE_LINE_COMMENT && type != MULTI_LINE_COMMENT) {
                break;
            }
        }

        return type != null && (type == RBRACE || type == SEMICOLON || type == SEMICOLON_SYNTHETIC);
    }
}
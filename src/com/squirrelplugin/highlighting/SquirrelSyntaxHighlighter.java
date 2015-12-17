package com.squirrelplugin.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.SquirrelTokenTypes;
import com.squirrelplugin.SquirrelTokenTypesSets;
import com.squirrelplugin.lexer.SquirrelLexer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.squirrelplugin.highlighting.SquirrelSyntaxHighlightingColors.*;

public class SquirrelSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

    static {
        fillMap(ATTRIBUTES, SINGLE_LINE_COMMENT, SquirrelTokenTypes.SINGLE_LINE_COMMENT);
        fillMap(ATTRIBUTES, MULTI_LINE_COMMENT, SquirrelTokenTypes.MULTI_LINE_COMMENT);
        fillMap(ATTRIBUTES, MULTI_LINE_DOC_COMMENT, SquirrelTokenTypesSets.MULTI_LINE_DOC_COMMENT);
        fillMap(ATTRIBUTES, PARENTHESES, SquirrelTokenTypes.LPAREN, SquirrelTokenTypes.RPAREN);
        fillMap(ATTRIBUTES, BRACES, SquirrelTokenTypes.LBRACE, SquirrelTokenTypes.RBRACE);
        fillMap(ATTRIBUTES, BRACKETS, SquirrelTokenTypes.LBRACKET, SquirrelTokenTypes.RBRACKET);
        fillMap(ATTRIBUTES, CLASS_ATTRIBUTES, SquirrelTokenTypes.CLASS_ATTR_START, SquirrelTokenTypes.CLASS_ATTR_END);
        fillMap(ATTRIBUTES, BAD_CHARACTER, TokenType.BAD_CHARACTER);
        fillMap(ATTRIBUTES, IDENTIFIER, SquirrelTokenTypes.IDENTIFIER);
        fillMap(ATTRIBUTES, COLON, SquirrelTokenTypes.COLON);
        fillMap(ATTRIBUTES, SEMICOLON, SquirrelTokenTypes.SEMICOLON);
        fillMap(ATTRIBUTES, COMMA, SquirrelTokenTypes.COMMA);
        fillMap(ATTRIBUTES, DOT, SquirrelTokenTypes.DOT);
        fillMap(ATTRIBUTES, SquirrelTokenTypesSets.OPERATORS, OPERATOR);
        fillMap(ATTRIBUTES, SquirrelTokenTypesSets.KEYWORDS, KEYWORD);
        fillMap(ATTRIBUTES, SquirrelTokenTypesSets.NUMBERS, NUMBER);
        fillMap(ATTRIBUTES, SquirrelTokenTypesSets.STRING_LITERALS, STRING);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new SquirrelLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ATTRIBUTES.get(tokenType));
    }
}
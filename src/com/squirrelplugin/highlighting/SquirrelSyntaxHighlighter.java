package com.squirrelplugin.highlighting;

import com.intellij.lexer.Lexer;
import com.squirrelplugin.lexer.SquirrelLexerAdapter;
import com.squirrelplugin.SquirrelParserDefinition;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.psi.SquirrelTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.squirrelplugin.highlighting.SquirrelSyntaxHighlightingColors.*;

public class SquirrelSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

    static {
        fillMap(ATTRIBUTES, LINE_COMMENT, SquirrelTypes.LINE_COMMENT);
        fillMap(ATTRIBUTES, BLOCK_COMMENT, SquirrelTypes.BLOCK_COMMENT);
        fillMap(ATTRIBUTES, PARENTHESES, SquirrelTypes.LPAREN, SquirrelTypes.RPAREN);
        fillMap(ATTRIBUTES, BRACES, SquirrelTypes.LBRACE, SquirrelTypes.RBRACE);
        fillMap(ATTRIBUTES, BRACKETS, SquirrelTypes.LBRACK, SquirrelTypes.RBRACK);
        fillMap(ATTRIBUTES, BAD_CHARACTER, TokenType.BAD_CHARACTER);
        fillMap(ATTRIBUTES, IDENTIFIER, SquirrelTypes.IDENTIFIER);
        fillMap(ATTRIBUTES, COLON, SquirrelTypes.COLON);
        fillMap(ATTRIBUTES, SEMICOLON, SquirrelTypes.SEMICOLON);
        fillMap(ATTRIBUTES, COMMA, SquirrelTypes.COMMA);
        fillMap(ATTRIBUTES, SquirrelParserDefinition.OPERATORS, OPERATOR);
        fillMap(ATTRIBUTES, SquirrelParserDefinition.KEYWORDS, KEYWORD);
        fillMap(ATTRIBUTES, SquirrelParserDefinition.NUMBERS, NUMBER);
        fillMap(ATTRIBUTES, SquirrelParserDefinition.STRING_LITERALS, STRING);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new SquirrelLexerAdapter();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ATTRIBUTES.get(tokenType));
    }
}
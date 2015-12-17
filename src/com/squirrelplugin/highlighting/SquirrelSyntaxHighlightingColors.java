package com.squirrelplugin.highlighting;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SquirrelSyntaxHighlightingColors {
  public static final TextAttributesKey SINGLE_LINE_COMMENT = createTextAttributesKey("SQ_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey MULTI_LINE_COMMENT = createTextAttributesKey("SQ_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
  public static final TextAttributesKey MULTI_LINE_DOC_COMMENT = createTextAttributesKey("SQ_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
  public static final TextAttributesKey KEYWORD = createTextAttributesKey("SQ_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey STRING = createTextAttributesKey("SQ_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey NUMBER = createTextAttributesKey("SQ_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey BRACKETS = createTextAttributesKey("SQ_BRACKET", DefaultLanguageHighlighterColors.BRACKETS);
  public static final TextAttributesKey BRACES = createTextAttributesKey("SQ_BRACES", DefaultLanguageHighlighterColors.BRACES);
  public static final TextAttributesKey PARENTHESES = createTextAttributesKey("SQ_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
  public static final TextAttributesKey CLASS_ATTRIBUTES = createTextAttributesKey("SQ_CLASS_ATTRIBUTES", DefaultLanguageHighlighterColors.METADATA);
  public static final TextAttributesKey OPERATOR = createTextAttributesKey("SQ_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
  public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("SQ_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey DOT = createTextAttributesKey("SQ_DOT", DefaultLanguageHighlighterColors.DOT);
  public static final TextAttributesKey SEMICOLON = createTextAttributesKey("SQ_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
  public static final TextAttributesKey COLON = createTextAttributesKey("SQ_COLON", HighlighterColors.TEXT);
  public static final TextAttributesKey COMMA = createTextAttributesKey("SQ_COMMA", DefaultLanguageHighlighterColors.COMMA);
  public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("SQ_BAD_TOKEN", HighlighterColors.BAD_CHARACTER);
  private SquirrelSyntaxHighlightingColors() {
  }
}

package com.squirrelplugin.highlighting;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SquirrelSyntaxHighlightingColors {
  public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("SQ_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("SQ_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
  public static final TextAttributesKey KEYWORD = createTextAttributesKey("SQ_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey STRING = createTextAttributesKey("SQ_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey NUMBER = createTextAttributesKey("SQ_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey BRACKETS = createTextAttributesKey("SQ_BRACKET", DefaultLanguageHighlighterColors.BRACKETS);
  public static final TextAttributesKey BRACES = createTextAttributesKey("SQ_BRACES", DefaultLanguageHighlighterColors.BRACES);
  public static final TextAttributesKey PARENTHESES = createTextAttributesKey("SQ_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
  public static final TextAttributesKey OPERATOR = createTextAttributesKey("SQ_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
  public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("SQ_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey DOT = createTextAttributesKey("SQ_DOT", DefaultLanguageHighlighterColors.DOT);
  public static final TextAttributesKey SEMICOLON = createTextAttributesKey("SQ_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
  public static final TextAttributesKey COLON = createTextAttributesKey("SQ_COLON", HighlighterColors.TEXT);
  public static final TextAttributesKey COMMA = createTextAttributesKey("SQ_COMMA", DefaultLanguageHighlighterColors.COMMA);
  public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("SQ_BAD_TOKEN", HighlighterColors.BAD_CHARACTER);
  public static final TextAttributesKey TYPE_SPECIFICATION = createTextAttributesKey("SQ_TYPE_SPECIFICATION", DefaultLanguageHighlighterColors.CLASS_NAME);
  public static final TextAttributesKey TYPE_REFERENCE = createTextAttributesKey("SQ_TYPE_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
  public static final TextAttributesKey BUILTIN_TYPE_REFERENCE = createTextAttributesKey("SQ_BUILTIN_TYPE_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
  public static final TextAttributesKey EXPORTED_FUNCTION = createTextAttributesKey("SQ_EXPORTED_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
  public static final TextAttributesKey LOCAL_FUNCTION = createTextAttributesKey("SQ_LOCAL_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
  public static final TextAttributesKey PACKAGE_EXPORTED_INTERFACE = createTextAttributesKey("SQ_PACKAGE_EXPORTED_INTERFACE", DefaultLanguageHighlighterColors.INTERFACE_NAME);
  public static final TextAttributesKey PACKAGE_EXPORTED_STRUCT = createTextAttributesKey("SQ_PACKAGE_EXPORTED_STRUCT", DefaultLanguageHighlighterColors.CLASS_NAME);
  public static final TextAttributesKey PACKAGE_EXPORTED_CONSTANT = createTextAttributesKey("SQ_PACKAGE_EXPORTED_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT);
  public static final TextAttributesKey PACKAGE_EXPORTED_VARIABLE = createTextAttributesKey("SQ_PACKAGE_EXPORTED_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE);
  public static final TextAttributesKey PACKAGE_LOCAL_INTERFACE = createTextAttributesKey("SQ_PACKAGE_LOCAL_INTERFACE", DefaultLanguageHighlighterColors.INTERFACE_NAME);
  public static final TextAttributesKey PACKAGE_LOCAL_STRUCT = createTextAttributesKey("SQ_PACKAGE_LOCAL_STRUCT", DefaultLanguageHighlighterColors.CLASS_NAME);
  public static final TextAttributesKey PACKAGE_LOCAL_CONSTANT = createTextAttributesKey("SQ_PACKAGE_LOCAL_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT);
  public static final TextAttributesKey PACKAGE_LOCAL_VARIABLE = createTextAttributesKey("SQ_PACKAGE_LOCAL_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
  public static final TextAttributesKey STRUCT_EXPORTED_MEMBER = createTextAttributesKey("SQ_STRUCT_EXPORTED_MEMBER", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE);
  public static final TextAttributesKey STRUCT_LOCAL_MEMBER = createTextAttributesKey("SQ_STRUCT_LOCAL_MEMBER", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
  public static final TextAttributesKey METHOD_RECEIVER = createTextAttributesKey("SQ_METHOD_RECEIVER", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
  public static final TextAttributesKey FUNCTION_PARAMETER = createTextAttributesKey("SQ_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
  public static final TextAttributesKey LOCAL_CONSTANT = createTextAttributesKey("SQ_LOCAL_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT);
  public static final TextAttributesKey LOCAL_VARIABLE = createTextAttributesKey("SQ_LOCAL_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
  public static final TextAttributesKey SCOPE_VARIABLE = createTextAttributesKey("SQ_SCOPE_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
  public static final TextAttributesKey LABEL = createTextAttributesKey("SQ_LABEL", DefaultLanguageHighlighterColors.LABEL);
  private SquirrelSyntaxHighlightingColors() {
  }
}

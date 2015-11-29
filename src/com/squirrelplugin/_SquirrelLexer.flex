package com.squirrelplugin;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.squirrelplugin.psi.SquirrelTypes.*;
import static com.squirrelplugin.SquirrelParserDefinition.*;
import com.intellij.psi.TokenType;

%%

%{
  public _SquirrelLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SquirrelLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

LINE_COMMENT=("//"|#)[^\r\n]*
BLOCK_COMMENT="/"\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*(\*+"/")
IDENTIFIER=[:letter:][a-zA-Z_0-9]*
INT=((0[1-9][0-7]*)|(0x[0-9a-fA-F]*)|('[:letter:]')|(0|([1-9][0-9]*)))
FLOAT=((([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+))([eE][+-]?[0-9]+)?)|([0-9]+([eE][+-]?[0-9]+))
STRING=(@\"([^\"]|\"\")*\"|\"(\\.|[^\"\n\r])*\")
NL=[\r\n]|\r\n
WS=[ \t\f]

%state MAYBE_SEMICOLON

%%
<YYINITIAL> {
  {WS}                 { return TokenType.WHITE_SPACE; }
  {NL}+                { return NLS; }

  "}"                  { yybegin(MAYBE_SEMICOLON); return RBRACE; }
  "]"                  { yybegin(MAYBE_SEMICOLON); return RBRACK; }
  ")"                  { yybegin(MAYBE_SEMICOLON); return RPAREN; }
  "++"                 { yybegin(MAYBE_SEMICOLON); return PLUS_PLUS; }
  "--"                 { yybegin(MAYBE_SEMICOLON); return MINUS_MINUS; }
  "{"                  { return LBRACE; }
  "["                  { return LBRACK; }
  "("                  { return LPAREN; }
  ":"                  { return COLON; }
  "::"                 { return DOUBLE_COLON; }
  ";"                  { return SEMICOLON; }
  ","                  { return COMMA; }
  "="                  { return ASSIGN; }
  "<-"                 { return SEND_CHANNEL; }
  "=="                 { return EQ; }
  "!="                 { return NOT_EQ; }
  "<=>"                { return CMP; }
  "!"                  { return NOT; }
  "||"                 { return COND_OR; }
  "&&"                 { return COND_AND; }
  "|"                  { return BIT_OR; }
  "^"                  { return BIT_XOR; }
  "&"                  { return BIT_AND; }
  "<"                  { return LESS; }
  "<="                 { return LESS_OR_EQUAL; }
  ">="                 { return GREATER_OR_EQUAL; }
  ">"                  { return GREATER; }
  "<<"                 { return SHIFT_LEFT; }
  ">>"                 { return SHIFT_RIGHT; }
  ">>>"                { return UNSIGNED_SHIFT_RIGHT; }
  "+="                 { return PLUS_ASSIGN; }
  "+"                  { return PLUS; }
  "-="                 { return MINUS_ASSIGN; }
  "-"                  { return MINUS; }
  "*"                  { return MUL; }
  "/"                  { return QUOTIENT; }
  "%"                  { return REMAINDER; }
  "const"              { return CONST; }
  "local"              { return LOCAL; }
  "function"           { return FUNCTION; }
  "in"                 { return IN; }
  "typeof"             { return TYPEOF; }
  "true"               { return TRUE; }
  "false"              { return FALSE; }
  "null"               { return NULL; }

  {LINE_COMMENT}       { return LINE_COMMENT; }
  {BLOCK_COMMENT}      { return BLOCK_COMMENT; }
  {IDENTIFIER}         { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
  {INT}                { yybegin(MAYBE_SEMICOLON); return INT; }
  {FLOAT}              { yybegin(MAYBE_SEMICOLON); return FLOAT; }
  {STRING}             { yybegin(MAYBE_SEMICOLON); return STRING; }

  [^] { return TokenType.BAD_CHARACTER; }
}

<MAYBE_SEMICOLON> {
{WS}               { return TokenType.WHITE_SPACE; }
{NL}               { yybegin(YYINITIAL); yypushback(yytext().length()); return SEMICOLON_SYNTHETIC; }
{LINE_COMMENT}     { return LINE_COMMENT; }
{BLOCK_COMMENT}    { return BLOCK_COMMENT; }

.                  { yybegin(YYINITIAL); yypushback(yytext().length()); }

}
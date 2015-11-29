package com.squirrelplugin;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.squirrelplugin.psi.SquirrelTypes.*;

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

EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+

LINE_COMMENT=("//"|#)[^\r\n]*
BLOCK_COMMENT="/"\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*(\*+"/")
IDENTIFIER=[:letter:][a-zA-Z_0-9]*
INT=((0[1-9][0-7]*)|(0x[0-9a-fA-F]*)|('[:letter:]')|(0|([1-9][0-9]*)))
FLOAT=((([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+))([eE][+-]?[0-9]+)?)|([0-9]+([eE][+-]?[0-9]+))
STRING=(@\"([^\"]|\"\")*\"|\"(\\.|[^\"\n\r])*\")
WS=[\n\r\ \t\f]+

%%
<YYINITIAL> {
  {WHITE_SPACE}        { return com.intellij.psi.TokenType.WHITE_SPACE; }

  "{"                  { return LBRACE; }
  "}"                  { return RBRACE; }
  "["                  { return LBRACK; }
  "]"                  { return RBRACK; }
  "("                  { return LPAREN; }
  ")"                  { return RPAREN; }
  "::"                 { return DOUBLE_COLON; }
  ":"                  { return COLON; }
  ";"                  { return SEMICOLON; }
  "<NL>"               { return SEMICOLON_SYNTHETIC; }
  ","                  { return COMMA; }
  "=="                 { return EQ; }
  "="                  { return ASSIGN; }
  "!="                 { return NOT_EQ; }
  "!"                  { return NOT; }
  "++"                 { return PLUS_PLUS; }
  "+="                 { return PLUS_ASSIGN; }
  "+"                  { return PLUS; }
  "--"                 { return MINUS_MINUS; }
  "-="                 { return MINUS_ASSIGN; }
  "-"                  { return MINUS; }
  "||"                 { return COND_OR; }
  "|="                 { return BIT_OR_ASSIGN; }
  "&^="                { return BIT_CLEAR_ASSIGN; }
  "&^"                 { return BIT_CLEAR; }
  "&&"                 { return COND_AND; }
  "&="                 { return BIT_AND_ASSIGN; }
  "&"                  { return BIT_AND; }
  "|"                  { return BIT_OR; }
  "<<="                { return SHIFT_LEFT_ASSIGN; }
  "<<"                 { return SHIFT_LEFT; }
  "<-"                 { return SEND_CHANNEL; }
  "<="                 { return LESS_OR_EQUAL; }
  "<"                  { return LESS; }
  "^="                 { return BIT_XOR_ASSIGN; }
  "^"                  { return BIT_XOR; }
  "*="                 { return MUL_ASSIGN; }
  "*"                  { return MUL; }
  "/="                 { return QUOTIENT_ASSIGN; }
  "/"                  { return QUOTIENT; }
  "%="                 { return REMAINDER_ASSIGN; }
  "%"                  { return REMAINDER; }
  ">>="                { return SHIFT_RIGHT_ASSIGN; }
  ">>"                 { return SHIFT_RIGHT; }
  ">="                 { return GREATER_OR_EQUAL; }
  ">"                  { return GREATER; }
  "const"              { return CONST; }
  "local"              { return LOCAL; }
  "function"           { return FUNCTION; }
  "in"                 { return IN; }
  "true"               { return TRUE; }
  "false"              { return FALSE; }
  "null"               { return NULL; }

  {LINE_COMMENT}       { return LINE_COMMENT; }
  {BLOCK_COMMENT}      { return BLOCK_COMMENT; }
  {IDENTIFIER}         { return IDENTIFIER; }
  {INT}                { return INT; }
  {FLOAT}              { return FLOAT; }
  {STRING}             { return STRING; }
  {WS}                 { return WS; }

  [^] { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

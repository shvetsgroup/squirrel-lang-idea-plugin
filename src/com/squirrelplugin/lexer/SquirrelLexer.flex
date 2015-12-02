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

%state MAYBE_SEMICOLON, RBRACE_SEMICOLON

%%
<YYINITIAL> {
  {WS}                 { return TokenType.WHITE_SPACE; }
  {NL}+                { return NLS; }

  "}"                  { yybegin(RBRACE_SEMICOLON); return RBRACE; }
  "]"                  { yybegin(MAYBE_SEMICOLON); return RBRACK; }
  ")"                  { yybegin(MAYBE_SEMICOLON); return RPAREN; } // TODO: Do not add semicolon if it's after keyword braces like for or if.
  "++"                 { yybegin(MAYBE_SEMICOLON); return INCREMENT; }
  "--"                 { yybegin(MAYBE_SEMICOLON); return DECREMENT; }
  "{"                  { return LBRACE; }
  "["                  { return LBRACK; }
  "("                  { return LPAREN; }
  "::"                 { return DOUBLE_COLON; }
  ":"                  { return COLON; }
  ";"                  { return SEMICOLON; }
  ","                  { return COMMA; }
  "..."                { return MULTI_ARGS; }
  "</"                 { return CLASS_ATTR_START; }
  "/>"                 { return CLASS_ATTR_END; }
  "<<"                 { return SHIFT_LEFT; }
  ">>"                 { return SHIFT_RIGHT; }
  ">>>"                { return UNSIGNED_SHIFT_RIGHT; }
  "<=>"                { return CMP; }
  "=="                 { return EQ; }
  "!="                 { return NOT_EQ; }
  "<="                 { return LESS_OR_EQUAL; }
  ">="                 { return GREATER_OR_EQUAL; }
  "<-"                 { return SEND_CHANNEL; }
  "+="                 { return PLUS_ASSIGN; }
  "-="                 { return MINUS_ASSIGN; }
  "*="                 { return MUL_ASSIGN; }
  "/="                 { return QUOTIENT_ASSIGN; }
  "%="                 { return REMAINDER_ASSIGN; }
  "||"                 { return COND_OR; }
  "&&"                 { return COND_AND; }
  "="                  { return ASSIGN; }
  "!"                  { return NOT; }
  "~"                  { return BIT_NOT; }
  "|"                  { return BIT_OR; }
  "^"                  { return BIT_XOR; }
  "&"                  { return BIT_AND; }
  "<"                  { return LESS; }
  ">"                  { return GREATER; }
  "+"                  { return PLUS; }
  "-"                  { return MINUS; }
  "*"                  { return MUL; }
  "/"                  { return QUOTIENT; }
  "%"                  { return REMAINDER; }
  "?"                  { return QUESTION; }
  "@"                  { return AT; }
  "."                  { return PERIOD; }
  "const"              { return CONST; }
  "enum"               { return ENUM; }
  "local"              { return LOCAL; }
  "function"           { return FUNCTION; }
  "constructor"        { return CONSTRUCTOR; }
  "class"              { return CLASS; }
  "extends"            { return EXTENDS; }
  "static"             { return STATIC; }
  "break"              { return BREAK; }
  "continue"           { return CONTINUE; }
  "return"             { return RETURN; }
  "yield"              { return YIELD; }
  "throw"              { return THROW; }
  "for"                { return FOR; }
  "foreach"            { return FOREACH; }
  "in"                 { return IN; }
  "while"              { return WHILE; }
  "do"                 { return DO; }
  "if"                 { return IF; }
  "else"               { return ELSE; }
  "switch"             { return SWITCH; }
  "case"               { return CASE; }
  "default"            { return DEFAULT; }
  "try"                { return TRY; }
  "catch"              { return CATCH; }
  "typeof"             { return TYPEOF; }
  "clone"              { return CLONE; }
  "delete"             { return DELETE; }
  "resume"             { return RESUME; }
  "instanceof"         { return INSTANCEOF; }
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

<RBRACE_SEMICOLON> {
";"                { yybegin(YYINITIAL); yypushback(yytext().length()); }
.                  { yybegin(YYINITIAL); yypushback(yytext().length()); return SEMICOLON_SYNTHETIC; }
}

<MAYBE_SEMICOLON> {
{WS}               { return TokenType.WHITE_SPACE; }
{NL}               { yybegin(YYINITIAL); yypushback(yytext().length()); return SEMICOLON_SYNTHETIC; }
{LINE_COMMENT}     { return LINE_COMMENT; }
{BLOCK_COMMENT}    { return BLOCK_COMMENT; }

.                  { yybegin(YYINITIAL); yypushback(yytext().length()); }

}
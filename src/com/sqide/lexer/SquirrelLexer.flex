package com.sqide.lexer;
import java.util.*;
import com.intellij.lexer.*;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import static com.sqide.SquirrelTokenTypes.*;
import static com.sqide.SquirrelTokenTypesSets.*;
import static com.sqide.lexer.SquirrelLexer.*;

%%

%{
    public _SquirrelLexer() {
        this((java.io.Reader)null);
    }

    protected int myLeftParenCount = 0;
    protected boolean setClassStateOnBrace = false;

    private static final class State {
        final int state;

        public State(int state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "yystate = " + state;
        }
    }

    protected final Stack<State> myStateStack = new Stack<State>();

    private void pushState(int state) {
        myStateStack.push(new State(yystate()));
        yybegin(state);
    }

    private void pushStateUnless(int state, int... unlessNotIn) {
        for (int i = 0; i < unlessNotIn.length; i++) {
            if (unlessNotIn[i] == yystate()) return;
        }
        myStateStack.push(new State(yystate()));
        yybegin(state);
    }

    private void popState() {
        if (!myStateStack.empty()) {
            State state = myStateStack.pop();
            yybegin(state.state);
        }
    }
%}

%public
%class _SquirrelLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%eof{
  myLeftParenCount = 0;
  myStateStack.clear();
  setClassStateOnBrace = false;
%eof}

SINGLE_LINE_COMMENT=("//"|#)[^\r\n]*

MULTI_LINE_DEGENERATE_COMMENT = "/*" "*"+ "/"
MULTI_LINE_COMMENT_START      = "/*"
MULTI_LINE_DOC_COMMENT_START  = "/**"
MULTI_LINE_COMMENT_END        = "*/"


IDENTIFIER=[a-zA-Z_]+[a-zA-Z_0-9]*
INT=((0[1-9][0-7]*)|(0x[0-9a-fA-F]*)|('[:letter:]')|(0|([1-9][0-9]*)))
FLOAT=((([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+))([eE][+-]?[0-9]+)?)|([0-9]+([eE][+-]?[0-9]+))
STRING=(@\"([^\"]|\"\")*\"|\"(\\.|[^\"\n\r])*\")
NL=\r|\n|\r\n
WS=[ \t\f]
ANY=[ \t\f\r\na-zA-Z0-9_\-\.,+-*&\^%@!~|<>/\?:;\(\)\{\}\[\]]

// SS means SYNTHETIC_SEMICOLON
%state MULTI_LINE_COMMENT_STATE, SEE_IF_NEXT_IS_NL, MAYBE_SET_SS, MAYBE_TERMINATE_KEYWORD_WITH_NL, PREVENT_SS_AFTER_PARENTHESES, PREVENT_SS_UNTIL_BRACE, DO_STATEMENT, TRY_STATEMENT, BRACES, CLASS_DECLARATION_BRACES

%%

<YYINITIAL, SEE_IF_NEXT_IS_NL, MAYBE_SET_SS, MAYBE_TERMINATE_KEYWORD_WITH_NL, PREVENT_SS_AFTER_PARENTHESES, PREVENT_SS_UNTIL_BRACE, DO_STATEMENT, TRY_STATEMENT, BRACES, CLASS_DECLARATION_BRACES> {
  {SINGLE_LINE_COMMENT} { return SINGLE_LINE_COMMENT; }

  // multi-line comments
  // without this rule /*****/ is parsed as doc comment and /**/ is parsed as not closed doc comment
  {MULTI_LINE_DEGENERATE_COMMENT} { return MULTI_LINE_COMMENT; }
  // next rules return temporary IElementType's that are replaced with SquirrelTokenTypesSets#MULTI_LINE_COMMENT or
  // SquirrelTokenTypesSets#MULTI_LINE_DOC_COMMENT in com.sqideuirrelLexer
  {MULTI_LINE_DOC_COMMENT_START}  { pushState(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_DOC_COMMENT_START;                                                             }
  {MULTI_LINE_COMMENT_START}      { pushState(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_COMMENT_START;                                                                 }
}

<MULTI_LINE_COMMENT_STATE> {
{MULTI_LINE_COMMENT_START} { return MULTI_LINE_COMMENT_BODY; }
[^]                        { return MULTI_LINE_COMMENT_BODY; }
{MULTI_LINE_COMMENT_END}   { return yystate() == MULTI_LINE_COMMENT_STATE ? MULTI_LINE_COMMENT_BODY : MULTI_LINE_COMMENT_END; }
}

<YYINITIAL, PREVENT_SS_AFTER_PARENTHESES, PREVENT_SS_UNTIL_BRACE, DO_STATEMENT, TRY_STATEMENT, BRACES, CLASS_DECLARATION_BRACES> {
  {WS}+                { return WS; }
  {NL}+                { return NL; }

  "{"                  {

if (yystate() == PREVENT_SS_UNTIL_BRACE) {
    popState();
}

if (setClassStateOnBrace) {
  pushState(CLASS_DECLARATION_BRACES);
  setClassStateOnBrace = false;
}
else {
  pushState(BRACES);
}

return LBRACE;

                       }
  "}"                  { popState(); return RBRACE; }

  "["                  { return LBRACKET; }
  "]"                  { pushState(SEE_IF_NEXT_IS_NL); return RBRACKET; }

  "("                  { myLeftParenCount++; return LPAREN; }
  ")"                  {

                        myLeftParenCount--;
                        if (myLeftParenCount == 0) {
                            if (yystate() == PREVENT_SS_AFTER_PARENTHESES) {
                                popState();
                            } else {
                                pushState(SEE_IF_NEXT_IS_NL);
                            }
                        }

                       return RPAREN;
                       }

  "++"                 { pushState(SEE_IF_NEXT_IS_NL); return PLUS_PLUS; }
  "--"                 { pushState(SEE_IF_NEXT_IS_NL); return MINUS_MINUS; }
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
  "=="                 { return EQ_EQ; }
  "!="                 { return NOT_EQ; }
  "<="                 { return LESS_OR_EQ; }
  ">="                 { return GREATER_OR_EQ; }
  "<-"                 { return SEND_CHANNEL; }
  "+="                 { return PLUS_EQ; }
  "-="                 { return MINUS_EQ; }
  "*="                 { return MUL_EQ; }
  "/="                 { return DIV_EQ; }
  "%="                 { return REMAINDER_EQ; }
  "||"                 { return OR_OR; }
  "&&"                 { return AND_AND; }
  "="                  { return EQ; }
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
  "/"                  { return DIV; }
  "%"                  { return REMAINDER; }
  "?"                  { return QUESTION; }
  "@"                  { return AT; }
  "."                  { return DOT; }
  "const"              { return CONST; }
  "enum"               { pushState(PREVENT_SS_UNTIL_BRACE); return ENUM; }
  "local"              { return LOCAL; }
  "function"           { pushState(PREVENT_SS_AFTER_PARENTHESES); return FUNCTION; }
  "constructor"        {
      if (yystate() == CLASS_DECLARATION_BRACES) {
        pushState(PREVENT_SS_AFTER_PARENTHESES);
      }
      return CONSTRUCTOR;
  }
  "class"              { pushState(PREVENT_SS_UNTIL_BRACE); setClassStateOnBrace = true; return CLASS; }
  "extends"            { return EXTENDS; }
  "static"             { return STATIC; }
  "break"              { pushState(MAYBE_TERMINATE_KEYWORD_WITH_NL); return BREAK; }
  "continue"           { pushState(MAYBE_TERMINATE_KEYWORD_WITH_NL); return CONTINUE; }
  "return"             { pushState(MAYBE_TERMINATE_KEYWORD_WITH_NL); return RETURN; }
  "yield"              { return YIELD; }
  "throw"              { return THROW; }
  "for"                { pushState(PREVENT_SS_AFTER_PARENTHESES); return FOR; }
  "foreach"            { pushState(PREVENT_SS_AFTER_PARENTHESES); return FOREACH; }
  "in"                 { return IN; }

  // We need to track state of these two in order to place correct synthetic semicolons.
  // Let's consider following code:
  //
  // do
  //   print()
  // while (a)
  // a = 1
  //
  // while (b)
  //   b--
  //
  // There are 2 possible problems when you don't track state:
  // 1) syn_semicolon will be placed after print() which will break BNF rule causing an error.
  // 2) syn_semicolon will be placed after "while (b)", which will produce incorrect PSI.
  //
  // So, to fix first one, we do special treatment inside MAYBE_SET_SS for while keyword.
  // To fix second, we do some checks in the code of "while" keyword (below).

  "do"                 { pushState(DO_STATEMENT); return DO; }
  "while"              {

if (yystate() != DO_STATEMENT) {
  pushState(PREVENT_SS_AFTER_PARENTHESES);
}
else {
  popState();
}
return WHILE;

                       }

  // TRY has the problem 1 of DO, so we need to track it's state as well.
  "try"                { pushState(TRY_STATEMENT); return TRY; }
  "catch"              { popState(); pushState(PREVENT_SS_AFTER_PARENTHESES); return CATCH; }

  // You would expect same type of problems with IF, but due to strange implementation of Squirrel, where it requires
  // IFs to have semicolons after a statement. This might change in future.
  "if"                 { pushState(PREVENT_SS_AFTER_PARENTHESES); return IF; }
  "else"               { return ELSE; }

  "switch"             { pushState(PREVENT_SS_AFTER_PARENTHESES); return SWITCH; }
  "case"               { return CASE; }
  "default"            { return DEFAULT; }
  "typeof"             { return TYPEOF; }
  "clone"              { return CLONE; }
  "delete"             { return DELETE; }
  "resume"             { return RESUME; }
  "instanceof"         { return INSTANCEOF; }
  "true"               { return TRUE; }
  "false"              { return FALSE; }
  "null"               { return NULL; }

  {IDENTIFIER}         { pushStateUnless(SEE_IF_NEXT_IS_NL, PREVENT_SS_UNTIL_BRACE, PREVENT_SS_AFTER_PARENTHESES); return IDENTIFIER; }
  {INT}                { pushState(SEE_IF_NEXT_IS_NL); return INT; }
  {FLOAT}              { pushState(SEE_IF_NEXT_IS_NL); return FLOAT; }
  {STRING}             { pushState(SEE_IF_NEXT_IS_NL); return STRING; }

  [^] { return TokenType.BAD_CHARACTER; }
}

<MAYBE_TERMINATE_KEYWORD_WITH_NL> {
{WS}+                  { return WS; }
{NL}+                  { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
.                      { popState(); yypushback(yylength());  }
}

<SEE_IF_NEXT_IS_NL> {
{WS}+                  { return WS; }
{NL}+                  { popState(); pushState(MAYBE_SET_SS); return NL; }
.                      { popState(); yypushback(yylength());  }
}

<MAYBE_SET_SS> {
{WS}+                  { return WS; }
{NL}+                  { return NL; }

// binary operator should continue expression
"instanceof"           { popState(); yypushback(yylength()); }

// unary operators should break expression (rest of operators (i.e. typeof) match as {IDENTIFIER})
"!"                    { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
"~"                    { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
"--"                   { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
"++"                   { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }

"{"                    { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
"["                    { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; } // TODO: works as current compiler, but may be not right
"("                    { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; } // TODO: works not as current compiler, but may be not right

"::"                   { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
"while"                { popState(); yypushback(yylength()); if (yystate() != DO_STATEMENT) {return SEMICOLON_SYNTHETIC;} }
"catch"                { popState(); yypushback(yylength()); if (yystate() != TRY_STATEMENT) {return SEMICOLON_SYNTHETIC;} }
{IDENTIFIER}           { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
{INT}                  { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
{FLOAT}                { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }
{STRING}               { popState(); yypushback(yylength()); return SEMICOLON_SYNTHETIC; }

.                      { popState(); yypushback(yylength()); }
}
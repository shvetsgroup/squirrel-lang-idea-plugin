// This is a generated file. Not intended for manual editing.
package com.squirrelplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.squirrelplugin.psi.impl.*;

public interface SquirrelTypes {

  IElementType AND_EXPR = new SquirrelElementType("AND_EXPR");
  IElementType ASSIGN_EXPR = new SquirrelElementType("ASSIGN_EXPR");
  IElementType BIT_AND_EXPR = new SquirrelElementType("BIT_AND_EXPR");
  IElementType BIT_OR_EXPR = new SquirrelElementType("BIT_OR_EXPR");
  IElementType BIT_SHIFT_EXPR = new SquirrelElementType("BIT_SHIFT_EXPR");
  IElementType BIT_XOR_EXPR = new SquirrelElementType("BIT_XOR_EXPR");
  IElementType BLOCK = new SquirrelElementType("BLOCK");
  IElementType COMMA_EXPR = new SquirrelElementType("COMMA_EXPR");
  IElementType COMPARE_EXPR = new SquirrelElementType("COMPARE_EXPR");
  IElementType CONST_DECLARATION = new SquirrelElementType("CONST_DECLARATION");
  IElementType ELVIS_EXPR = new SquirrelElementType("ELVIS_EXPR");
  IElementType EXPR = new SquirrelElementType("EXPR");
  IElementType FUNCTION_DECLARATION = new SquirrelElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_NAME = new SquirrelElementType("FUNCTION_NAME");
  IElementType IDENTIFIER = new SquirrelElementType("IDENTIFIER");
  IElementType IN_EXPR = new SquirrelElementType("IN_EXPR");
  IElementType LESS_GREATER_EQUAL_EXPR = new SquirrelElementType("LESS_GREATER_EQUAL_EXPR");
  IElementType LITERAL = new SquirrelElementType("LITERAL");
  IElementType LITERAL_EXPR = new SquirrelElementType("LITERAL_EXPR");
  IElementType LOCAL_VAR_DECLARATION = new SquirrelElementType("LOCAL_VAR_DECLARATION");
  IElementType MUL_DIV_MOD_EXPR = new SquirrelElementType("MUL_DIV_MOD_EXPR");
  IElementType OR_EXPR = new SquirrelElementType("OR_EXPR");
  IElementType PAREN_EXPR = new SquirrelElementType("PAREN_EXPR");
  IElementType PLUS_MINUS_EXPR = new SquirrelElementType("PLUS_MINUS_EXPR");
  IElementType REF_EXPR = new SquirrelElementType("REF_EXPR");
  IElementType STATEMENTS_WITH_SEMI = new SquirrelElementType("STATEMENTS_WITH_SEMI");
  IElementType STATEMENT_WITHOUT_SEMI = new SquirrelElementType("STATEMENT_WITHOUT_SEMI");
  IElementType UNARY_EXPR = new SquirrelElementType("UNARY_EXPR");
  IElementType VAR_DECLARATION = new SquirrelElementType("VAR_DECLARATION");

  IElementType ASSIGN = new SquirrelTokenType("=");
  IElementType BIT_AND = new SquirrelTokenType("&");
  IElementType BIT_AND_ASSIGN = new SquirrelTokenType("&=");
  IElementType BIT_CLEAR = new SquirrelTokenType("&^");
  IElementType BIT_CLEAR_ASSIGN = new SquirrelTokenType("&^=");
  IElementType BIT_OR = new SquirrelTokenType("|");
  IElementType BIT_OR_ASSIGN = new SquirrelTokenType("|=");
  IElementType BIT_XOR = new SquirrelTokenType("^");
  IElementType BIT_XOR_ASSIGN = new SquirrelTokenType("^=");
  IElementType BLOCK_COMMENT = new SquirrelTokenType("BLOCK_COMMENT");
  IElementType COLON = new SquirrelTokenType(":");
  IElementType COMMA = new SquirrelTokenType(",");
  IElementType COND_AND = new SquirrelTokenType("&&");
  IElementType COND_OR = new SquirrelTokenType("||");
  IElementType CONST = new SquirrelTokenType("const");
  IElementType DOUBLE_COLON = new SquirrelTokenType("::");
  IElementType EQ = new SquirrelTokenType("==");
  IElementType FALSE = new SquirrelTokenType("false");
  IElementType FLOAT = new SquirrelTokenType("FLOAT");
  IElementType FUNCTION = new SquirrelTokenType("function");
  IElementType GREATER = new SquirrelTokenType(">");
  IElementType GREATER_OR_EQUAL = new SquirrelTokenType(">=");
  IElementType IDENTIFIER = new SquirrelTokenType("IDENTIFIER");
  IElementType IN = new SquirrelTokenType("in");
  IElementType INT = new SquirrelTokenType("INT");
  IElementType LBRACE = new SquirrelTokenType("{");
  IElementType LBRACK = new SquirrelTokenType("[");
  IElementType LESS = new SquirrelTokenType("<");
  IElementType LESS_OR_EQUAL = new SquirrelTokenType("<=");
  IElementType LINE_COMMENT = new SquirrelTokenType("LINE_COMMENT");
  IElementType LOCAL = new SquirrelTokenType("local");
  IElementType LPAREN = new SquirrelTokenType("(");
  IElementType MINUS = new SquirrelTokenType("-");
  IElementType MINUS_ASSIGN = new SquirrelTokenType("-=");
  IElementType MINUS_MINUS = new SquirrelTokenType("--");
  IElementType MUL = new SquirrelTokenType("*");
  IElementType MUL_ASSIGN = new SquirrelTokenType("*=");
  IElementType NOT = new SquirrelTokenType("!");
  IElementType NOT_EQ = new SquirrelTokenType("!=");
  IElementType NULL = new SquirrelTokenType("null");
  IElementType PLUS = new SquirrelTokenType("+");
  IElementType PLUS_ASSIGN = new SquirrelTokenType("+=");
  IElementType PLUS_PLUS = new SquirrelTokenType("++");
  IElementType QUOTIENT = new SquirrelTokenType("/");
  IElementType QUOTIENT_ASSIGN = new SquirrelTokenType("/=");
  IElementType RBRACE = new SquirrelTokenType("}");
  IElementType RBRACK = new SquirrelTokenType("]");
  IElementType REMAINDER = new SquirrelTokenType("%");
  IElementType REMAINDER_ASSIGN = new SquirrelTokenType("%=");
  IElementType RPAREN = new SquirrelTokenType(")");
  IElementType SEMICOLON = new SquirrelTokenType(";");
  IElementType SEMICOLON_SYNTHETIC = new SquirrelTokenType("<NL>");
  IElementType SEND_CHANNEL = new SquirrelTokenType("<-");
  IElementType SHIFT_LEFT = new SquirrelTokenType("<<");
  IElementType SHIFT_LEFT_ASSIGN = new SquirrelTokenType("<<=");
  IElementType SHIFT_RIGHT = new SquirrelTokenType(">>");
  IElementType SHIFT_RIGHT_ASSIGN = new SquirrelTokenType(">>=");
  IElementType STRING = new SquirrelTokenType("STRING");
  IElementType TRUE = new SquirrelTokenType("true");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == AND_EXPR) {
        return new SquirrelAndExprImpl(node);
      }
      else if (type == ASSIGN_EXPR) {
        return new SquirrelAssignExprImpl(node);
      }
      else if (type == BIT_AND_EXPR) {
        return new SquirrelBitAndExprImpl(node);
      }
      else if (type == BIT_OR_EXPR) {
        return new SquirrelBitOrExprImpl(node);
      }
      else if (type == BIT_SHIFT_EXPR) {
        return new SquirrelBitShiftExprImpl(node);
      }
      else if (type == BIT_XOR_EXPR) {
        return new SquirrelBitXorExprImpl(node);
      }
      else if (type == BLOCK) {
        return new SquirrelBlockImpl(node);
      }
      else if (type == COMMA_EXPR) {
        return new SquirrelCommaExprImpl(node);
      }
      else if (type == COMPARE_EXPR) {
        return new SquirrelCompareExprImpl(node);
      }
      else if (type == CONST_DECLARATION) {
        return new SquirrelConstDeclarationImpl(node);
      }
      else if (type == ELVIS_EXPR) {
        return new SquirrelElvisExprImpl(node);
      }
      else if (type == EXPR) {
        return new SquirrelExprImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new SquirrelFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_NAME) {
        return new SquirrelFunctionNameImpl(node);
      }
      else if (type == IDENTIFIER) {
        return new SquirrelIdentifierImpl(node);
      }
      else if (type == IN_EXPR) {
        return new SquirrelInExprImpl(node);
      }
      else if (type == LESS_GREATER_EQUAL_EXPR) {
        return new SquirrelLessGreaterEqualExprImpl(node);
      }
      else if (type == LITERAL) {
        return new SquirrelLiteralImpl(node);
      }
      else if (type == LITERAL_EXPR) {
        return new SquirrelLiteralExprImpl(node);
      }
      else if (type == LOCAL_VAR_DECLARATION) {
        return new SquirrelLocalVarDeclarationImpl(node);
      }
      else if (type == MUL_DIV_MOD_EXPR) {
        return new SquirrelMulDivModExprImpl(node);
      }
      else if (type == OR_EXPR) {
        return new SquirrelOrExprImpl(node);
      }
      else if (type == PAREN_EXPR) {
        return new SquirrelParenExprImpl(node);
      }
      else if (type == PLUS_MINUS_EXPR) {
        return new SquirrelPlusMinusExprImpl(node);
      }
      else if (type == REF_EXPR) {
        return new SquirrelRefExprImpl(node);
      }
      else if (type == STATEMENTS_WITH_SEMI) {
        return new SquirrelStatementsWithSemiImpl(node);
      }
      else if (type == STATEMENT_WITHOUT_SEMI) {
        return new SquirrelStatementWithoutSemiImpl(node);
      }
      else if (type == UNARY_EXPR) {
        return new SquirrelUnaryExprImpl(node);
      }
      else if (type == VAR_DECLARATION) {
        return new SquirrelVarDeclarationImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

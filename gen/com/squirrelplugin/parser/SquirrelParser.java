// This is a generated file. Not intended for manual editing.
package com.squirrelplugin.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.squirrelplugin.psi.SquirrelTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SquirrelParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == AND_EXPR) {
      r = Expr(b, 0, 12);
    }
    else if (t == ASSIGN_EXPR) {
      r = Expr(b, 0, 0);
    }
    else if (t == BIT_AND_EXPR) {
      r = Expr(b, 0, 5);
    }
    else if (t == BIT_OR_EXPR) {
      r = Expr(b, 0, 3);
    }
    else if (t == BIT_SHIFT_EXPR) {
      r = Expr(b, 0, 8);
    }
    else if (t == BIT_XOR_EXPR) {
      r = Expr(b, 0, 4);
    }
    else if (t == BLOCK) {
      r = Block(b, 0);
    }
    else if (t == COMMA_EXPR) {
      r = Expr(b, 0, -1);
    }
    else if (t == COMPARE_EXPR) {
      r = Expr(b, 0, 6);
    }
    else if (t == CONST_DECLARATION) {
      r = ConstDeclaration(b, 0);
    }
    else if (t == ELVIS_EXPR) {
      r = Expr(b, 0, 1);
    }
    else if (t == EXPR) {
      r = Expr(b, 0, -1);
    }
    else if (t == FUNCTION_DECLARATION) {
      r = FunctionDeclaration(b, 0);
    }
    else if (t == FUNCTION_NAME) {
      r = FunctionName(b, 0);
    }
    else if (t == IDENTIFIER) {
      r = Identifier(b, 0);
    }
    else if (t == IN_EXPR) {
      r = Expr(b, 0, 13);
    }
    else if (t == LESS_GREATER_EQUAL_EXPR) {
      r = Expr(b, 0, 7);
    }
    else if (t == LITERAL) {
      r = Literal(b, 0);
    }
    else if (t == LITERAL_EXPR) {
      r = LiteralExpr(b, 0);
    }
    else if (t == LOCAL_VAR_DECLARATION) {
      r = LocalVarDeclaration(b, 0);
    }
    else if (t == MUL_DIV_MOD_EXPR) {
      r = Expr(b, 0, 10);
    }
    else if (t == OR_EXPR) {
      r = Expr(b, 0, 2);
    }
    else if (t == PAREN_EXPR) {
      r = ParenExpr(b, 0);
    }
    else if (t == PLUS_MINUS_EXPR) {
      r = Expr(b, 0, 9);
    }
    else if (t == REF_EXPR) {
      r = Expr(b, 0, 11);
    }
    else if (t == STATEMENT_WITHOUT_SEMI) {
      r = StatementWithoutSemi(b, 0);
    }
    else if (t == STATEMENTS_WITH_SEMI) {
      r = StatementsWithSemi(b, 0);
    }
    else if (t == UNARY_EXPR) {
      r = UnaryExpr(b, 0);
    }
    else if (t == VAR_DECLARATION) {
      r = VarDeclaration(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return SquirrelFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(AND_EXPR, ASSIGN_EXPR, BIT_AND_EXPR, BIT_OR_EXPR,
      BIT_SHIFT_EXPR, BIT_XOR_EXPR, COMMA_EXPR, COMPARE_EXPR,
      ELVIS_EXPR, EXPR, IN_EXPR, LESS_GREATER_EQUAL_EXPR,
      LITERAL_EXPR, MUL_DIV_MOD_EXPR, OR_EXPR, PAREN_EXPR,
      PLUS_MINUS_EXPR, REF_EXPR, UNARY_EXPR),
  };

  /* ********************************************************** */
  // LBRACE (RBRACE | StatementList RBRACE)
  public static boolean Block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && Block_1(b, l + 1);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  // RBRACE | StatementList RBRACE
  private static boolean Block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = Block_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // StatementList RBRACE
  private static boolean Block_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StatementList(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // const IDENTIFIER ASSIGN Literal
  public static boolean ConstDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CONST, IDENTIFIER, ASSIGN);
    r = r && Literal(b, l + 1);
    exit_section_(b, m, CONST_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // function FunctionName Block?
  public static boolean FunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration")) return false;
    if (!nextTokenIs(b, FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FUNCTION);
    r = r && FunctionName(b, l + 1);
    r = r && FunctionDeclaration_2(b, l + 1);
    exit_section_(b, m, FUNCTION_DECLARATION, r);
    return r;
  }

  // Block?
  private static boolean FunctionDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_2")) return false;
    Block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER (DOUBLE_COLON IDENTIFIER)?
  public static boolean FunctionName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && FunctionName_1(b, l + 1);
    exit_section_(b, m, FUNCTION_NAME, r);
    return r;
  }

  // (DOUBLE_COLON IDENTIFIER)?
  private static boolean FunctionName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionName_1")) return false;
    FunctionName_1_0(b, l + 1);
    return true;
  }

  // DOUBLE_COLON IDENTIFIER
  private static boolean FunctionName_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionName_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOUBLE_COLON, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean Identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Identifier")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // INT
  //   | FLOAT
  //   | STRING
  //   | true | false
  //   | null
  public static boolean Literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<literal>");
    r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NULL);
    exit_section_(b, l, m, LITERAL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // local VarDeclaration
  public static boolean LocalVarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LocalVarDeclaration")) return false;
    if (!nextTokenIs(b, LOCAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOCAL);
    r = r && VarDeclaration(b, l + 1);
    exit_section_(b, m, LOCAL_VAR_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // StatementsWithSemi | StatementWithoutSemi
  static boolean SingleStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SingleStatement")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StatementsWithSemi(b, l + 1);
    if (!r) r = StatementWithoutSemi(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // StatementList <<eof>>
  static boolean SquirrelFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SquirrelFile")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = StatementList(b, l + 1);
    p = r; // pin = 1
    r = r && eof(b, l + 1);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (StatementWithoutSemi semi* StatementList | StatementsWithSemi semi+ StatementList | SingleStatement) semi*
  static boolean StatementList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StatementList_0(b, l + 1);
    r = r && StatementList_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // StatementWithoutSemi semi* StatementList | StatementsWithSemi semi+ StatementList | SingleStatement
  private static boolean StatementList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StatementList_0_0(b, l + 1);
    if (!r) r = StatementList_0_1(b, l + 1);
    if (!r) r = SingleStatement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // StatementWithoutSemi semi* StatementList
  private static boolean StatementList_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StatementWithoutSemi(b, l + 1);
    r = r && StatementList_0_0_1(b, l + 1);
    r = r && StatementList(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi*
  private static boolean StatementList_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList_0_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!semi(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StatementList_0_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // StatementsWithSemi semi+ StatementList
  private static boolean StatementList_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StatementsWithSemi(b, l + 1);
    r = r && StatementList_0_1_1(b, l + 1);
    r = r && StatementList(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi+
  private static boolean StatementList_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!semi(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StatementList_0_1_1", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // semi*
  private static boolean StatementList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementList_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!semi(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StatementList_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // LINE_COMMENT
  //     | BLOCK_COMMENT
  //     | FunctionDeclaration
  //     | Block
  public static boolean StatementWithoutSemi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementWithoutSemi")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<statement without semi>");
    r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    if (!r) r = FunctionDeclaration(b, l + 1);
    if (!r) r = Block(b, l + 1);
    exit_section_(b, l, m, STATEMENT_WITHOUT_SEMI, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Expr
  //     | ConstDeclaration
  //     | LocalVarDeclaration
  public static boolean StatementsWithSemi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementsWithSemi")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<statements with semi>");
    r = Expr(b, l + 1, -1);
    if (!r) r = ConstDeclaration(b, l + 1);
    if (!r) r = LocalVarDeclaration(b, l + 1);
    exit_section_(b, l, m, STATEMENTS_WITH_SEMI, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER [ ASSIGN Expr ][ COMMA VarDeclaration ]
  public static boolean VarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && VarDeclaration_1(b, l + 1);
    r = r && VarDeclaration_2(b, l + 1);
    exit_section_(b, m, VAR_DECLARATION, r);
    return r;
  }

  // [ ASSIGN Expr ]
  private static boolean VarDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration_1")) return false;
    VarDeclaration_1_0(b, l + 1);
    return true;
  }

  // ASSIGN Expr
  private static boolean VarDeclaration_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && Expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ COMMA VarDeclaration ]
  private static boolean VarDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration_2")) return false;
    VarDeclaration_2_0(b, l + 1);
    return true;
  }

  // COMMA VarDeclaration
  private static boolean VarDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && VarDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SEMICOLON_SYNTHETIC | SEMICOLON
  static boolean semi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semi")) return false;
    if (!nextTokenIs(b, "", SEMICOLON, SEMICOLON_SYNTHETIC)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON_SYNTHETIC);
    if (!r) r = consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: Expr
  // Operator priority table:
  // 0: BINARY(CommaExpr)
  // 1: BINARY(AssignExpr)
  // 2: BINARY(ElvisExpr)
  // 3: BINARY(OrExpr)
  // 4: BINARY(BitOrExpr)
  // 5: BINARY(BitXorExpr)
  // 6: BINARY(BitAndExpr)
  // 7: BINARY(CompareExpr)
  // 8: BINARY(LessGreaterEqualExpr)
  // 9: BINARY(BitShiftExpr)
  // 10: BINARY(PlusMinusExpr)
  // 11: BINARY(MulDivModExpr)
  // 12: POSTFIX(RefExpr)
  // 13: BINARY(AndExpr)
  // 14: BINARY(InExpr)
  // 15: PREFIX(UnaryExpr)
  // 16: ATOM(SimpleRefExpr)
  // 17: ATOM(LiteralExpr)
  // 18: PREFIX(ParenExpr)
  public static boolean Expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expr")) return false;
    addVariant(b, "<expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = UnaryExpr(b, l + 1);
    if (!r) r = SimpleRefExpr(b, l + 1);
    if (!r) r = LiteralExpr(b, l + 1);
    if (!r) r = ParenExpr(b, l + 1);
    p = r;
    r = r && Expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean Expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && consumeTokenSmart(b, COMMA)) {
        r = Expr(b, l, 0);
        exit_section_(b, l, m, COMMA_EXPR, r, true, null);
      }
      else if (g < 1 && AssignExpr_0(b, l + 1)) {
        r = Expr(b, l, 0);
        exit_section_(b, l, m, ASSIGN_EXPR, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, "?")) {
        r = report_error_(b, Expr(b, l, 2));
        r = ElvisExpr_1(b, l + 1) && r;
        exit_section_(b, l, m, ELVIS_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, COND_OR)) {
        r = Expr(b, l, 3);
        exit_section_(b, l, m, OR_EXPR, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, BIT_OR)) {
        r = Expr(b, l, 4);
        exit_section_(b, l, m, BIT_OR_EXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, BIT_XOR)) {
        r = Expr(b, l, 5);
        exit_section_(b, l, m, BIT_XOR_EXPR, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, BIT_AND)) {
        r = Expr(b, l, 6);
        exit_section_(b, l, m, BIT_AND_EXPR, r, true, null);
      }
      else if (g < 7 && CompareExpr_0(b, l + 1)) {
        r = Expr(b, l, 7);
        exit_section_(b, l, m, COMPARE_EXPR, r, true, null);
      }
      else if (g < 8 && LessGreaterEqualExpr_0(b, l + 1)) {
        r = Expr(b, l, 8);
        exit_section_(b, l, m, LESS_GREATER_EQUAL_EXPR, r, true, null);
      }
      else if (g < 9 && BitShiftExpr_0(b, l + 1)) {
        r = Expr(b, l, 9);
        exit_section_(b, l, m, BIT_SHIFT_EXPR, r, true, null);
      }
      else if (g < 10 && PlusMinusExpr_0(b, l + 1)) {
        r = Expr(b, l, 10);
        exit_section_(b, l, m, PLUS_MINUS_EXPR, r, true, null);
      }
      else if (g < 11 && MulDivModExpr_0(b, l + 1)) {
        r = Expr(b, l, 11);
        exit_section_(b, l, m, MUL_DIV_MOD_EXPR, r, true, null);
      }
      else if (g < 12 && RefExpr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, REF_EXPR, r, true, null);
      }
      else if (g < 13 && consumeTokenSmart(b, COND_AND)) {
        r = Expr(b, l, 13);
        exit_section_(b, l, m, AND_EXPR, r, true, null);
      }
      else if (g < 14 && consumeTokenSmart(b, IN)) {
        r = Expr(b, l, 14);
        exit_section_(b, l, m, IN_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // '=' | '<-' | '*=' | '/=' | '%=' | '+=' | '-='
  private static boolean AssignExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ASSIGN);
    if (!r) r = consumeTokenSmart(b, SEND_CHANNEL);
    if (!r) r = consumeTokenSmart(b, MUL_ASSIGN);
    if (!r) r = consumeTokenSmart(b, QUOTIENT_ASSIGN);
    if (!r) r = consumeTokenSmart(b, REMAINDER_ASSIGN);
    if (!r) r = consumeTokenSmart(b, PLUS_ASSIGN);
    if (!r) r = consumeTokenSmart(b, MINUS_ASSIGN);
    exit_section_(b, m, null, r);
    return r;
  }

  // ':' Expr
  private static boolean ElvisExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElvisExpr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && Expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '==' | '!=' | '<=>'
  private static boolean CompareExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompareExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    if (!r) r = consumeTokenSmart(b, NOT_EQ);
    if (!r) r = consumeTokenSmart(b, "<=>");
    exit_section_(b, m, null, r);
    return r;
  }

  // '<' | '<=' | '>' | '>='
  private static boolean LessGreaterEqualExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LessGreaterEqualExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LESS);
    if (!r) r = consumeTokenSmart(b, LESS_OR_EQUAL);
    if (!r) r = consumeTokenSmart(b, GREATER);
    if (!r) r = consumeTokenSmart(b, GREATER_OR_EQUAL);
    exit_section_(b, m, null, r);
    return r;
  }

  // '<<' | '>>' | '>>>'
  private static boolean BitShiftExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BitShiftExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SHIFT_LEFT);
    if (!r) r = consumeTokenSmart(b, SHIFT_RIGHT);
    if (!r) r = consumeTokenSmart(b, ">>>");
    exit_section_(b, m, null, r);
    return r;
  }

  // '+' | '-'
  private static boolean PlusMinusExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlusMinusExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PLUS);
    if (!r) r = consumeTokenSmart(b, MINUS);
    exit_section_(b, m, null, r);
    return r;
  }

  // '*' | '/' | '%'
  private static boolean MulDivModExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MulDivModExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, MUL);
    if (!r) r = consumeTokenSmart(b, QUOTIENT);
    if (!r) r = consumeTokenSmart(b, REMAINDER);
    exit_section_(b, m, null, r);
    return r;
  }

  // '.' Identifier
  private static boolean RefExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RefExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ".");
    r = r && Identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean UnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryExpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = UnaryExpr_0(b, l + 1);
    p = r;
    r = p && Expr(b, l, 15);
    exit_section_(b, l, m, UNARY_EXPR, r, p, null);
    return r || p;
  }

  // '-' | '+' | '!' | '~' | 'typeof'
  private static boolean UnaryExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, MINUS);
    if (!r) r = consumeTokenSmart(b, PLUS);
    if (!r) r = consumeTokenSmart(b, NOT);
    if (!r) r = consumeTokenSmart(b, "~");
    if (!r) r = consumeTokenSmart(b, "typeof");
    exit_section_(b, m, null, r);
    return r;
  }

  // Identifier
  public static boolean SimpleRefExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleRefExpr")) return false;
    if (!nextTokenIsFast(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Identifier(b, l + 1);
    exit_section_(b, m, REF_EXPR, r);
    return r;
  }

  // Literal
  public static boolean LiteralExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralExpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<literal expr>");
    r = Literal(b, l + 1);
    exit_section_(b, l, m, LITERAL_EXPR, r, false, null);
    return r;
  }

  public static boolean ParenExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParenExpr")) return false;
    if (!nextTokenIsFast(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && Expr(b, l, -1);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PAREN_EXPR, r, p, null);
    return r || p;
  }

}

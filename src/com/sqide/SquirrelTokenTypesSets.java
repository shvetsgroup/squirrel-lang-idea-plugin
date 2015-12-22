package com.sqide;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.*;
import com.sqide.lexer.SquirrelDocLexer;
import com.sqide.psi.SquirrelElementType;

import static com.sqide.SquirrelTokenTypes.*;

public interface SquirrelTokenTypesSets {
    IFileElementType SQUIRREL_FILE = new IFileElementType("SQUIRRELFILE", SquirrelLanguage.INSTANCE);

    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    // SquirrelLexer returns multiline comments as a single MULTI_LINE_COMMENT or MULTI_LINE_DOC_COMMENT
    // SquirrelDocLexer splits MULTI_LINE_DOC_COMMENT in tokens

    // can't appear in PSI because merged into MULTI_LINE_COMMENT
    IElementType MULTI_LINE_COMMENT_START = new SquirrelElementType("MULTI_LINE_COMMENT_START");
    IElementType MULTI_LINE_DOC_COMMENT_START = new SquirrelElementType("MULTI_LINE_DOC_COMMENT_START");
    IElementType MULTI_LINE_COMMENT_BODY = new SquirrelElementType("MULTI_LINE_COMMENT_BODY");
    IElementType DOC_COMMENT_LEADING_ASTERISK = new SquirrelElementType("DOC_COMMENT_LEADING_ASTERISK");
    IElementType MULTI_LINE_COMMENT_END = new SquirrelElementType("MULTI_LINE_COMMENT_END");

    IElementType MULTI_LINE_DOC_COMMENT = new SquirrelDocCommentElementType();

    TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE, WS, NL);

    TokenSet COMMENTS = TokenSet.create(SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, MULTI_LINE_DOC_COMMENT);
    TokenSet SEMICOLONS = TokenSet.create(SEMICOLON, SEMICOLON_SYNTHETIC);

    TokenSet NUMBERS = TokenSet.create(INT, FLOAT);
    TokenSet STRING_LITERALS = TokenSet.create(STRING);

    TokenSet KEYWORDS = TokenSet.create(CONST, ENUM, LOCAL, FUNCTION, CONSTRUCTOR, CLASS, EXTENDS, STATIC, BREAK, CONTINUE, RETURN, YIELD, THROW, FOR, FOREACH, IN, WHILE, DO, IF, ELSE, SWITCH, CASE, DEFAULT, TRY, CATCH, TYPEOF, CLONE, DELETE, RESUME, INSTANCEOF, TRUE, FALSE, NULL);
    TokenSet OPERATORS = TokenSet.create(RBRACE, RBRACKET, RPAREN, PLUS_PLUS, MINUS_MINUS, LBRACE, LBRACKET, LPAREN, DOUBLE_COLON, COLON, SEMICOLON, COMMA, MULTI_ARGS, CLASS_ATTR_START, CLASS_ATTR_END, SHIFT_LEFT, SHIFT_RIGHT, UNSIGNED_SHIFT_RIGHT, CMP, EQ_EQ, NOT_EQ, LESS_OR_EQ, GREATER_OR_EQ, SEND_CHANNEL, PLUS_EQ, MINUS_EQ, MUL_EQ, DIV_EQ, REMAINDER_EQ, OR_OR, AND_AND, EQ, NOT, BIT_NOT, BIT_OR, BIT_XOR, BIT_AND, LESS, GREATER, PLUS, MINUS, MUL, DIV, REMAINDER, QUESTION, AT, DOT);

    TokenSet EXPRESSIONS = TokenSet
            .create(ADDITIVE_EXPRESSION, ARRAY_EXPRESSION, ARRAY_ITEM_EXPRESSION, ASSIGN_EXPRESSION, BITWISE_AND_EXPRESSION, BITWISE_OR_EXPRESSION, BITWISE_XOR_EXPRESSION, CALL_EXPRESSION, CLASS_EXPRESSION, COMMA_EXPRESSION, COMPARE_EXPRESSION, FUNCTION_EXPRESSION, INSTANCE_OF_EXPRESSION, IN_EXPRESSION, LAMBDA_FUNCTION_EXPRESSION, LITERAL_EXPRESSION, LOGIC_AND_EXPRESSION, LOGIC_OR_EXPRESSION, MULTIPLICATIVE_EXPRESSION, PARENTHESIZED_EXPRESSION, PREFIX_EXPRESSION, REFERENCE_EXPRESSION, RELATIONAL_EXPRESSION, SHIFT_EXPRESSION, TABLE_EXPRESSION, TERNARY_EXPRESSION, UNARY_EXPRESSION);

    TokenSet BINARY_EXPRESSIONS = TokenSet.create(
            COMMA_EXPRESSION,
            ASSIGN_EXPRESSION,
            LOGIC_OR_EXPRESSION,
            LOGIC_AND_EXPRESSION,
            IN_EXPRESSION,
            BITWISE_AND_EXPRESSION,
            BITWISE_OR_EXPRESSION,
            BITWISE_XOR_EXPRESSION,
            COMPARE_EXPRESSION,
            RELATIONAL_EXPRESSION,
            SHIFT_EXPRESSION,
            ADDITIVE_EXPRESSION,
            MULTIPLICATIVE_EXPRESSION
    );

    TokenSet LOGIC_OPERATORS = TokenSet.create(
            AND_AND, OR_OR
    );

    TokenSet BITWISE_OPERATORS = TokenSet.create(
            BIT_AND, BIT_OR, BIT_XOR, BIT_NOT
    );

    TokenSet STATEMENTS = TokenSet.create(
            BLOCK,
            EXPRESSION_STATEMENT,
            CONST_DECLARATION,
            ENUM_DECLARATION,
            LOCAL_DECLARATION,
            FUNCTION_DECLARATION,
            CLASS_DECLARATION,
            FOR_STATEMENT,
            FOREACH_STATEMENT,
            WHILE_STATEMENT,
            DO_WHILE_STATEMENT,
            IF_STATEMENT,
            SWITCH_STATEMENT,
            TRY_STATEMENT,
            RETURN_STATEMENT,
            BREAK_STATEMENT,
            CONTINUE_STATEMENT,
            YIELD_STATEMENT,
            THROW_STATEMENT
    );


    TokenSet DECLARATIONS = TokenSet.create(
            CLASS_DECLARATION,
            FUNCTION_DECLARATION,
            CONSTRUCTOR_DECLARATION,
            METHOD_DECLARATION,
            VAR_DECLARATION_LIST
    );

    TokenSet FUNCTION_DEFINITION = TokenSet.create(
            FUNCTION_DECLARATION,
            CONSTRUCTOR_DECLARATION,
            METHOD_DECLARATION
    );

    TokenSet BLOCKS = TokenSet.create(
            BLOCK,
            CLASS_BODY,
            SQUIRREL_FILE
    );
    TokenSet BLOCKS_WITH_BRACES = TokenSet.create(
            BLOCK,
            CLASS_BODY,
            SWITCH_STATEMENT,
            ENUM_DECLARATION,
            TABLE_EXPRESSION
    );

    TokenSet DOC_COMMENT_CONTENTS =
            TokenSet.create(MULTI_LINE_DOC_COMMENT_START, MULTI_LINE_COMMENT_BODY, DOC_COMMENT_LEADING_ASTERISK, MULTI_LINE_COMMENT_END);


    class SquirrelDocCommentElementType extends ILazyParseableElementType {
        public SquirrelDocCommentElementType() {
            super("MULTI_LINE_DOC_COMMENT", SquirrelLanguage.INSTANCE);
        }

        @Override
        public ASTNode parseContents(final ASTNode chameleon) {
            final PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(chameleon.getTreeParent().getPsi().getProject(),
                    chameleon,
                    new SquirrelDocLexer(),
                    getLanguage(),
                    chameleon.getChars());
            doParse(builder);
            return builder.getTreeBuilt().getFirstChildNode();
        }

        private void doParse(final PsiBuilder builder) {
            final PsiBuilder.Marker root = builder.mark();

            while (!builder.eof()) {
                builder.advanceLexer();
            }

            root.done(this);
        }
    }
}

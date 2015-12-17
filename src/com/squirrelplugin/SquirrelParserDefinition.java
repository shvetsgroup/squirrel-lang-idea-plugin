package com.squirrelplugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.squirrelplugin.lexer.SquirrelLexer;
import com.squirrelplugin.psi.SquirrelFile;
import com.squirrelplugin.psi.impl.SquirrelDocCommentImpl;
import org.jetbrains.annotations.NotNull;

public class SquirrelParserDefinition implements ParserDefinition {

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new SquirrelLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new SquirrelParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return SquirrelTokenTypesSets.SQUIRREL_FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return SquirrelTokenTypesSets.WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return SquirrelTokenTypesSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return SquirrelTokenTypesSets.STRING_LITERALS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        if (node.getElementType() == SquirrelTokenTypesSets.MULTI_LINE_DOC_COMMENT) {
            return new SquirrelDocCommentImpl(node);
        }
        return SquirrelTokenTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SquirrelFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
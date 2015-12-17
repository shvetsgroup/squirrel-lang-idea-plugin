package com.squirrelplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.psi.SquirrelDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelDocCommentImpl extends ASTWrapperPsiElement implements SquirrelDocComment {

    public SquirrelDocCommentImpl(@NotNull final ASTNode node) {
        super(node);
    }

    @Nullable
    public PsiElement getOwner() {
        return null;
    }

    public IElementType getTokenType() {
        return getNode().getElementType();
    }
}
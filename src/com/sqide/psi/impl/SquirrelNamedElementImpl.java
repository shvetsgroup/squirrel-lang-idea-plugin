package com.sqide.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.sqide.psi.SquirrelNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class SquirrelNamedElementImpl extends ASTWrapperPsiElement implements SquirrelNamedElement {
    public SquirrelNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
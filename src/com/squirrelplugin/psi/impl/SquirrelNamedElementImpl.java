package com.squirrelplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.squirrelplugin.psi.SquirrelNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class SquirrelNamedElementImpl extends ASTWrapperPsiElement implements SquirrelNamedElement {
    public SquirrelNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
package com.sqide.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.sqide.psi.SquirrelClass;
import org.jetbrains.annotations.NotNull;

public abstract class SquirrelClassImpl extends ASTWrapperPsiElement implements SquirrelClass {
    public SquirrelClassImpl(@NotNull ASTNode node) {
        super(node);
    }
}
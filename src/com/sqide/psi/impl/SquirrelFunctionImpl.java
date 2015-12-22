package com.sqide.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.sqide.psi.SquirrelFunction;
import org.jetbrains.annotations.NotNull;

public abstract class SquirrelFunctionImpl extends ASTWrapperPsiElement implements SquirrelFunction {
    public SquirrelFunctionImpl(@NotNull ASTNode node) {
        super(node);
    }
}
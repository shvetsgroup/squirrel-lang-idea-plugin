package com.squirrelplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.squirrelplugin.psi.SquirrelFunction;
import org.jetbrains.annotations.NotNull;

public abstract class SquirrelFunctionImpl extends ASTWrapperPsiElement implements SquirrelFunction {
    public SquirrelFunctionImpl(@NotNull ASTNode node) {
        super(node);
    }
}
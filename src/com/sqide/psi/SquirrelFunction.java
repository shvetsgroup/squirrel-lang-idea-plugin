package com.sqide.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface SquirrelFunction extends PsiElement {

    @Nullable
    SquirrelFunctionBody getFunctionBody();
}
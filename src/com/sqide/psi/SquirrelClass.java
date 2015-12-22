package com.sqide.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface SquirrelClass extends PsiElement {

    @Nullable
    SquirrelClassBody getClassBody();

}
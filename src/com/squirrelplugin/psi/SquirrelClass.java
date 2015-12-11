package com.squirrelplugin.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.Nullable;

public interface SquirrelClass extends PsiElement {

    @Nullable
    SquirrelClassBody getClassBody();

}
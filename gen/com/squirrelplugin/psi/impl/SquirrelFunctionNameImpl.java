// This is a generated file. Not intended for manual editing.
package com.squirrelplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.squirrelplugin.psi.SquirrelTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.squirrelplugin.psi.*;

public class SquirrelFunctionNameImpl extends ASTWrapperPsiElement implements SquirrelFunctionName {

  public SquirrelFunctionNameImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SquirrelVisitor) ((SquirrelVisitor)visitor).visitFunctionName(this);
    else super.accept(visitor);
  }

}

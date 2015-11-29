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

public class SquirrelStatementsWithSemiImpl extends ASTWrapperPsiElement implements SquirrelStatementsWithSemi {

  public SquirrelStatementsWithSemiImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SquirrelVisitor) ((SquirrelVisitor)visitor).visitStatementsWithSemi(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SquirrelConstDeclaration getConstDeclaration() {
    return findChildByClass(SquirrelConstDeclaration.class);
  }

  @Override
  @Nullable
  public SquirrelExpr getExpr() {
    return findChildByClass(SquirrelExpr.class);
  }

  @Override
  @Nullable
  public SquirrelLocalVarDeclaration getLocalVarDeclaration() {
    return findChildByClass(SquirrelLocalVarDeclaration.class);
  }

}

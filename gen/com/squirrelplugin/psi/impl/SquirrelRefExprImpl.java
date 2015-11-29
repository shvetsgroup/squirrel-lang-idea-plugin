// This is a generated file. Not intended for manual editing.
package com.squirrelplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.squirrelplugin.psi.SquirrelTypes.*;
import com.squirrelplugin.psi.*;

public class SquirrelRefExprImpl extends SquirrelExprImpl implements SquirrelRefExpr {

  public SquirrelRefExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SquirrelVisitor) ((SquirrelVisitor)visitor).visitRefExpr(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SquirrelExpr getExpr() {
    return findNotNullChildByClass(SquirrelExpr.class);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return findNotNullChildByType(IDENTIFIER);
  }

}

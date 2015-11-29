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

public class SquirrelBlockImpl extends ASTWrapperPsiElement implements SquirrelBlock {

  public SquirrelBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SquirrelVisitor) ((SquirrelVisitor)visitor).visitBlock(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SquirrelStatementWithoutSemi> getStatementWithoutSemiList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SquirrelStatementWithoutSemi.class);
  }

  @Override
  @NotNull
  public List<SquirrelStatementsWithSemi> getStatementsWithSemiList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SquirrelStatementsWithSemi.class);
  }

}

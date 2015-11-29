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

public class SquirrelStatementWithoutSemiImpl extends ASTWrapperPsiElement implements SquirrelStatementWithoutSemi {

  public SquirrelStatementWithoutSemiImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SquirrelVisitor) ((SquirrelVisitor)visitor).visitStatementWithoutSemi(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SquirrelBlock getBlock() {
    return findChildByClass(SquirrelBlock.class);
  }

  @Override
  @Nullable
  public SquirrelFunctionDeclaration getFunctionDeclaration() {
    return findChildByClass(SquirrelFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public PsiElement getBlockComment() {
    return findChildByType(BLOCK_COMMENT);
  }

  @Override
  @Nullable
  public PsiElement getLineComment() {
    return findChildByType(LINE_COMMENT);
  }

}

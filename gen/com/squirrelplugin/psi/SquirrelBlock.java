// This is a generated file. Not intended for manual editing.
package com.squirrelplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SquirrelBlock extends PsiElement {

  @NotNull
  List<SquirrelStatementWithoutSemi> getStatementWithoutSemiList();

  @NotNull
  List<SquirrelStatementsWithSemi> getStatementsWithSemiList();

}

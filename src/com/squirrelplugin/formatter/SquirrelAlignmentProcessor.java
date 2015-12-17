package com.squirrelplugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.formatter.settings.SquirrelCodeStyleSettings;
import org.jetbrains.annotations.Nullable;

import static com.squirrelplugin.SquirrelTokenTypes.*;
import static com.squirrelplugin.SquirrelTokenTypesSets.BINARY_EXPRESSIONS;


public class SquirrelAlignmentProcessor {
  @Nullable
  public static Alignment createChildAlignment(ASTNode child, ASTNode myNode, CommonCodeStyleSettings cmSettings, SquirrelCodeStyleSettings sqSettings) {
    IElementType elementType = myNode.getElementType();
    Alignment myBaseAlignment = Alignment.createAlignment();

    if (BINARY_EXPRESSIONS.contains(elementType) && cmSettings.ALIGN_MULTILINE_BINARY_OPERATION) {
      return myBaseAlignment;
    }

    if (elementType == TERNARY_EXPRESSION && cmSettings.ALIGN_MULTILINE_TERNARY_OPERATION) {
      return myBaseAlignment;
    }

    if (elementType == PARAMETER_LIST) {
      if (cmSettings.ALIGN_MULTILINE_PARAMETERS) {
        return myBaseAlignment;
      }
    }
    if (elementType == ARGUMENTS) {
      if (cmSettings.ALIGN_MULTILINE_PARAMETERS_IN_CALLS) {
        return myBaseAlignment;
      }
    }

    return null;
  }
}

package com.squirrelplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.squirrelplugin.SquirrelLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SquirrelElementType extends IElementType {
    public SquirrelElementType(@NotNull @NonNls String debugName) {
        super(debugName, SquirrelLanguage.INSTANCE);
    }
}
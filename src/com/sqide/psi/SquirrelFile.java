package com.sqide.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.sqide.SquirrelFileType;
import com.sqide.SquirrelLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SquirrelFile extends PsiFileBase {
    public SquirrelFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SquirrelLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SquirrelFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Squirrel File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
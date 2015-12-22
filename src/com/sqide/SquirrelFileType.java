package com.sqide;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SquirrelFileType extends LanguageFileType {
    public static final SquirrelFileType INSTANCE = new SquirrelFileType();

    public static final String EXTENSION = "nut";

    private SquirrelFileType() {
        super(SquirrelLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Squirrel file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Squirrel language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SquirrelIcons.NUT_FILE;
    }
}
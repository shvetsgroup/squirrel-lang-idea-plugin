package com.squirrelplugin;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SquirrelModuleType extends ModuleType<SquirrelModuleBuilder> {
    public static final String MODULE_TYPE_ID = "SQUIRREL_MODULE";

    public SquirrelModuleType() {
        super(MODULE_TYPE_ID);
    }

    @NotNull
    public static SquirrelModuleType getInstance() {
        return (SquirrelModuleType)ModuleTypeManager.getInstance().findByID(MODULE_TYPE_ID);
    }

    @NotNull
    @Override
    public SquirrelModuleBuilder createModuleBuilder() {
        return new SquirrelModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "Squirrel Module";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Squirrel modules are used for developing <b>Squirrel</b> applications.";
    }

    @Nullable
    @Override
    public Icon getBigIcon() {
        return SquirrelIcons.SQUIRREL;
    }

    @Nullable
    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return SquirrelIcons.NUT_FILE;
    }
}
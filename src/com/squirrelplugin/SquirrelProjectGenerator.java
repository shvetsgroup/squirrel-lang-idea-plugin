package com.squirrelplugin;

import com.intellij.facet.ui.ValidationResult;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.DirectoryProjectGenerator;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SquirrelProjectGenerator implements DirectoryProjectGenerator {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return SquirrelBundle.message("squirrel.title");
    }

    @Nullable
    @Override
    public Object showGenerationSettings(VirtualFile baseDir) throws ProcessCanceledException {
        return null;
    }

    @Nullable
    @Override
    public Icon getLogo() {
        return SquirrelIcons.SQUIRREL;
    }

    @Override
    public void generateProject(@NotNull Project project, @NotNull VirtualFile baseDir, @Nullable Object settings, @NotNull Module module) {
        ApplicationManager.getApplication().runWriteAction(
                new Runnable() {
                    public void run() {
                        final ModifiableRootModel modifiableModel = ModifiableModelsProvider.SERVICE.getInstance().getModuleModifiableModel(module);
                        SquirrelModuleBuilder.setupProject(modifiableModel, baseDir);
                        ModifiableModelsProvider.SERVICE.getInstance().commitModuleModifiableModel(modifiableModel);
                    }
                });
    }

    @NotNull
    @Override
    public ValidationResult validate(@NotNull String baseDirPath) {
        return ValidationResult.OK;
    }
}

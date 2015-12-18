package com.squirrelplugin.projectWizard;

import com.intellij.ide.util.projectWizard.WebProjectTemplate;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VirtualFile;
import com.squirrelplugin.SquirrelBundle;
import com.squirrelplugin.SquirrelIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SquirrelProjectGenerator extends WebProjectTemplate<Object> implements Comparable<com.squirrelplugin.projectWizard.SquirrelProjectGenerator> {

    @NotNull
    public final String getName() {
        return SquirrelBundle.message("squirrel.title");
    }

    @NotNull
    public final String getDescription() {
        return SquirrelBundle.message("squirrel.project.description");
    }

    @Override
    public void generateProject(@NotNull Project project, @NotNull VirtualFile baseDir, @NotNull Object settings, @NotNull Module module) {
        ApplicationManager.getApplication().runWriteAction(
                new Runnable() {
                    public void run() {
                        final ModifiableRootModel modifiableModel = ModifiableModelsProvider.SERVICE.getInstance().getModuleModifiableModel(module);
                        SquirrelModuleBuilder.setupProject(modifiableModel, baseDir);
                        ModifiableModelsProvider.SERVICE.getInstance().commitModuleModifiableModel(modifiableModel);
                    }
                });
    }

    public Icon getLogo() {
        return SquirrelIcons.SQUIRREL; // todo replace with project icon
    }

    @NotNull
    @Override
    public GeneratorPeer<Object> createPeer() {
        return new SquirrelGeneratorPeer();
    }

    @Override
    public int compareTo(@NotNull final com.squirrelplugin.projectWizard.SquirrelProjectGenerator generator) {
        return getName().compareTo(generator.getName());
    }
}

package com.sqide;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.sqide.sdk.SquirrelSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class SquirrelModuleBuilder extends ModuleBuilder {

    @Override
    public String getName() {
        return SquirrelBundle.message("squirrel.title");
    }

    @Override
    public String getPresentableName() {
        return SquirrelBundle.message("squirrel.title");
    }

    @Override
    public String getDescription() {
        return SquirrelBundle.message("squirrel.project.description");
    }

    @Override
    public Icon getNodeIcon() {
        return SquirrelIcons.SQUIRREL;
    }

    @Override
    public ModuleType getModuleType() {
        return SquirrelModuleType.getInstance();
    }

    @Override
    public String getParentGroup() {
        return SquirrelModuleType.MODULE_TYPE_ID;
    }

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(final WizardContext context, final Disposable parentDisposable) {
        return null;
    }

    @Override
    public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof SquirrelSdkType;
    }

    @Override
    public void setupRootModel(final ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        final ContentEntry contentEntry = doAddContentEntry(modifiableRootModel);
        final VirtualFile baseDir = contentEntry == null ? null : contentEntry.getFile();
        if (baseDir != null) {
            setupProject(modifiableRootModel, baseDir);
        }
    }

    static void setupProject(@NotNull final ModifiableRootModel modifiableRootModel, @NotNull final VirtualFile baseDir) {
        try {
            final VirtualFile mainFile = baseDir.createChildData(null, modifiableRootModel.getModule().getName().toLowerCase(Locale.US) + ".nut");
            mainFile.setBinaryContent(("function main() {\n" +
                    "  ::print(\"Hello, World!\");\n" +
                    "}\n\n" +
                    "main();").getBytes(Charset.forName("UTF-8")));
            scheduleFilesOpeningAndPubGet(modifiableRootModel.getModule(), Arrays.asList(mainFile));
            // TODO setup run configuration
        } catch (IOException ignore) {/*unlucky*/}
    }

    private static void scheduleFilesOpeningAndPubGet(@NotNull final Module module, @NotNull final Collection<VirtualFile> files) {
        runWhenNonModalIfModuleNotDisposed(new Runnable() {
            public void run() {
                final FileEditorManager manager = FileEditorManager.getInstance(module.getProject());
                for (VirtualFile file : files) {
                    manager.openFile(file, true);
                }
            }
        }, module);
    }

    static void runWhenNonModalIfModuleNotDisposed(@NotNull final Runnable runnable, @NotNull final Module module) {
        StartupManager.getInstance(module.getProject()).runWhenProjectIsInitialized(new Runnable() {
            @Override
            public void run() {
                if (ApplicationManager.getApplication().getCurrentModalityState() == ModalityState.NON_MODAL) {
                    runnable.run();
                } else {
                    ApplicationManager.getApplication().invokeLater(runnable, ModalityState.NON_MODAL, new Condition() {
                        @Override
                        public boolean value(final Object o) {
                            return module.isDisposed();
                        }
                    });
                }
            }
        });
    }
}

package com.sqide;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectJdkForModuleStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.sqide.sdk.SquirrelSdkType;
import com.sqide.util.SquirrelConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SquirrelModuleType extends ModuleType<SquirrelModuleBuilder> {
    public static final String MODULE_TYPE_ID = SquirrelConstants.MODULE_TYPE_ID;

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
    public Icon getNodeIcon(boolean isOpened) {
        return SquirrelIcons.NUT_FILE;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull final SquirrelModuleBuilder moduleBuilder,
                                                @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{new ProjectJdkForModuleStep(wizardContext, SquirrelSdkType.getInstance()) {
            public void updateDataModel() {
                super.updateDataModel();
                moduleBuilder.setModuleJdk(getJdk());
            }
        }};
    }
}
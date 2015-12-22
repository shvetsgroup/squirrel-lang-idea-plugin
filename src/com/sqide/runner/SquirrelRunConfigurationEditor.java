package com.sqide.runner;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SquirrelRunConfigurationEditor extends SettingsEditor<SquirrelRunConfiguration> {
    private SquirrelConfigForm form;

    public SquirrelRunConfigurationEditor(Module module) {
        this.form = new SquirrelConfigForm();
        this.form.setModuleContext(module);
    }

    @Override
    protected void resetEditorFrom(SquirrelRunConfiguration runConfiguration) {
        form.reset(runConfiguration);
        form.resetSquirrel(runConfiguration);
    }

    @Override
    protected void applyEditorTo(SquirrelRunConfiguration runConfiguration) throws ConfigurationException {
        form.applyTo(runConfiguration);
        form.applySquirrelTo(runConfiguration);
    }

    @Override
    @NotNull
    protected JComponent createEditor() {
        return form;
    }

    @Override
    protected void disposeEditor() {
        form = null;
    }
}
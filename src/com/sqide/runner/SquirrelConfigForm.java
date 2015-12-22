package com.sqide.runner;

import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.MacroAwareTextBrowseFolderListener;

import javax.swing.*;
import java.awt.*;

public class SquirrelConfigForm extends CommonProgramParametersPanel {
    private LabeledComponent<JComponent> scriptNameComponent;
    private TextFieldWithBrowseButton scriptNameField;

    public SquirrelConfigForm() {
    }

    protected void initOwnComponents() {
        FileChooserDescriptor chooseScriptDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        scriptNameField = new TextFieldWithBrowseButton();
        scriptNameField.addBrowseFolderListener(new MacroAwareTextBrowseFolderListener(chooseScriptDescriptor,
                getProject()));

        scriptNameComponent = LabeledComponent.create(createComponentWithMacroBrowse(scriptNameField), "Script:");
        scriptNameComponent.setLabelLocation(BorderLayout.WEST);
    }


    @Override
    protected void addComponents() {
        initOwnComponents();

        add(scriptNameComponent);

        super.addComponents();

        setProgramParametersLabel("Compiler options:");
    }

    public void resetSquirrel(SquirrelRunConfiguration configuration) {
        scriptNameField.setText(configuration.getScriptName());
    }

    public void applySquirrelTo(SquirrelRunConfiguration configuration) {
        configuration.setScriptName(scriptNameField.getText());
    }
}

package com.squirrelplugin.projectWizard;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

public class SquirrelModuleWizardStep extends ModuleWizardStep implements Disposable {
  private final WizardContext myContext;
  private final SquirrelGeneratorPeer myPeer;

  public SquirrelModuleWizardStep(final WizardContext context) {
    myContext = context;
    myPeer = new SquirrelGeneratorPeer();
  }

  @Override
  public JComponent getComponent() {
    return myPeer.getComponent();
  }

  @Override
  public void updateDataModel() {
  }

  @Override
  public boolean validate() throws ConfigurationException {
    return true;
  }

  @Override
  public void dispose() {

  }
}

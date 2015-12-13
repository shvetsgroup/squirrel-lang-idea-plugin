package com.squirrelplugin.formatter.settings;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class SquirrelCodeStyleConfigurable extends CodeStyleAbstractConfigurable {
  public SquirrelCodeStyleConfigurable(@NotNull CodeStyleSettings settings, CodeStyleSettings cloneSettings) {
    super(settings, cloneSettings, "Squirrel");
  }

  @Override
  protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
    return new SquirrelCodeStyleMainPanel(getCurrentSettings(), settings);
  }

  @Override
  public String getHelpTopic() {
    return "reference.settingsdialog.codestyle.squirrel";
  }
}

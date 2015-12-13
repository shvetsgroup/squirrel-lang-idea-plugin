package com.squirrelplugin.formatter.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.squirrelplugin.SquirrelBundle;
import org.jetbrains.annotations.NotNull;

public class SquirrelCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @Override
  public String getConfigurableDisplayName() {
    return SquirrelBundle.message("squirrel.title");
  }

  @NotNull
  @Override
  public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
    return new SquirrelCodeStyleConfigurable(settings, originalSettings);
  }
}

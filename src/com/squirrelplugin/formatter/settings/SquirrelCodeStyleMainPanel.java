package com.squirrelplugin.formatter.settings;

import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.squirrelplugin.SquirrelLanguage;

public class SquirrelCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
  protected SquirrelCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
    super(SquirrelLanguage.INSTANCE, currentSettings, settings);
  }
}

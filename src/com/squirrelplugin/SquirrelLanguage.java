package com.squirrelplugin;

import com.intellij.lang.Language;

public class SquirrelLanguage extends Language {
    public static final SquirrelLanguage INSTANCE = new SquirrelLanguage();

    private SquirrelLanguage() {
        super("Squirrel");
    }
}
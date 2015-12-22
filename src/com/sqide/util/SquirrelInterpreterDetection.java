package com.sqide.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to detect if there's is a Squirrel installation in one of the most common places.
 */
public class SquirrelInterpreterDetection {
    public static final List<String> POSSIBLE_LOCATIONS = Collections.unmodifiableList(Lists.newArrayList(
            "/usr/local/bin/sq"
    ));

    public static final SquirrelInterpreterDetection INSTANCE = new SquirrelInterpreterDetection();

    public static SquirrelInterpreterDetection instance() {
        return INSTANCE;
    }

    public String findBestLocation() {
        for (String guessLocation : POSSIBLE_LOCATIONS) {
            if (isSuitable(guessLocation)) {
                return guessLocation;
            }
        }

        return "";
    }

    public boolean isSuitable(String guessLocation) {
        if (guessLocation == null) {
            return false;
        }

        File f = new File(guessLocation);
        return f.isFile() && f.canRead();
    }
}

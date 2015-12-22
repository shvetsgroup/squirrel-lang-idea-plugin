package com.sqide.runner;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.DefaultProgramRunner;
import org.jetbrains.annotations.NotNull;

/**
 * This code is based on the intellij-batch plugin.
 *
 * @author wibotwi, jansorg
 */
public class SquirrelRunner extends DefaultProgramRunner {
    @NotNull
    public String getRunnerId() {
        return "SquirrelRunner";
    }

    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof SquirrelRunConfiguration;
    }
}
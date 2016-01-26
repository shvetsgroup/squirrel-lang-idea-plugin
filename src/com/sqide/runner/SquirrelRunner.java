package com.sqide.runner;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.DefaultProgramRunner;
import org.jetbrains.annotations.NotNull;

public class SquirrelRunner extends DefaultProgramRunner {
    public static final String SQUIRREL_RUNNER_ID = "SquirrelRunner";

    @NotNull
    @Override
    public String getRunnerId() {
        return SQUIRREL_RUNNER_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof SquirrelRunConfiguration;
    }
}
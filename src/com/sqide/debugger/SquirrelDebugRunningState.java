package com.sqide.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.sqide.SquirrelBundle;
import com.sqide.runner.SquirrelRunConfiguration;
import com.sqide.runner.SquirrelRunningState;
import java.io.File;

public class SquirrelDebugRunningState extends SquirrelRunningState {
    public SquirrelDebugRunningState(SquirrelRunConfiguration runConfig, ExecutionEnvironment environment) {
        super(runConfig, environment);
    }

    @Override
    public final void setExePath(GeneralCommandLine commandLine) throws ExecutionException {
        String sqdbgPath = "/Users/neochief/_SQDBG/bin/sqdbg"; // todo make configurable
        File sqdbg = new File(sqdbgPath);

        if (!sqdbg.exists()) {
            throw new ExecutionException(SquirrelBundle.message("sqdbg.not.exists.at", sqdbg.getAbsolutePath()));
        }

        if (sqdbg.exists() && !sqdbg.canExecute()) {
            //noinspection ResultOfMethodCallIgnored
            sqdbg.setExecutable(true, false);
        }

        commandLine.setExePath(sqdbg.getAbsolutePath());
    }

    @Override
    public final void setParameters(GeneralCommandLine commandLine) throws ExecutionException {
    }
}

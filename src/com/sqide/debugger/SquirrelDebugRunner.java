package com.sqide.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.DefaultProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.sqide.runner.SquirrelRunConfiguration;
import com.sqide.debugger.xdebug.SquirrelDebugProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelDebugRunner extends DefaultProgramRunner {
    public static final String SQUIRREL_DEBUG_RUNNER_ID = "SquirrelDebugRunner";

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull final ExecutionEnvironment environment) throws ExecutionException {
        RunContentDescriptor r = super.doExecute(state, environment);

        XDebuggerManager xDebuggerManager = XDebuggerManager.getInstance(environment.getProject());
        return xDebuggerManager.startSession(environment, new XDebugProcessStarter() {
            @NotNull
            @Override
            public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
                return new SquirrelDebugProcess(session, environment, r);
            }
        }).getRunContentDescriptor();
    }

    @NotNull
    @Override
    public String getRunnerId() {
        return SQUIRREL_DEBUG_RUNNER_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof SquirrelRunConfiguration;
    }
}
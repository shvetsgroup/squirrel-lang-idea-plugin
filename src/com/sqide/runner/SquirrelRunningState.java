package com.sqide.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.util.ProgramParametersUtil;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.sqide.SquirrelBundle;
import com.sqide.util.SquirrelExecutor;
import com.sqide.util.SquirrelHistoryProcessListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class SquirrelRunningState extends CommandLineState {
    private final SquirrelRunConfiguration runConfig;
    @Nullable private SquirrelHistoryProcessListener myHistoryProcessHandler;
    private int myDebugPort = 1234;

    protected SquirrelRunningState(SquirrelRunConfiguration runConfig, ExecutionEnvironment environment) {
        super(environment);
        this.runConfig = runConfig;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = patchExecutor(createCommonExecutor())
                .withParameterString(StringUtil.notNullize(runConfig.getProgramParameters()))
                .createCommandLine();

        final ProcessHandler processHandler = new KillableColoredProcessHandler(commandLine);
        processHandler.addProcessListener(new ProcessAdapter() {
            private AtomicBoolean firstOutput = new AtomicBoolean(true);

            @Override
            public void onTextAvailable(ProcessEvent event, Key outputType) {
                if (firstOutput.getAndSet(false)) {
                    if (myHistoryProcessHandler != null) {
                        myHistoryProcessHandler.apply(processHandler);
                    }
                }
                super.onTextAvailable(event, outputType);
            }

            @Override
            public void processTerminated(ProcessEvent event) {
                super.processTerminated(event);
            }
        });
        return processHandler;
    }

    @NotNull
    public SquirrelExecutor createCommonExecutor() {
        return SquirrelExecutor.in(runConfig.getConfigurationModule().getModule()).withWorkDirectory(runConfig.getWorkingDirectory());
    }

    protected SquirrelExecutor patchExecutor(@NotNull SquirrelExecutor executor) throws ExecutionException {
        String compiler = runConfig.getCompilerPath();
        if (StringUtil.isEmptyOrSpaces(compiler)) {
            throw new ExecutionException(SquirrelBundle.message("squirrel.sdk.not.configured"));
        }

        return executor.withExePath("/bin/bash").withWorkDirectory("/Users/neochief/Desktop").withParameterString("test.sh");
//
//
//        if (isDebug()) {
//            File sqdbg = sqdbg();
//            if (sqdbg.exists() && !sqdbg.canExecute()) {
//                //noinspection ResultOfMethodCallIgnored
//                sqdbg.setExecutable(true, false);
//            }
//            return executor.withExePath(sqdbg.getAbsolutePath()).withParameters(runConfig.getScriptName());
//        }
//        return executor.withExePath(compiler).withParameterString(StringUtil.notNullize(runConfig.getProgramParameters())).withParameters(runConfig.getScriptName());
    }

    public boolean isDebug() {
        return DefaultDebugExecutor.EXECUTOR_ID.equals(getEnvironment().getExecutor().getId());
    }

    @NotNull
    private static File sqdbg() {
        String sqdbgPath = "/Users/neochief/_SQDBG/bin/sqdbg"; // todo make configurable
        return new File(sqdbgPath);
    }

    public void setHistoryProcessHandler(@Nullable SquirrelHistoryProcessListener historyProcessHandler) {
        myHistoryProcessHandler = historyProcessHandler;
    }

    public void setDebugPort(int debugPort) {
        myDebugPort = debugPort;
    }
}
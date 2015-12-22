package com.sqide.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.util.ProgramParametersUtil;
import org.jetbrains.annotations.NotNull;

public class SquirrelCommandLineState extends CommandLineState {
    private final SquirrelRunConfiguration runConfig;

    protected SquirrelCommandLineState(SquirrelRunConfiguration runConfig, ExecutionEnvironment environment) {
        super(environment);
        this.runConfig = runConfig;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        String workingDir = ProgramParametersUtil.getWorkingDir(runConfig, getEnvironment().getProject(), runConfig.getConfigurationModule().getModule());

        GeneralCommandLine cmd = new GeneralCommandLine();
        cmd.setExePath(runConfig.getInterpreterPath());
        cmd.getParametersList().addParametersString(runConfig.getInterpreterOptions());

        cmd.addParameter(runConfig.getScriptName());
        cmd.getParametersList().addParametersString(runConfig.getProgramParameters());

        cmd.withWorkDirectory(workingDir);
        cmd.setPassParentEnvironment(runConfig.isPassParentEnvs());
        cmd.withEnvironment(runConfig.getEnvs());

        OSProcessHandler processHandler = new KillableColoredProcessHandler(cmd);
        ProcessTerminatedListener.attach(processHandler, getEnvironment().getProject());

        //fixme handle path macros

        return processHandler;
    }
}
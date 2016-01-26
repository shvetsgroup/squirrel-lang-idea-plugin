package com.sqide.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.filters.TextConsoleBuilderImpl;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import org.jetbrains.annotations.NotNull;

public class SquirrelRunningState extends CommandLineState {
    protected final SquirrelRunConfiguration runConfig;

    protected SquirrelRunningState(SquirrelRunConfiguration runConfig, ExecutionEnvironment environment) {
        super(environment);
        this.runConfig = runConfig;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = getCommand();
        return new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
    }

    private GeneralCommandLine getCommand() throws ExecutionException {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        setExePath(commandLine);
        setWorkDirectory(commandLine);
        setParameters(commandLine);
        setScript(commandLine);
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(runConfig.getProject());
        setConsoleBuilder(consoleBuilder);
        return commandLine;
    }

    public void setExePath(GeneralCommandLine commandLine) throws ExecutionException {
        commandLine.setExePath(runConfig.getCompilerPath());
    }

    public void setWorkDirectory(GeneralCommandLine commandLine) {
        commandLine.withWorkDirectory(runConfig.getWorkingDirectory());
    }

    public void setParameters(GeneralCommandLine commandLine) throws ExecutionException {
        String parameters = runConfig.getProgramParameters();
        if (parameters != null && parameters != "") {
            commandLine.withParameters(parameters);
        }
    }

    public void setScript(GeneralCommandLine commandLine) throws ExecutionException {
        commandLine.withParameters(runConfig.getScriptName());
    }

    @NotNull
    public ConsoleView createConsoleView(Executor executor) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(runConfig.getProject());
        return consoleBuilder.getConsole();
    }
}
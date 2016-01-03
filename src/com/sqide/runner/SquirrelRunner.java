package com.sqide.runner;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.AsyncGenericProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.internal.statistic.UsageTrigger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.util.net.NetUtils;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.sqide.sqdbg.DlvDebugProcess;
import com.sqide.sqdbg.DlvRemoteVmConnection;
import com.sqide.util.SquirrelHistoryProcessListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.AsyncPromise;
import org.jetbrains.concurrency.Promise;
import org.jetbrains.debugger.connection.RemoteVmConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SquirrelRunner extends AsyncGenericProgramRunner {
    @NotNull
    public String getRunnerId() {
        return "SquirrelRunner";
    }

    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return profile instanceof SquirrelRunConfiguration &&
                (DefaultRunExecutor.EXECUTOR_ID.equals(executorId) || DefaultDebugExecutor.EXECUTOR_ID.equals(executorId));
    }

    @NotNull
    @Override
    protected Promise<RunProfileStarter> prepare(@NotNull ExecutionEnvironment environment, @NotNull final RunProfileState state) throws ExecutionException {

        FileDocumentManager.getInstance().saveAllDocuments();

        final AsyncPromise<RunProfileStarter> buildingPromise = new AsyncPromise<RunProfileStarter>();
        final SquirrelHistoryProcessListener historyProcessListener = new SquirrelHistoryProcessListener();
        if (((SquirrelRunningState)state).isDebug()) {
            buildingPromise.setResult(new MyDebugStarter(historyProcessListener));
        }
        else {
            buildingPromise.setResult(new MyRunStarter(historyProcessListener));
        }
        return buildingPromise;
    }

    private class MyRunStarter extends RunProfileStarter {
        private final SquirrelHistoryProcessListener myHistoryProcessListener;

        private MyRunStarter(@NotNull SquirrelHistoryProcessListener historyProcessListener) {
            myHistoryProcessListener = historyProcessListener;
        }

        @Nullable
        @Override
        public RunContentDescriptor execute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
            if (state instanceof SquirrelRunningState) {
                FileDocumentManager.getInstance().saveAllDocuments();
                ((SquirrelRunningState)state).setHistoryProcessHandler(myHistoryProcessListener);
                ExecutionResult executionResult = state.execute(env.getExecutor(), SquirrelRunner.this);
                return executionResult != null ? new RunContentBuilder(executionResult, env).showRunContent(env.getContentToReuse()) : null;
            }
            return null;
        }
    }

    private class MyDebugStarter extends RunProfileStarter {
        private final SquirrelHistoryProcessListener myHistoryProcessListener;

        private MyDebugStarter(@NotNull SquirrelHistoryProcessListener historyProcessListener) {
            myHistoryProcessListener = historyProcessListener;
        }

        @Nullable
        @Override
        public RunContentDescriptor execute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env)
                throws ExecutionException {
            if (state instanceof SquirrelRunningState) {
                FileDocumentManager.getInstance().saveAllDocuments();
                ((SquirrelRunningState)state).setHistoryProcessHandler(myHistoryProcessListener);
                ((SquirrelRunningState)state).setDebugPort(1234);

                // start debugger
                final ExecutionResult executionResult = state.execute(env.getExecutor(), SquirrelRunner.this);
                if (executionResult == null) {
                    throw new ExecutionException("Cannot run debugger");
                }

                UsageTrigger.trigger("squrrel.sqdbg.debugger");


                return XDebuggerManager.getInstance(env.getProject()).startSession(env, new XDebugProcessStarter() {
                    @NotNull
                    @Override
                    public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
                        RemoteVmConnection connection = new DlvRemoteVmConnection();
                        DlvDebugProcess process = new DlvDebugProcess(session, connection, executionResult);
                        connection.open(new InetSocketAddress("127.0.0.1", 1234));
                        return process;
                    }
                }).getRunContentDescriptor();
            }
            return null;
        }
    }

}
package com.sqide.debugger.xdebug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.evaluation.EvaluationMode;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.sqide.SquirrelFileType;
import com.sqide.debugger.xdebug.breakpoint.SquirrelLineBreakpointHandler;
import com.sqide.runner.SquirrelRunConfiguration;
import com.sqide.runner.SquirrelRunningState;
import com.sqide.debugger.connection.SquirrelDebuggerConnection;
import com.sqide.debugger.connection.SquirrelDebuggerEventListener;
import com.sqide.debugger.sqdbg.SquirrelProcessSnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class SquirrelDebugProcess extends XDebugProcess implements SquirrelDebuggerEventListener {
    private final ExecutionEnvironment myExecutionEnvironment;
    private final SquirrelRunningState myRunningState;
    private final RunContentDescriptor myRunContentDescriptor;
    private final SquirrelDebuggerConnection myDebuggerConnection;
    private XBreakpointHandler<?>[] myBreakpointHandlers = new XBreakpointHandler[]{new SquirrelLineBreakpointHandler(this)};
    private ConcurrentHashMap<String, XLineBreakpoint<XBreakpointProperties>> myPositionToLineBreakpointMap =
            new ConcurrentHashMap<String, XLineBreakpoint<XBreakpointProperties>>();

    public SquirrelDebugProcess(@NotNull XDebugSession session, ExecutionEnvironment env, RunContentDescriptor executionResult) throws ExecutionException {
        super(session);
        session.setPauseActionSupported(false);

        myExecutionEnvironment = env;
        myRunningState = (SquirrelRunningState) getRunConfiguration().getState(myExecutionEnvironment.getExecutor(), myExecutionEnvironment);
        if (myRunningState == null) {
            throw new ExecutionException("Failed to execute a run configuration.");
        }

        myRunContentDescriptor = executionResult;

        getProcessHandler().addProcessListener(new ProcessAdapter() {
            @Override
            public void processWillTerminate(ProcessEvent event, boolean willBeDestroyed) {
                stop();
            }
        });

        myDebuggerConnection = new SquirrelDebuggerConnection(this, "127.0.0.1", 1234);  // todo make configurable
    }

    private SquirrelRunConfiguration getRunConfiguration() {
        SquirrelRunConfiguration runConfig = (SquirrelRunConfiguration) getSession().getRunProfile();
        assert runConfig != null;
        return runConfig;
    }

    @NotNull
    @Override
    public ExecutionConsole createConsole() {
        return myRunContentDescriptor.getExecutionConsole();
    }

    @Nullable
    @Override
    protected ProcessHandler doGetProcessHandler() {
        return myRunContentDescriptor.getProcessHandler();
    }

    @NotNull
    @Override
    public XBreakpointHandler<?>[] getBreakpointHandlers() {
        return myBreakpointHandlers;
    }

    @NotNull
    @Override
    public XDebuggerEditorsProvider getEditorsProvider() {
        return new XDebuggerEditorsProvider() {
            @NotNull
            @Override
            public FileType getFileType() {
                return SquirrelFileType.INSTANCE;
            }

            @NotNull
            @Override
            public Document createDocument(@NotNull Project project,
                                           @NotNull String text,
                                           @Nullable XSourcePosition sourcePosition,
                                           @NotNull EvaluationMode mode) {
                LightVirtualFile file = new LightVirtualFile("plain-text-squirrel-debugger.nut", text);
                //noinspection ConstantConditions
                return FileDocumentManager.getInstance().getDocument(file);
            }
        };
    }

    private String getBreakpointLocation(String file, int line) {
        return file + ":" + line;
    }

    public void addBreakpoint(XLineBreakpoint<XBreakpointProperties> breakpoint) {
        myPositionToLineBreakpointMap.put(getBreakpointLocation(breakpoint.getShortFilePath(), breakpoint.getLine()), breakpoint);
        myDebuggerConnection.setBreakpoint(breakpoint.getShortFilePath(), breakpoint.getLine());
    }

    public void removeBreakpoint(XLineBreakpoint<XBreakpointProperties> breakpoint,
                          @SuppressWarnings("UnusedParameters") boolean temporary) {
        myPositionToLineBreakpointMap.remove(getBreakpointLocation(breakpoint.getShortFilePath(), breakpoint.getLine()));
        myDebuggerConnection.removeBreakpoint(breakpoint.getShortFilePath(), breakpoint.getLine());
    }

    @Override
    public void sessionInitialized() {
        myDebuggerConnection.debuggerReady();
    }

    @Override
    public void startStepOver() {
        myDebuggerConnection.stepOver();
    }

    @Override
    public void startStepInto() {
        myDebuggerConnection.stepInto();
    }

    @Override
    public void startStepOut() {
        myDebuggerConnection.stepOut();
    }

    @Override
    public void stop() {
        myDebuggerConnection.stop();
    }

    @Override
    public void resume() {
        myDebuggerConnection.resume();
    }

    @Override
    public void runToPosition(@NotNull XSourcePosition position) {
        //TODO implement me
    }

    /**
     * SquirrelDebuggerEventListener implementations.
     */

    @Override
    public void debuggerStarted() {
        getSession().reportMessage("Debug process started", MessageType.INFO);
    }

    @Override
    public void unknownMessage(String messageText) {
        getSession().reportMessage("Unknown message received: " + messageText, MessageType.WARNING);
    }

    @Override
    public void breakpointIsSet(String module, int line) {
    }

    @Override
    public void breakpointIsRemoved(String module, int line) {
    }

    @Override
    public void breakpointReached(SquirrelProcessSnapshot snapshot) {
        XLineBreakpoint<XBreakpointProperties> breakpoint = myPositionToLineBreakpointMap.get(getBreakpointLocation(snapshot.getBreakFile(), snapshot.getBreakLine()));
        SquirrelSuspendContext suspendContext = new SquirrelSuspendContext(snapshot);

        if (breakpoint == null) {
            getSession().positionReached(suspendContext);
        }
        else {
            boolean shouldSuspend = getSession().breakpointReached(breakpoint, null, suspendContext);
            if (!shouldSuspend) {
                resume();
            }
        }
    }

    @Override
    public void debuggerStopped() {
        getSession().reportMessage("Debug process stopped", MessageType.INFO);
    }
}

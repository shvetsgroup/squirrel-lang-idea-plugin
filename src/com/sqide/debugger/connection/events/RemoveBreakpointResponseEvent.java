package com.sqide.debugger.connection.events;

import com.sqide.debugger.connection.SquirrelDebuggerConnection;
import com.sqide.debugger.connection.SquirrelDebuggerEventListener;

class RemoveBreakpointResponseEvent extends SetBreakpointResponseEvent {
    public static final String NAME = "removebreakpoint";

    public RemoveBreakpointResponseEvent(String message) throws DebuggerEventFormatException {
        super(message);
    }

    @Override
    public void process(SquirrelDebuggerConnection debuggerNode, SquirrelDebuggerEventListener eventListener) {
        eventListener.breakpointIsRemoved(file, line);
    }
}

package com.sqide.debugger.connection.events;

import com.sqide.debugger.connection.SquirrelDebuggerConnection;
import com.sqide.debugger.connection.SquirrelDebuggerEventListener;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.IOException;
import java.io.StringReader;
import org.jetbrains.annotations.Nullable;

public abstract class SquirrelDebuggerEvent {

    public abstract void process(SquirrelDebuggerConnection debuggerNode, SquirrelDebuggerEventListener eventListener);

    @Nullable
    public static SquirrelDebuggerEvent create(String message) {
        if (message == null) return null;
        try {
            org.jdom.Document doc = new SAXBuilder().build(new StringReader(message));
            String messageName = doc.getRootElement().getName();
            if (messageName == null) return null;

//            if (ResumedEvent.NAME.equals(messageName)) {
//                return new ResumedEvent();
//            }
            if (SetBreakpointResponseEvent.NAME.equals(messageName)) {
                return new SetBreakpointResponseEvent(message);
            }
            if (RemoveBreakpointResponseEvent.NAME.equals(messageName)) {
                return new RemoveBreakpointResponseEvent(message);
            }
            if (BreakpointReachedEvent.NAME.equals(messageName)) {
                return new BreakpointReachedEvent(message);
            }

        } catch (DebuggerEventFormatException | IOException | JDOMException e) {
            return new UnknownMessageEvent(message);
        }

        return new UnknownMessageEvent(message);
    }
}

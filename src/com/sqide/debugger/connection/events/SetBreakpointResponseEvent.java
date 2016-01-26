package com.sqide.debugger.connection.events;

import com.sqide.debugger.connection.SquirrelDebuggerConnection;
import com.sqide.debugger.connection.SquirrelDebuggerEventListener;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.IOException;
import java.io.StringReader;

class SetBreakpointResponseEvent extends SquirrelDebuggerEvent {
    public static final String NAME = "addbreakpoint";

    protected final String file;
    protected final int line;

    public SetBreakpointResponseEvent(String message) throws DebuggerEventFormatException {
        try {
            org.jdom.Document doc = new SAXBuilder().build(new StringReader(message));
            file = doc.getRootElement().getAttributeValue("file");
            line = Integer.parseInt(doc.getRootElement().getAttributeValue("line"));
        } catch (IOException | JDOMException e) {
            throw new DebuggerEventFormatException();
        }
    }

    @Override
    public void process(SquirrelDebuggerConnection debuggerNode, SquirrelDebuggerEventListener eventListener) {
        eventListener.breakpointIsSet(file, line);
    }
}

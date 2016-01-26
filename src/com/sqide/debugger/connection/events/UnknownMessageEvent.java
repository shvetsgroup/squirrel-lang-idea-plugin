package com.sqide.debugger.connection.events;

import com.sqide.debugger.connection.SquirrelDebuggerConnection;
import com.sqide.debugger.connection.SquirrelDebuggerEventListener;

class UnknownMessageEvent extends SquirrelDebuggerEvent {
  private final String myUnknownMessageText;

  public UnknownMessageEvent(String message) {
    myUnknownMessageText = message;
  }

  @Override
  public void process(SquirrelDebuggerConnection debuggerNode, SquirrelDebuggerEventListener eventListener) {
    eventListener.unknownMessage(myUnknownMessageText);
  }
}

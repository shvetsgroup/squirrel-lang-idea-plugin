package com.sqide.debugger.xdebug.breakpoint;

import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.sqide.debugger.xdebug.SquirrelDebugProcess;
import org.jetbrains.annotations.NotNull;

public class SquirrelLineBreakpointHandler extends XBreakpointHandler<XLineBreakpoint<XBreakpointProperties>> {
  private final SquirrelDebugProcess myDebugProcess;

  public SquirrelLineBreakpointHandler(SquirrelDebugProcess debugProcess) {
    super(SquirrelBreakpointType.class);
    myDebugProcess = debugProcess;
  }

  @Override
  public void registerBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint) {
    myDebugProcess.addBreakpoint(breakpoint);
  }

  @Override
  public void unregisterBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint, boolean temporary) {
    myDebugProcess.removeBreakpoint(breakpoint, temporary);
  }
}

package com.sqide.debugger.xdebug;

import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.sqide.debugger.sqdbg.SquirrelCall;
import com.sqide.debugger.sqdbg.SquirrelProcessSnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelSuspendContext extends XSuspendContext {
  private final XExecutionStack[] myExecutionStacks;
  private final int myActiveStackIdx;

  public SquirrelSuspendContext(@NotNull SquirrelProcessSnapshot snapshot) {
    myExecutionStacks = new XExecutionStack[snapshot.getStack().size()];
    int activeStackIdx = 0;
    for (int i = 0; i < snapshot.getStack().size(); i++) {
      SquirrelCall call = snapshot.getStack().get(i);
      myExecutionStacks[i] = new SquirrelExecutionStack(call, snapshot);
    }
    myActiveStackIdx = activeStackIdx;
  }

  @Override
  public XExecutionStack[] getExecutionStacks() {
    return myExecutionStacks;
  }

  @Nullable
  @Override
  public XExecutionStack getActiveExecutionStack() {
    return myExecutionStacks[myActiveStackIdx];
  }
}

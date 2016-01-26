package com.sqide.debugger.xdebug;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTextContainer;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XValue;
import com.sqide.debugger.sqdbg.SquirrelCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelStackFrame extends XStackFrame {
    private final SquirrelCall myTraceElement;

    public SquirrelStackFrame(@NotNull SquirrelCall traceElement) {
        myTraceElement = traceElement;
    }

    @Nullable
    @Override
    public XSourcePosition getSourcePosition() {
        //PsiShortNamesCache.getInstance(project).getFilesByName()
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(myTraceElement.getFile());
        return XDebuggerUtil.getInstance().createPosition(file, myTraceElement.getLine());
    }

    @Override
    public void customizePresentation(@NotNull ColoredTextContainer component) {
//    String functionName = mySourcePosition != null ? mySourcePosition.getFunctionName() : null;
//    if (functionName != null) {
//      SquirrelFile module = myResolver.findPsi(mySourcePosition.getSourcePosition().getFile());
//      SquirrelFunction function = module != null ? module.getFunction(functionName, mySourcePosition.getFunctionArity()) : null;
//      if (function != null) {
//        String title = SquirrelPsiImplUtil.getQualifiedFunctionName(function);
//        SquirrelFunExpression funExpression = SquirrelPsiImplUtil.findFunExpression(function, mySourcePosition.getFunExpressionArity());
//        if (funExpression != null) {
//          int line = 1 + StringUtil.offsetToLineNumber(funExpression.getContainingFile().getText(), funExpression.getTextOffset());
//          title += ": " + mySourcePosition.getFunExpressionName() + " at line " + line;
//        }
//        component.append(title, SimpleTextAttributes.REGULAR_ATTRIBUTES);
//        component.setIcon(AllIcons.Debugger.StackFrame);
//        return;
//      }
//    }
        super.customizePresentation(component);
    }

    @Override
    public void computeChildren(@NotNull XCompositeNode node) {
//    XValueChildrenList myVariables = new XValueChildrenList(myTraceElement.getBindings().size());
//    for (SquirrelVariableBinding binding : myTraceElement.getBindings()) {
//      myVariables.add(binding.getName(), getVariableValue(binding.getValue()));
//    }
//    node.addChildren(myVariables, true);
    }
}

package com.sqide.debugger.xdebug.breakpoint;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.Processor;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.sqide.SquirrelFileType;
import com.sqide.SquirrelTokenTypesSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelBreakpointType extends XLineBreakpointType<XBreakpointProperties> {
    public static final String ID = "SquirrelLineBreakpoint";
    public static final String NAME = "Squirrel breakpoint";

    protected SquirrelBreakpointType() {
        super(ID, NAME);
    }

    @Nullable
    @Override
    public XBreakpointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return null;
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        if (line < 0 || file.getFileType() != SquirrelFileType.INSTANCE) return false;

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null || document.getLineEndOffset(line) == document.getLineStartOffset(line)) return false;

        Checker canPutAtChecker = new Checker();
        XDebuggerUtil.getInstance().iterateLine(project, document, line, canPutAtChecker);

        return canPutAtChecker.isLineBreakpointAvailable();
    }

    private static final class Checker implements Processor<PsiElement> {
        private boolean myIsLineBreakpointAvailable;

        @Override
        public boolean process(@NotNull PsiElement o) {
            IElementType type = o.getNode().getElementType();
            if (SquirrelTokenTypesSets.COMMENTS.contains(type) || SquirrelTokenTypesSets.WHITE_SPACES.contains(type)) {
                return true;
            }
            myIsLineBreakpointAvailable = true;
            return false;
        }

        public boolean isLineBreakpointAvailable() {
            return myIsLineBreakpointAvailable;
        }
    }
}

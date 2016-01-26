package com.sqide.debugger.sqdbg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class SquirrelProcessSnapshot {
    private final String myBreakFile;
    private final int myBreakLine;
    private final String myBreakType;
    public static final String BREAK_TYPE_STEP = "step";
    public static final String BREAK_TYPE_BREAKPOINT = "breakpoint";
    public static final String BREAK_TYPE_ERROR = "error";
    private final String myBreakError;
    private final Collection<SquirrelVariable> myObjects;
    private final List<SquirrelCall> myStack;

    public SquirrelProcessSnapshot(@NotNull String breakFile, @NotNull int breakLine, @NotNull String breakType, @Nullable String breakError,
                                   @Nullable Collection<SquirrelVariable> objects, @NotNull List<SquirrelCall> stack) {
        myBreakType = breakType;
        myBreakFile = breakFile;
        myBreakLine = breakLine;
        myBreakError = breakError;
        myObjects = objects;
        myStack = stack;
    }

    @NotNull
    public String getBreakFile() {
        return myBreakFile;
    }

    @NotNull
    public int getBreakLine() {
        return myBreakLine;
    }

    @NotNull
    public String getBreakType() {
        return myBreakType;
    }

    @Nullable
    public String getBreakError() {
        return myBreakError;
    }

    @Nullable
    public Collection<SquirrelVariable> getObjects() {
        return myObjects;
    }

    @NotNull
    public List<SquirrelCall> getStack() {
        return myStack;
    }
}

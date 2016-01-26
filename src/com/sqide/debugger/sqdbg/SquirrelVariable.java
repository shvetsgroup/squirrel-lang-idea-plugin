package com.sqide.debugger.sqdbg;

public class SquirrelVariable {
    private final String myName;
    private final String myType;
    private final String myValue;

    public SquirrelVariable(String name, String type, String value) {
        myName = name;
        myType = type;
        myValue = value;
    }

    public String getName() {
        return myName;
    }

    public String getType() {
        return myType;
    }

    public String getValue() {
        return myValue;
    }
}

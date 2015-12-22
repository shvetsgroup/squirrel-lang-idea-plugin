package com.sqide.runner;

import com.intellij.execution.CommonProgramRunConfigurationParameters;

public interface SquirrelRunConfigurationParams extends CommonProgramRunConfigurationParameters {
    String getScriptName();

    void setScriptName(String scriptName);
}
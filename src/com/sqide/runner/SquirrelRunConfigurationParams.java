package com.sqide.runner;

import com.intellij.execution.CommonProgramRunConfigurationParameters;

public interface SquirrelRunConfigurationParams extends CommonProgramRunConfigurationParameters {
    String getInterpreterPath();

    void setInterpreterPath(String interpreterPath);

    String getInterpreterOptions();

    void setInterpreterOptions(String options);

    String getScriptName();

    void setScriptName(String scriptName);
}
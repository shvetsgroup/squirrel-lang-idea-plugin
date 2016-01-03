package com.sqide.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.AbstractRunConfiguration;
import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.util.ProgramParametersUtil;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import com.sqide.SquirrelBundle;
import com.sqide.sdk.SquirrelSdkService;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * This code is based on the intellij-batch plugin.
 *
 * @author wibotwi, jansorg
 */
public class SquirrelRunConfiguration extends AbstractRunConfiguration implements SquirrelRunConfigurationParams,
        RunConfigurationWithSuppressedDefaultDebugAction {
    private String interpreterOptions = "";
    private String workingDirectory = "";
    private String scriptName;
    private String programsParameters;

    protected SquirrelRunConfiguration(RunConfigurationModule runConfigurationModule, ConfigurationFactory
            configurationFactory, String name) {
        super(name, runConfigurationModule, configurationFactory);
    }

    @Override
    public Collection<Module> getValidModules() {
        return Arrays.asList(ModuleManager.getInstance(getProject()).getModules());
    }

    @Override
    public boolean excludeCompileBeforeLaunchOption() {
        return false;
    }

    @NotNull
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SquirrelRunConfigurationEditor(getConfigurationModule().getModule());
    }

    @Nullable
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws
            ExecutionException {
        return new SquirrelRunningState(this, env);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        super.checkConfiguration();

        Module module = getConfigurationModule().getModule();
        Project project = getProject();

        ProgramParametersUtil.checkWorkingDirectoryExist(this, getProject(), module);

        String executable = SquirrelSdkService.getInstance(project).getSquirrelExecutablePath(module);

        if (StringUtil.isEmptyOrSpaces(executable)) {
            throw new RuntimeConfigurationException(SquirrelBundle.message("squirrel.sdk.not.configured"));
        }

        if (StringUtil.isEmptyOrSpaces(scriptName)) {
            throw new RuntimeConfigurationException(SquirrelBundle.message("script.name.not.given"));
        }
    }

    @Override
    public String suggestedName() {
        String name = (new File(scriptName)).getName();

        int ind = name.lastIndexOf('.');
        if (ind != -1) {
            return name.substring(0, ind);
        }
        return name;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        PathMacroManager.getInstance(getProject()).expandPaths(element);
        super.readExternal(element);

        DefaultJDOMExternalizer.readExternal(this, element);
        readModule(element);
        EnvironmentVariablesComponent.readExternal(element, getEnvs());

        // common config
        workingDirectory = JDOMExternalizerUtil.readField(element, "WORKING_DIRECTORY");

        String parentEnvValue = JDOMExternalizerUtil.readField(element, "PARENT_ENVS");
        if (parentEnvValue != null) {
            setPassParentEnvs(Boolean.parseBoolean(parentEnvValue));
        }

        // run config
        scriptName = JDOMExternalizerUtil.readField(element, "SCRIPT_NAME");
        setProgramParameters(JDOMExternalizerUtil.readField(element, "PARAMETERS"));
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);

        // common config
        JDOMExternalizerUtil.writeField(element, "WORKING_DIRECTORY", workingDirectory);
        JDOMExternalizerUtil.writeField(element, "PARENT_ENVS", Boolean.toString(isPassParentEnvs()));

        // run config
        JDOMExternalizerUtil.writeField(element, "SCRIPT_NAME", scriptName);
        JDOMExternalizerUtil.writeField(element, "PARAMETERS", getProgramParameters());

        //JavaRunConfigurationExtensionManager.getInstance().writeExternal(this, element);
        DefaultJDOMExternalizer.writeExternal(this, element);
        writeModule(element);
        EnvironmentVariablesComponent.writeExternal(element, getEnvs());

        PathMacroManager.getInstance(getProject()).collapsePathsRecursively(element);
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Nullable
    public String getProgramParameters() {
        return programsParameters;
    }

    public void setProgramParameters(@Nullable String programParameters) {
        this.programsParameters = programParameters;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    @Nullable
    public String getCompilerPath() {
        return SquirrelSdkService.getInstance(getProject()).getSquirrelExecutablePath(getConfigurationModule().getModule());
    }
}
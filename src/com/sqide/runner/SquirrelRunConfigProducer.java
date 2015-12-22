package com.sqide.runner;

import com.sqide.psi.SquirrelFile;
import com.sqide.util.SquirrelInterpreterDetection;
import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Squirrel run config producer which looks at the current context to create a new run configuation.
 */
public class SquirrelRunConfigProducer extends RunConfigurationProducer<SquirrelRunConfiguration> {
    public SquirrelRunConfigProducer() {
        super(SquirrelConfigurationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(SquirrelRunConfiguration configuration, ConfigurationContext context, Ref<PsiElement> sourceElement) {
        Location location = context.getLocation();
        if (location == null) {
            return false;
        }

        PsiElement psiElement = location.getPsiElement();
        if (!psiElement.isValid()) {
            return false;
        }

        PsiFile psiFile = psiElement.getContainingFile();
        if (psiFile == null || !(psiFile instanceof SquirrelFile)) {
            return false;
        }

        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return false;
        }

        sourceElement.set(psiFile);

        configuration.setName(location.getVirtualFile().getPresentableName());
        configuration.setScriptName(file.getPath());

        if (file.getParent() != null) {
            configuration.setWorkingDirectory(file.getParent().getPath());
        }

        Module module = context.getModule();
        if (module != null) {
            configuration.setModule(module);
        }

        //fallback location if none was found
        if (StringUtil.isEmptyOrSpaces(configuration.getInterpreterPath())) {
            configuration.setInterpreterPath(SquirrelInterpreterDetection.instance().findBestLocation());
        }

        return true;
    }

    @Override
    public boolean isConfigurationFromContext(SquirrelRunConfiguration configuration, ConfigurationContext context) {
        Location location = context.getLocation();
        if (location == null) {
            return false;
        }

        //fixme file checks needs to check the properties

        VirtualFile file = location.getVirtualFile();

        return file != null && FileUtil.pathsEqual(file.getPath(), configuration.getScriptName());
    }
}

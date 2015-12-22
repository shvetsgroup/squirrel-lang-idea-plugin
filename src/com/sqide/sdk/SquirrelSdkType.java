package com.sqide.sdk;

import com.sqide.util.SquirrelConstants;
import com.sqide.SquirrelIcons;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class SquirrelSdkType extends SdkType {

    public SquirrelSdkType() {
        super(SquirrelConstants.SDK_TYPE_ID);
    }

    @NotNull
    public static SquirrelSdkType getInstance() {
        SquirrelSdkType instance = SdkType.findInstance(SquirrelSdkType.class);
        assert instance != null;
        return instance;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return SquirrelIcons.SQUIRREL;
    }

    @NotNull
    @Override
    public Icon getIconForAddAction() {
        return getIcon();
    }

    @Nullable
    @Override
    public String suggestHomePath() {
        VirtualFile suggestSdkDirectory = SquirrelSdkUtil.suggestSdkDirectory();
        return suggestSdkDirectory != null ? suggestSdkDirectory.getPath() : null;
    }

    @Override
    public boolean isValidSdkHome(@NotNull String path) {
        SquirrelSdkService.LOG.debug("Validating sdk path: " + path);
        String executablePath = SquirrelSdkService.getSquirrelExecutablePath(path);
        if (executablePath == null) {
            SquirrelSdkService.LOG.debug("Squirrel executable is not found: ");
            return false;
        }
        if (!new File(executablePath).canExecute()) {
            SquirrelSdkService.LOG.debug("Squirrel binary cannot be executed: " + path);
            return false;
        }
        if (getVersionString(path) != null) {
            SquirrelSdkService.LOG.debug("Found valid sdk: " + path);
            return true;
        }
        return false;
    }

    @Override
    public String adjustSelectedSdkHome(String homePath) {
        return SquirrelSdkUtil.adjustSdkPath(homePath);
    }

    @NotNull
    @Override
    public String suggestSdkName(@Nullable String currentSdkName, @NotNull String sdkHome) {
        String version = getVersionString(sdkHome);
        if (version == null) {
            return "Unknown Squirrel version at " + sdkHome;
        }
        return "Squirrel " + version;
    }

    @Nullable
    @Override
    public String getVersionString(@NotNull String sdkHome) {
        return SquirrelSdkUtil.retrieveSquirrelVersion(sdkHome);
    }

    @Nullable
    @Override
    public String getDefaultDocumentationUrl(@NotNull Sdk sdk) {
        return "http://squirrel-lang.org/doc/squirrel3.html";
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {
        return null;
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData additionalData, @NotNull Element additional) {
    }

    @NotNull
    @NonNls
    @Override
    public String getPresentableName() {
        return "Squirrel SDK";
    }
}

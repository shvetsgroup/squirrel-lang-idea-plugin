package com.sqide.sdk;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.sqide.SquirrelBundle;
import com.sqide.SquirrelIcons;
import com.sqide.util.SquirrelConstants;
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
            SquirrelSdkService.LOG.debug("Squirrel executable is not found.");
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
            return SquirrelBundle.message("squirrel.sdk.unknown.version.at", sdkHome);
        }
        return SquirrelBundle.message("squirrel.version", version);
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
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull
    SdkModificator sdkModificator) {
        return null;
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData additionalData, @NotNull Element additional) {
    }

    @NotNull
    @NonNls
    @Override
    public String getPresentableName() {
        return SquirrelBundle.message("squirrel.sdk");
    }

    @Override
    public void setupSdkPaths(@NotNull Sdk sdk) {
        String versionString = sdk.getVersionString();
        if (versionString == null) throw new RuntimeException(SquirrelBundle.message("squirrel.sdk.version.undefined"));

        SdkModificator modificator = sdk.getSdkModificator();
        String path = sdk.getHomePath();
        if (path == null) return;
        modificator.setHomePath(path);

        for (VirtualFile file : SquirrelSdkUtil.getSdkDirectoriesToAttach(path, versionString)) {
            modificator.addRoot(file, OrderRootType.CLASSES);
            modificator.addRoot(file, OrderRootType.SOURCES);
        }
        modificator.commitChanges();
    }
}

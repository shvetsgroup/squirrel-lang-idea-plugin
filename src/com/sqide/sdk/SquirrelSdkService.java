package com.sqide.sdk;

import com.sqide.util.SquirrelConstants;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SimpleModificationTracker;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

public abstract class SquirrelSdkService extends SimpleModificationTracker {
    public static final Logger LOG = Logger.getInstance(SquirrelSdkService.class);
    private static final Set<String> FEDORA_SUBDIRECTORIES = ContainerUtil.newHashSet("linux_amd64", "linux_386", "linux_arm");

    @NotNull
    protected final Project myProject;

    protected SquirrelSdkService(@NotNull Project project) {
        myProject = project;
    }

    public static SquirrelSdkService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, SquirrelSdkService.class);
    }

    @Nullable
    public abstract String getSdkHomePath(@Nullable Module module);

    @Nullable
    public abstract String getSdkVersion(@Nullable Module module);

    public abstract void chooseAndSetSdk(@Nullable Module module);

    /**
     * Use this method in order to check whether the method is appropriate for providing Squirrel-specific code insight
     */
    @Contract("null -> false")
    public boolean isSquirrelModule(@Nullable Module module) {
        return module != null && !module.isDisposed();
    }

    @Nullable
    public Configurable createSdkConfigurable() {
        return null;
    }

    @Nullable
    public String getSquirrelExecutablePath(@Nullable Module module) {
        return getSquirrelExecutablePath(getSdkHomePath(module));
    }

    // todo test this
    public static String getSquirrelExecutablePath(@Nullable String sdkHomePath) {
        if (sdkHomePath != null) {
            File binDirectory = new File(sdkHomePath, "bin");
            if (!binDirectory.exists() && SystemInfo.isLinux) {
                LOG.debug(sdkHomePath + "/bin doesn't exist, checking linux-specific paths");

                // failed to define executable path in old linux and old squirrel
                File squirrelFromPath = PathEnvironmentVariableUtil.findInPath(SquirrelConstants.SQUIRREL_EXECUTABLE_NAME);
                if (squirrelFromPath != null && squirrelFromPath.exists()) {
                    LOG.debug("Squirrel executable found at " + squirrelFromPath.getAbsolutePath());
                    return squirrelFromPath.getAbsolutePath();
                }
            }

            String executableName = SquirrelSdkUtil.getBinaryFileNameForPath(SquirrelConstants.SQUIRREL_EXECUTABLE_NAME);
            String executable = FileUtil.join(sdkHomePath, "bin", executableName);

            if (!new File(executable).exists() && SystemInfo.isLinux) {
                LOG.debug(executable + " doesn't exists. Looking for binaries in fedora-specific directories");
                // fedora
                for (String directory : FEDORA_SUBDIRECTORIES) {
                    File file = new File(binDirectory, directory);
                    if (file.exists() && file.isDirectory()) {
                        LOG.debug("Squirrel executable found at " + file.getAbsolutePath());
                        return FileUtil.join(file.getAbsolutePath(), executableName);
                    }
                }
            }
            LOG.debug("Squirrel executable found at " + executable);
            return executable;
        }
        return null;
    }
}

package com.sqide.sdk;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionHelper;
import com.intellij.execution.ExecutionModes;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.*;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.PathUtil;
import com.sqide.util.SquirrelConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SquirrelSdkUtil {
    private static final Pattern SQUIRREL_VERSION_PATTERN = Pattern.compile("#define SQUIRREL_VERSION\t_SC\\" +
            "(\"Squirrel ([0-9\\.]*?) stable\"\\)");

    // todo test
    @Nullable
    public static VirtualFile suggestSdkDirectory() {
        if (SystemInfo.isMac || SystemInfo.isLinux) {
            String fromEnv = suggestSdkDirectoryPathFromEnv();
            if (fromEnv != null) {
                return LocalFileSystem.getInstance().findFileByPath(fromEnv);
            }
            VirtualFile usrLocal = LocalFileSystem.getInstance().findFileByPath("/usr/local/squirrel");
            if (usrLocal != null) return usrLocal;
        }
        if (SystemInfo.isMac) {
            String macPorts = "/opt/local/lib/squirrel";
            String homeBrew = "/usr/local/Cellar/squirrel";
            File file = FileUtil.findFirstThatExist(macPorts, homeBrew);
            if (file != null) {
                return LocalFileSystem.getInstance().findFileByIoFile(file);
            }
        }
        return null;
    }

    @Nullable
    private static String suggestSdkDirectoryPathFromEnv() {
        File fileFromPath = PathEnvironmentVariableUtil.findInPath(SquirrelConstants.SQUIRREL_COMPILER_NAME);
        if (fileFromPath != null) {
            File canonicalFile;
            try {
                canonicalFile = fileFromPath.getCanonicalFile();
                String path = canonicalFile.getPath();
                if (path.endsWith("bin/" + SquirrelConstants.SQUIRREL_COMPILER_NAME)) {
                    return StringUtil.trimEnd(path, "bin/" + SquirrelConstants.SQUIRREL_COMPILER_NAME);
                }
            } catch (IOException ignore) {
            }
        }
        return null;
    }


    // todo test
    @Nullable
    public static String retrieveSquirrelVersion(@NotNull String sdkPath) {
        try {
            File versionFilePath = new File(sdkPath, SquirrelConstants.SQUIRREL_VERSION_FILE_PATH);
            if (!versionFilePath.exists()) {
                SquirrelSdkService.LOG.debug("Cannot find 'include/squirrel.h' file at sdk path: " + sdkPath);
                return null;
            }
            String file = FileUtil.loadFile(versionFilePath);
            String version = parseSquirrelVersion(file);
            if (version == null) {
                SquirrelSdkService.LOG.debug("Cannot retrieve squirrel version from 'include/squirrel.h' file: " + file);
            }
            return version;
        } catch (IOException e) {
            SquirrelSdkService.LOG.debug("Cannot retrieve squirrel version from sdk path: " + sdkPath, e);
            return null;
        }
    }

    @Nullable
    public static String parseSquirrelVersion(@NotNull String text) {
        Matcher matcher = SQUIRREL_VERSION_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static class Version implements Comparable<Version> {

        private String version;

        public final String get() {
            return this.version;
        }

        public Version(String version) {
            if (version == null)
                throw new IllegalArgumentException("Version can not be null");
            if (!version.matches("[0-9]+(\\.[0-9]+)*"))
                throw new IllegalArgumentException("Invalid version format");
            this.version = version;
        }

        @Override
        public int compareTo(Version that) {
            if (that == null)
                return 1;
            String[] thisParts = this.get().split("\\.");
            String[] thatParts = that.get().split("\\.");
            int length = Math.max(thisParts.length, thatParts.length);
            for (int i = 0; i < length; i++) {
                int thisPart = i < thisParts.length ?
                        Integer.parseInt(thisParts[i]) : 0;
                int thatPart = i < thatParts.length ?
                        Integer.parseInt(thatParts[i]) : 0;
                if (thisPart < thatPart)
                    return -1;
                if (thisPart > thatPart)
                    return 1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that)
                return true;
            if (that == null)
                return false;
            if (this.getClass() != that.getClass())
                return false;
            return this.compareTo((Version) that) == 0;
        }

    }

    /**
     * Check if we've got the directory with list of version directories. If so, grab the last one which have a bin
     * in it.
     */
    @NotNull
    public static String adjustSdkPath(@NotNull String path) {
        List<Version> versions = new ArrayList<Version>();
        File binDirectory = new File(path, "bin");

        if (!binDirectory.exists() && path.endsWith("bin/" + SquirrelConstants.SQUIRREL_COMPILER_NAME)) {
            path = StringUtil.trimEnd(path, "bin/" + SquirrelConstants.SQUIRREL_COMPILER_NAME);
        }

        if (!binDirectory.exists() && path.endsWith("bin")) {
            path = StringUtil.trimEnd(path, "bin");
        }

        if (!binDirectory.exists()) {
            File[] files = new File(path).listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    String versionDir = file.getAbsolutePath();
                    binDirectory = new File(versionDir, "bin");
                    if (binDirectory.exists()) {
                        versions.add(new Version(file.getName()));
                    }
                }
            }
            if (versions.size() > 0) {
                Collections.sort(versions, Collections.reverseOrder());
                return path + '/' + versions.get(0).get();
            }
        }

        return path;
    }

    @Nullable
    public static String getMakeExecutable() {
        File make = PathEnvironmentVariableUtil.findInPath("make");
        if (make == null) return null;
        return make.getAbsolutePath();
    }

    @NotNull
    public static String getCompilerName() {
        String resultBinaryName = FileUtil.getNameWithoutExtension(PathUtil.getFileName(SquirrelConstants.SQUIRREL_COMPILER_NAME));
        return SystemInfo.isWindows ? resultBinaryName + ".exe" : resultBinaryName;
    }

    @NotNull
    public static String getCompilerPath(@NotNull String sdkHomePath) {
        String compilerName = SquirrelSdkUtil.getCompilerName();
        return FileUtil.join(sdkHomePath, "bin", compilerName);
    }

    @NotNull
    public static boolean sourcesExist(String sdkHomePath) {
        return new File(sdkHomePath, "include/squirrel.h").exists();
    }

    @NotNull
    public static boolean canMake() {
        String make = getMakeExecutable();
        return make != null;
    }

    @NotNull
    public static boolean binDirExist(String sdkHomePath) {
        return new File(sdkHomePath, "bin").exists();
    }

    @NotNull
    public static boolean compilerExist(String sdkHomePath) {
        String compilerName = SquirrelSdkUtil.getCompilerName();
        String compiler = FileUtil.join(sdkHomePath, "bin", compilerName);
        return new File(compiler).exists();
    }


    @NotNull
    public static Collection<VirtualFile> getSdkDirectoriesToAttach(@NotNull String sdkPath, @NotNull String versionString) {
        // At this point, we only add a root path.
        String srcPath = "";
        VirtualFile src = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(FileUtil.join(sdkPath, srcPath)));
        if (src != null && src.isDirectory()) {
            return Collections.singletonList(src);
        }
        return Collections.emptyList();
    }

    public static void makeSquirrelCompiler(String sdkPath) {
        try {
            if (retrieveSquirrelVersion(sdkPath) == null) {
                throw new ExecutionException("Failed to make squirrel compiler, no sources present at the path.");
            }

            String make = getMakeExecutable();
            if (make == null) {
                throw new ExecutionException("Failed to found make executable.");
            }

            GeneralCommandLine cmd = new GeneralCommandLine();
            cmd.setExePath(make);
            cmd.withWorkDirectory(sdkPath);
            cmd.getParametersList().addParametersString("-C " + sdkPath);

            KillableColoredProcessHandler handler = new KillableColoredProcessHandler(cmd);
            handler.setShouldDestroyProcessRecursively(true);
            ProcessTerminatedListener.attach(handler);
            handler.startNotify();

            Project project = ProjectManager.getInstance().getDefaultProject();
            ExecutionHelper.executeExternalProcess(project, handler, new ExecutionModes.ModalProgressMode("Making a squirrel compiler..."), cmd);
        }
        catch (ExecutionException e) {
            SquirrelSdkService.LOG.debug(e.getMessage());
        }
    }
}

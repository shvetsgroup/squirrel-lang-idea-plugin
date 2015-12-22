package com.sqide.sdk;

import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
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
        File fileFromPath = PathEnvironmentVariableUtil.findInPath(SquirrelConstants.SQUIRREL_EXECUTABLE_NAME);
        if (fileFromPath != null) {
            File canonicalFile;
            try {
                canonicalFile = fileFromPath.getCanonicalFile();
                String path = canonicalFile.getPath();
                if (path.endsWith("bin/" + SquirrelConstants.SQUIRREL_EXECUTABLE_NAME)) {
                    return StringUtil.trimEnd(path, "bin/" + SquirrelConstants.SQUIRREL_EXECUTABLE_NAME);
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
                SquirrelSdkService.LOG.debug("Cannot retrieve squirrel version from zVersion file: " + file);
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

    @NotNull
    public static String getBinaryFileNameForPath(@NotNull String path) {
        String resultBinaryName = FileUtil.getNameWithoutExtension(PathUtil.getFileName(path));
        return SystemInfo.isWindows ? resultBinaryName + ".exe" : resultBinaryName;
    }

    @NotNull
    public static Collection<VirtualFile> getSdkDirectoriesToAttach(@NotNull String sdkPath, @NotNull String
            versionString) {
        String srcPath = "";
        // scr is enough at the moment, possible process binaries from pkg
        VirtualFile src = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(FileUtil.join(sdkPath,
                srcPath)));
        if (src != null && src.isDirectory()) {
            return Collections.singletonList(src);
        }
        return Collections.emptyList();
    }


/*
  @Nullable
  private static VirtualFile getSdkSrcDir(@NotNull Project project, @Nullable Module module) {
    String sdkHomePath = SquirrelSdkService.getInstance(project).getSdkHomePath(module);
    String sdkVersionString = SquirrelSdkService.getInstance(project).getSdkVersion(module);
    VirtualFile sdkSrcDir = null;
    if (sdkHomePath != null && sdkVersionString != null) {
      File sdkSrcDirFile = new File(sdkHomePath, getSrcLocation(sdkVersionString));
      sdkSrcDir = LocalFileSystem.getInstance().findFileByIoFile(sdkSrcDirFile);
    }
    return sdkSrcDir;
  }

  @Nullable
  public static SquirrelFile findBuiltinFile(@NotNull PsiElement context) {
    final Project project = context.getProject();
    Module moduleFromContext = ModuleUtilCore.findModuleForPsiElement(context);
    if (moduleFromContext == null) {
      for (Module module : ModuleManager.getInstance(project).getModules()) {
        if (SquirrelSdkService.getInstance(project).isSquirrelModule(module)) {
          moduleFromContext = module;
          break;
        }
      }
    }

    final Module module = moduleFromContext;
    UserDataHolder holder = ObjectUtils.notNull(module, project);
    VirtualFile file = CachedValuesManager.getManager(context.getProject()).getCachedValue(holder, new
    CachedValueProvider<VirtualFile>() {
      @Nullable
      @Override
      public Result<VirtualFile> compute() {
        VirtualFile sdkSrcDir = getSdkSrcDir(project, module);
        VirtualFile result = sdkSrcDir != null ? sdkSrcDir.findFileByRelativePath(SquirrelConstants
        .BUILTIN_FILE_PATH) : null;
        return Result.create(result, getSdkAndLibrariesCacheDependencies(project, module, result));
      }
    });

    if (file == null) return null;
    PsiFile psiBuiltin = context.getManager().findFile(file);
    return (psiBuiltin instanceof SquirrelFile) ? (SquirrelFile)psiBuiltin : null;
  }

  @Nullable
  public static VirtualFile findExecutableInSquirrelPath(@NotNull String executableName, @NotNull Project project,
  @Nullable Module module) {
    executableName = getBinaryFileNameForPath(executableName);
    Collection<VirtualFile> roots = getSquirrelPathRoots(project, module);
    for (VirtualFile file : roots) {
      VirtualFile child = VfsUtil.findRelativeFile(file, "bin", executableName);
      if (child != null) return child;
    }
    File fromPath = PathEnvironmentVariableUtil.findInPath(executableName);
    return fromPath != null ? VfsUtil.findFileByIoFile(fromPath, true) : null;
  }

  /**
   * @return concatination of
   * {@link this#getSdkSrcDir(Project, Module)} and {@link this#getSquirrelPathSources(Project, Module)}
   */
  /*@NotNull
  public static Collection<VirtualFile> getSourcesPathsToLookup(@NotNull Project project, @Nullable Module module) {
    Set<VirtualFile> result = newLinkedHashSet();
    ContainerUtil.addIfNotNull(result, getSdkSrcDir(project, module));
    result.addAll(getSquirrelPathSources(project, module));
    return result;
  }

  @NotNull
  private static Collection<VirtualFile> getSquirrelPathRoots(@NotNull Project project, @Nullable Module module) {
    Collection<VirtualFile> roots = ContainerUtil.newArrayList();
    if (SquirrelApplicationLibrariesService.getInstance().isUseSquirrelPathFromSystemEnvironment()) {
      roots.addAll(getSquirrelPathsRootsFromEnvironment());
    }
    roots.addAll(module != null ? SquirrelLibrariesService.getUserDefinedLibraries(module) : SquirrelLibrariesService
    .getUserDefinedLibraries(project));
    return roots;
  }

  @NotNull
  public static Collection<VirtualFile> getSquirrelPathSources(@NotNull Project project, @Nullable Module module) {
    Collection<VirtualFile> result = newLinkedHashSet();
    if (module != null && SquirrelSdkService.getInstance(project).isAppEngineSdk(module)) {
      ContainerUtil.addAllNotNull(result, ContainerUtil.mapNotNull(YamlFilesModificationTracker.getYamlFiles(project,
       module),
                                                                   SquirrelUtil.RETRIEVE_FILE_PARENT_FUNCTION));
    }
    result.addAll(ContainerUtil.mapNotNull(getSquirrelPathRoots(project, module), new
    RetrieveSubDirectoryOrSelfFunction("src")));
    return result;
  }

  @NotNull
  public static Collection<VirtualFile> getSquirrelPathBins(@NotNull Project project, @Nullable Module module) {
    Collection<VirtualFile> result = newLinkedHashSet(ContainerUtil.mapNotNull(getSquirrelPathRoots(project, module),
                                                                               new RetrieveSubDirectoryOrSelfFunction
                                                                               ("bin")));
    String executableSquirrelPath = SquirrelSdkService.getInstance(project).getSquirrelExecutablePath(module);
    if (executableSquirrelPath != null) {
      VirtualFile executable = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl
      (executableSquirrelPath));
      if (executable != null) ContainerUtil.addIfNotNull(result, executable.getParent());
    }
    return result;
  }

  /**
   * Retrieves root directories from SquirrelPATH env-variable.
   * This method doesn't consider user defined libraries,
   * for that case use {@link {@link this#getSquirrelPathRoots(Project, Module)}
   */
  /*@NotNull
  public static Collection<VirtualFile> getSquirrelPathsRootsFromEnvironment() {
    return SquirrelEnvironmentSquirrelPathModificationTracker.getSquirrelEnvironmentSquirrelPathRoots();
  }

  @NotNull
  public static String retrieveSquirrelPath(@NotNull Project project, @Nullable Module module) {
    return StringUtil.join(ContainerUtil.map(getSquirrelPathRoots(project, module), SquirrelUtil
    .RETRIEVE_FILE_PATH_FUNCTION), File.pathSeparator);
  }

  @NotNull
  public static String retrieveEnvironmentPathForSquirrel(@NotNull Project project, @Nullable Module module) {
    return StringUtil.join(ContainerUtil.map(getSquirrelPathBins(project, module), SquirrelUtil
    .RETRIEVE_FILE_PATH_FUNCTION), File.pathSeparator);
  }

  @NotNull
  static String getSrcLocation(@NotNull String version) {
    if (version.startsWith("devel")) {
      return "src";
    }
    return compareVersions(version, "1.4") < 0 ? "src/pkg" : "src";
  }

  public static int compareVersions(@NotNull String lhs, @NotNull String rhs) {
    return VersionComparatorUtil.compare(lhs, rhs);
  }

  @Nullable
  public static VirtualFile findFileByRelativeToLibrariesPath(@NotNull String path, @NotNull Project project,
  @Nullable Module module) {
    for (VirtualFile root : getSourcesPathsToLookup(project, module)) {
      VirtualFile file = root.findFileByRelativePath(path);
      if (file != null) {
        return file;
      }
    }
    return null;
  }

  @Nullable
  @Contract("null -> null")
  public static String getImportPath(@Nullable final PsiDirectory psiDirectory) {
    if (psiDirectory == null) {
      return null;
    }
    return CachedValuesManager.getCachedValue(psiDirectory, new CachedValueProvider<String>() {
      @Nullable
      @Override
      public Result<String> compute() {
        Project project = psiDirectory.getProject();
        Module module = ModuleUtilCore.findModuleForPsiElement(psiDirectory);
        String path = getPathRelativeToSdkAndLibraries(psiDirectory.getVirtualFile(), project, module);
        return Result.create(path, getSdkAndLibrariesCacheDependencies(psiDirectory));
      }
    });
  }

  @Nullable
  public static String getPathRelativeToSdkAndLibraries(@NotNull VirtualFile file, @Nullable Project project,
  @Nullable Module module) {
    if (project != null) {
      String result = null;
      for (VirtualFile root : getSourcesPathsToLookup(project, module)) {
        String relativePath = VfsUtilCore.getRelativePath(file, root, '/');
        if (StringUtil.isNotEmpty(relativePath) && (result == null || result.length() > relativePath.length())) {
          result = relativePath;
        }
      }
      if (result != null) return result;
    }

    String filePath = file.getPath();
    int src = filePath.lastIndexOf("/src/");
    if (src > -1) {
      return filePath.substring(src + 5);
    }
    return null;
  }



  private static boolean isAppEngine(@NotNull String path) {
    return new File(path, SquirrelConstants.APP_ENGINE_MARKER_FILE).exists();
  }

  @NotNull
  public static Collection<Object> getSdkAndLibrariesCacheDependencies(@NotNull PsiElement context, Object... extra) {
    return getSdkAndLibrariesCacheDependencies(context.getProject(), ModuleUtilCore.findModuleForPsiElement(context),
                                               ArrayUtil.append(extra, context));
  }

  @NotNull
  public static Collection<Object> getSdkAndLibrariesCacheDependencies(@NotNull Project project, @Nullable Module
  module, Object... extra) {
    Collection<Object> dependencies = ContainerUtil.newArrayList((Object[])SquirrelLibrariesService
    .getModificationTrackers(project, module));
    ContainerUtil.addAllNotNull(dependencies, SquirrelSdkService.getInstance(project));
    ContainerUtil.addAllNotNull(dependencies, extra);
    return dependencies;
  }

  @NotNull
  public static Collection<Module> getSquirrelModules(@NotNull Project project) {
    if (project.isDefault()) return Collections.emptyList();
    final SquirrelSdkService sdkService = SquirrelSdkService.getInstance(project);
    return ContainerUtil.filter(ModuleManager.getInstance(project).getModules(), new Condition<Module>() {
      @Override
      public boolean value(Module module) {
        return sdkService.isSquirrelModule(module);
      }
    });
  }

  public static class RetrieveSubDirectoryOrSelfFunction implements Function<VirtualFile, VirtualFile> {
    @NotNull
    private final String mySubdirName;

    public RetrieveSubDirectoryOrSelfFunction(@NotNull String subdirName) {
      mySubdirName = subdirName;
    }

    @Override
    public VirtualFile fun(VirtualFile file) {
      return file == null || FileUtil.namesEqual(mySubdirName, file.getName()) ? file : file.findChild(mySubdirName);
    }
  }*/
}

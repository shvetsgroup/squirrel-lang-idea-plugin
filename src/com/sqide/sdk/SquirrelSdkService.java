package com.sqide.sdk;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.SimpleModificationTracker;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.ObjectUtils;
import com.sqide.configuration.SquirrelSdkConfigurable;
import com.sqide.util.SquirrelConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelSdkService extends SimpleModificationTracker {
    public static final Logger LOG = Logger.getInstance(SquirrelSdkService.class);

    @NotNull
    protected final Project myProject;

    protected SquirrelSdkService(@NotNull Project project) {
        myProject = project;
    }

    public static SquirrelSdkService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, SquirrelSdkService.class);
    }

    @Nullable
    public String getSquirrelExecutablePath(@Nullable Module module) {
        return getSquirrelExecutablePath(getSdkHomePath(module));
    }

    @Nullable
    public static String getSquirrelExecutablePath(@Nullable String sdkHomePath) {
        if (sdkHomePath == null)  return null;

        if (!SquirrelSdkUtil.binDirExist(sdkHomePath)) {
            LOG.debug(sdkHomePath + "/bin doesn't exist, checking linux-specific paths");
            return null;
        }

        if (!SquirrelSdkUtil.compilerExist(sdkHomePath) && SquirrelSdkUtil.sourcesExist(sdkHomePath)) {
            if (!SquirrelSdkUtil.canMake()) {
                LOG.debug("Can't found make in PATH.");
                return null;
            }

            SquirrelSdkUtil.makeSquirrelCompiler(sdkHomePath);
        }

        if (!SquirrelSdkUtil.compilerExist(sdkHomePath)) {
            LOG.debug("Compiler doesn't exists at " + sdkHomePath + "/bin.");
            return null;
        }

        String compiler = SquirrelSdkUtil.getCompilerPath(sdkHomePath);

        LOG.debug("Yay! Squirrel executable found at " + compiler);
        return compiler;
    }



    public static final String LIBRARY_NAME = SquirrelConstants.SDK_TYPE_ID;

    @Nullable
    public String getSdkHomePath(@Nullable Module module) {
        return getSdkHomeLibPath(module);
    }

    private String getSdkHomeLibPath(@Nullable Module module) {
        ComponentManager holder = ObjectUtils.notNull(module, myProject);
        return CachedValuesManager.getManager(myProject).getCachedValue(holder, new CachedValueProvider<String>() {
            @Nullable
            @Override
            public Result<String> compute() {
                return Result.create(ApplicationManager.getApplication().runReadAction(new Computable<String>() {
                    @Nullable
                    @Override
                    public String compute() {
                        LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTable(myProject);
                        for (Library library : table.getLibraries()) {
                            String libraryName = library.getName();
                            if (libraryName != null && libraryName.startsWith(LIBRARY_NAME)) {
                                for (VirtualFile root : library.getFiles(OrderRootType.CLASSES)) {
                                    if (isSquirrelSdkLibRoot(root)) {
                                        return root.getCanonicalPath();
                                    }
                                }
                            }
                        }
                        return null;
                    }
                }), SquirrelSdkService.this);
            }
        });
    }

    @Nullable
    public String getSdkVersion(@Nullable final Module module) {
        ComponentManager holder = ObjectUtils.notNull(module, myProject);
        return CachedValuesManager.getManager(myProject).getCachedValue(holder, new CachedValueProvider<String>() {
            @Nullable
            @Override
            public Result<String> compute() {
                String result = null;
                String sdkHomePath = getSdkHomePath(module);
                if (sdkHomePath != null) {
                    result = SquirrelSdkUtil.retrieveSquirrelVersion(sdkHomePath);
                }
                return Result.create(result, SquirrelSdkService.this);
            }
        });
    }

    public void chooseAndSetSdk(@Nullable Module module) {
        ShowSettingsUtil.getInstance().editConfigurable(myProject, new SquirrelSdkConfigurable(myProject, true));
    }

    @Nullable
    public Configurable createSdkConfigurable() {
        return !myProject.isDefault() ? new SquirrelSdkConfigurable(myProject, false) : null;
    }

    public boolean isSquirrelModule(@Nullable Module module) {
        return getSdkHomeLibPath(module) != null;
    }

    public static boolean isSquirrelSdkLibRoot(@NotNull VirtualFile root) {
        return root.isInLocalFileSystem() &&
                root.isDirectory() &&
                VfsUtilCore.findRelativeFile(SquirrelConstants.SQUIRREL_VERSION_FILE_PATH, root) != null;
    }
}

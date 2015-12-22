package com.sqide.sdk;

import com.sqide.util.SquirrelConstants;
import com.sqide.configuration.SquirrelSdkConfigurable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelSmallIDEsSdkService extends SquirrelSdkService {
    public static final String LIBRARY_NAME = "Squirrel SDK";

    public SquirrelSmallIDEsSdkService(@NotNull Project project) {
        super(project);
    }

    @Nullable
    @Override
    public String getSdkHomePath(@Nullable Module module) {
        ComponentManager holder = ObjectUtils.notNull(module, myProject);
        return CachedValuesManager.getManager(myProject).getCachedValue(holder, new CachedValueProvider<String>() {
            @Nullable
            @Override
            public Result<String> compute() {
                return Result.create(ApplicationManager.getApplication().runReadAction(new Computable<String>() {
                    @Nullable
                    @Override
                    public String compute() {
                        LOG.info("getSdkHomePath1: ");
                        LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTable(myProject);
                        for (Library library : table.getLibraries()) {
                            String libraryName = library.getName();
                            LOG.info("getSdkHomePath4: " + library.getName());
                            if (libraryName != null && libraryName.startsWith(LIBRARY_NAME)) {
                                for (VirtualFile root : library.getFiles(OrderRootType.CLASSES)) {
                                    LOG.info("getSdkHomePath5: " + root.getCanonicalPath());
                                    if (isSquirrelSdkLibRoot(root)) {
                                        LOG.info("getSdkHomePath2: " + root.getCanonicalPath());
                                        return root.getCanonicalPath();
                                    }
                                }
                            }
                        }
                        LOG.info("getSdkHomePath3: ");
                        return null;
                    }
                }), SquirrelSmallIDEsSdkService.this);
            }
        });
    }

    @Nullable
    @Override
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
                return Result.create(result, SquirrelSmallIDEsSdkService.this);
            }
        });
    }

    @Override
    public void chooseAndSetSdk(@Nullable Module module) {
        ShowSettingsUtil.getInstance().editConfigurable(myProject, new SquirrelSdkConfigurable(myProject, true));
    }

    @Nullable
    @Override
    public Configurable createSdkConfigurable() {
        LOG.info("createSdkConfigurable: " + myProject.isDefault());
        return !myProject.isDefault() ? new SquirrelSdkConfigurable(myProject, false) : null;
    }

    @Override
    public boolean isSquirrelModule(@Nullable Module module) {
        LOG.info("isSquirrelModule: " + super.isSquirrelModule(module));
        LOG.info("isSquirrelModule: " + (getSdkHomePath(module) != null));

        return super.isSquirrelModule(module) && getSdkHomePath(module) != null;
    }

    public static boolean isSquirrelSdkLibRoot(@NotNull VirtualFile root) {
        LOG.info("isSquirrelSdkLibRoot1: " + root);
        LOG.info("isSquirrelSdkLibRoot2: " + root.isInLocalFileSystem());
        LOG.info("isSquirrelSdkLibRoot3: " + root.isDirectory());
        LOG.info("isSquirrelSdkLibRoot4: " + (VfsUtilCore.findRelativeFile(SquirrelConstants.SQUIRREL_VERSION_FILE_PATH, root) != null));

        return root.isInLocalFileSystem() &&
                root.isDirectory() &&
                VfsUtilCore.findRelativeFile(SquirrelConstants.SQUIRREL_VERSION_FILE_PATH, root) != null;
    }
}

package com.sqide.sdk;

import com.sqide.SquirrelModuleType;
import com.intellij.ProjectTopics;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquirrelIdeaSdkService extends SquirrelSdkService {
    public SquirrelIdeaSdkService(@NotNull Project project) {
        super(project);
        myProject.getMessageBus().connect(project).subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter() {
            @Override
            public void rootsChanged(ModuleRootEvent event) {
                incModificationCount();
            }
        });
    }

    @Override
    public String getSdkHomePath(@Nullable final Module module) {
        ComponentManager holder = ObjectUtils.notNull(module, myProject);
        return CachedValuesManager.getManager(myProject).getCachedValue(holder, new CachedValueProvider<String>() {
            @Nullable
            @Override
            public Result<String> compute() {
                Sdk sdk = getSquirrelSdk(module);
                return Result.create(sdk != null ? sdk.getHomePath() : null, SquirrelIdeaSdkService.this);
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
                Sdk sdk = getSquirrelSdk(module);
                return Result.create(sdk != null ? sdk.getVersionString() : null, SquirrelIdeaSdkService.this);
            }
        });
    }

    @Override
    public void chooseAndSetSdk(@Nullable final Module module) {
        Sdk projectSdk = ProjectSettingsService.getInstance(myProject).chooseAndSetSdk();
        if (projectSdk == null && module != null) {
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    if (!module.isDisposed()) {
                        ModuleRootModificationUtil.setSdkInherited(module);
                    }
                }
            });
        }
    }

    @Override
    public boolean isSquirrelModule(@Nullable Module module) {
        return super.isSquirrelModule(module) && ModuleUtil.getModuleType(module) == SquirrelModuleType.getInstance();
    }

    private Sdk getSquirrelSdk(@Nullable Module module) {
        if (module != null) {
            Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
            if (sdk != null && sdk.getSdkType() instanceof SquirrelSdkType) {
                return sdk;
            }
        }
        Sdk sdk = ProjectRootManager.getInstance(myProject).getProjectSdk();
        return sdk != null && sdk.getSdkType() instanceof SquirrelSdkType ? sdk : null;
    }
}

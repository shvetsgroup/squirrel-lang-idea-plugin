package com.sqide.inspections;

import com.sqide.SquirrelBundle;
import com.sqide.SquirrelFileType;
import com.sqide.SquirrelLanguage;
import com.sqide.sdk.SquirrelSdkService;
import com.intellij.ProjectTopics;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrongSdkConfigurationNotificationProvider extends EditorNotifications.Provider<EditorNotificationPanel> implements DumbAware {
  private static final Key<EditorNotificationPanel> KEY = Key.create(SquirrelBundle.message("setup.squirrel.sdk"));

  private final Project myProject;

  public WrongSdkConfigurationNotificationProvider(@NotNull Project project, @NotNull final EditorNotifications notifications) {
    myProject = project;
    MessageBusConnection connection = myProject.getMessageBus().connect(project);
    connection.subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter() {
      @Override
      public void rootsChanged(ModuleRootEvent event) {
        notifications.updateAllNotifications();
      }
    });
  }

  @NotNull
  @Override
  public Key<EditorNotificationPanel> getKey() {
    return KEY;
  }

  @Override
  public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor) {
    if (file.getFileType() != SquirrelFileType.INSTANCE) return null;

    PsiFile psiFile = PsiManager.getInstance(myProject).findFile(file);
    if (psiFile == null) return null;

    if (psiFile.getLanguage() != SquirrelLanguage.INSTANCE) return null;

    Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
    if (module == null) return null;

    String sdkHomePath = SquirrelSdkService.getInstance(myProject).getSdkHomePath(module);
    if (StringUtil.isEmpty(sdkHomePath)) {
      return createMissingSdkPanel(myProject, module);
    }

    return null;
  }

  @NotNull
  private static EditorNotificationPanel createMissingSdkPanel(@NotNull final Project project, @Nullable final Module module) {
    EditorNotificationPanel panel = new EditorNotificationPanel();
    panel.setText(SquirrelBundle.message("squirrel.sdk.not.configured"));
    panel.createActionLabel(SquirrelBundle.message("setup.squirrel.sdk"), new Runnable() {
      @Override
      public void run() {
        SquirrelSdkService.getInstance(project).chooseAndSetSdk(module);
      }
    });
    return panel;
  }
}
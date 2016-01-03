/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sqide.util;

import com.sqide.sdk.SquirrelSdkService;
import com.sqide.sdk.SquirrelSdkUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionHelper;
import com.intellij.execution.ExecutionModes;
import com.intellij.execution.RunContentExecutor;
import com.intellij.execution.configurations.EncodingEnvironmentUtil;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.process.*;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.util.Consumer;
import com.intellij.util.EnvironmentUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.pty4j.unix.PtyHelpers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class SquirrelExecutor {
  private static final Logger LOGGER = Logger.getInstance(SquirrelExecutor.class);
  @NotNull
  private final Map<String, String> myExtraEnvironment = ContainerUtil.newHashMap();
  @NotNull
  private final ParametersList myParameterList = new ParametersList();
  @NotNull
  private ProcessOutput myProcessOutput = new ProcessOutput();
  @NotNull
  private final Project myProject;
  @Nullable
  private final Module myModule;
  @Nullable
  private String myGoRoot;
  @Nullable
  private String myGoPath;
  @Nullable
  private String myEnvPath;
  @Nullable
  private String myWorkDirectory;
  private boolean myShowOutputOnError = false;
  private boolean myShowNotificationsOnError = false;
  private boolean myShowNotificationsOnSuccess = false;
  private boolean myPassParentEnvironment = true;
  private boolean myPtyDisabled = false;
  @Nullable
  private String myExePath = null;
  @Nullable
  private String myPresentableName;
  private OSProcessHandler myProcessHandler;
  private Collection<ProcessListener> myProcessListeners = ContainerUtil.newArrayList();

  private SquirrelExecutor(@NotNull Project project, @Nullable Module module) {
    myProject = project;
    myModule = module;
  }

  public static SquirrelExecutor in(@NotNull Project project, @Nullable Module module) {
    return module != null ? in(module) : in(project);
  }

  @NotNull
  public static SquirrelExecutor in(@NotNull Project project) {
    return new SquirrelExecutor(project, null)
      .withGoRoot(SquirrelSdkService.getInstance(project).getSdkHomePath(null));
  }

  @NotNull
  public static SquirrelExecutor in(Module module) {
    Project project = module.getProject();
    return new SquirrelExecutor(project, module).withGoRoot(SquirrelSdkService.getInstance(project).getSdkHomePath(module));
  }

  @NotNull
  public SquirrelExecutor withPresentableName(@Nullable String presentableName) {
    myPresentableName = presentableName;
    return this;
  }

  @NotNull
  public SquirrelExecutor withExePath(@Nullable String exePath) {
    myExePath = exePath;
    return this;
  }

  @NotNull
  public SquirrelExecutor withWorkDirectory(@Nullable String workDirectory) {
    myWorkDirectory = workDirectory;
    return this;
  }

  @NotNull
  public SquirrelExecutor withGoRoot(@Nullable String goRoot) {
    myGoRoot = goRoot;
    return this;
  }

  public SquirrelExecutor withProcessListener(@NotNull ProcessListener listener) {
    myProcessListeners.add(listener);
    return this;
  }

  @NotNull
  public SquirrelExecutor withParameterString(@NotNull String parameterString) {
    myParameterList.addParametersString(parameterString);
    return this;
  }

  @NotNull
  public SquirrelExecutor withParameters(@NotNull String... parameters) {
    myParameterList.addAll(parameters);
    return this;
  }

  @NotNull
  public SquirrelExecutor showOutputOnError() {
    myShowOutputOnError = true;
    return this;
  }

  @NotNull
  public SquirrelExecutor disablePty() {
    myPtyDisabled = true;
    return this;
  }

  @NotNull
  public SquirrelExecutor showNotifications(boolean onErrorOnly) {
    myShowNotificationsOnError = true;
    myShowNotificationsOnSuccess = !onErrorOnly;
    return this;
  }

  public boolean execute() {
    Logger.getInstance(getClass()).assertTrue(!ApplicationManager.getApplication().isDispatchThread(),
                                              "It's bad idea to run external tool on EDT");
    Logger.getInstance(getClass()).assertTrue(myProcessHandler == null, "Process has already run with this executor instance");
    final Ref<Boolean> result = Ref.create(false);
    GeneralCommandLine commandLine = null;
    try {
      commandLine = createCommandLine();

      myProcessHandler = new KillableColoredProcessHandler(commandLine);
      final SquirrelHistoryProcessListener historyProcessListener = new SquirrelHistoryProcessListener();
      myProcessHandler.addProcessListener(historyProcessListener);
      for (ProcessListener listener : myProcessListeners) {
        myProcessHandler.addProcessListener(listener);
      }

      CapturingProcessAdapter processAdapter = new CapturingProcessAdapter(myProcessOutput) {
        @Override
        public void processTerminated(@NotNull ProcessEvent event) {
          super.processTerminated(event);
          final boolean success = event.getExitCode() == 0 && myProcessOutput.getStderr().isEmpty();
          boolean nothingToShow = myProcessOutput.getStdout().isEmpty() && myProcessOutput.getStderr().isEmpty();
          final boolean cancelledByUser = (SystemInfo.isWindows || event.getExitCode() == PtyHelpers.SIGINT) && nothingToShow;
          result.set(success);
          if (success && myShowNotificationsOnSuccess) {
            showNotification("Finished successfully", NotificationType.INFORMATION);
          }
          else if (cancelledByUser && myShowNotificationsOnError) {
            showNotification("Interrupted", NotificationType.WARNING);
          }
          if (!success && !cancelledByUser && myShowOutputOnError) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
              @Override
              public void run() {
                showOutput(myProcessHandler, historyProcessListener);
              }
            });
          }
        }
      };

      myProcessHandler.addProcessListener(processAdapter);
      myProcessHandler.startNotify();
      ExecutionModes.SameThreadMode sameThreadMode = new ExecutionModes.SameThreadMode(getPresentableName());
      ExecutionHelper.executeExternalProcess(myProject, myProcessHandler, sameThreadMode, commandLine);

      LOGGER.debug("Finished `" + getPresentableName() + "` with result: " + result.get());
      return result.get();
    }
    catch (ExecutionException e) {
      if (myShowOutputOnError) {
        ExecutionHelper.showErrors(myProject, Collections.singletonList(e), getPresentableName(), null);
      }
      if (myShowNotificationsOnError) {
        showNotification(StringUtil.notNullize(e.getMessage(), "Unknown error, see logs for details"), NotificationType.ERROR);
      }
      String commandLineInfo = commandLine != null ? commandLine.getCommandLineString() : "not constructed";
      LOGGER.debug("Finished `" + getPresentableName() + "` with an exception. Commandline: " + commandLineInfo, e);
      return false;
    }
  }

  public void executeWithProgress(final boolean modal) {
    //noinspection unchecked
    executeWithProgress(modal, Consumer.EMPTY_CONSUMER);
  }

  public void executeWithProgress(final boolean modal, @NotNull final Consumer<Boolean> consumer) {
    ProgressManager.getInstance().run(new Task.Backgroundable(myProject, getPresentableName(), true) {
      private boolean doNotStart = false;

      @Override
      public void onCancel() {
        doNotStart = true;
        ProcessHandler handler = getProcessHandler();
        if (handler != null) {
          handler.destroyProcess();
        }
      }

      @Override
      public boolean shouldStartInBackground() {
        return !modal;
      }

      @Override
      public boolean isConditionalModal() {
        return modal;
      }

      public void run(@NotNull ProgressIndicator indicator) {
        if (doNotStart || myProject == null || myProject.isDisposed()) {
          return;
        }
        indicator.setIndeterminate(true);
        consumer.consume(execute());
      }
    });
  }

  @Nullable
  public ProcessHandler getProcessHandler() {
    return myProcessHandler;
  }

  private void showNotification(@NotNull final String message, final NotificationType type) {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      @Override
      public void run() {
        String title = getPresentableName();
        Notifications.Bus.notify(SquirrelConstants.SQUIRREL_EXECUTION_NOTIFICATION_GROUP.createNotification(title, message, type, null), myProject);
      }
    });
  }

  private void showOutput(@NotNull OSProcessHandler originalHandler, @NotNull SquirrelHistoryProcessListener historyProcessListener) {
    if (myShowOutputOnError) {
      BaseOSProcessHandler outputHandler = new KillableColoredProcessHandler(originalHandler.getProcess(), null);
      RunContentExecutor runContentExecutor = new RunContentExecutor(myProject, outputHandler)
        .withTitle(getPresentableName())
        .withActivateToolWindow(myShowOutputOnError);
//        .withFilter(new GoConsoleFilter(myProject, myModule, myWorkDirectory != null ? VfsUtilCore.pathToUrl(myWorkDirectory) : null));
      Disposer.register(myProject, runContentExecutor);
      runContentExecutor.run();
      historyProcessListener.apply(outputHandler);
    }
    if (myShowNotificationsOnError) {
      showNotification("Failed to run", NotificationType.ERROR);
    }
  }

  @NotNull
  public GeneralCommandLine createCommandLine() throws ExecutionException {
    if (myGoRoot == null) {
      throw new ExecutionException("Sdk is not set or Sdk home path is empty for module");
    }

    GeneralCommandLine commandLine = !myPtyDisabled && (!SystemInfo.isWindows || PtyCommandLine.isEnabled())
                                     ? new PtyCommandLine()
                                     : new GeneralCommandLine();
    commandLine.setExePath(ObjectUtils.notNull(myExePath, SquirrelSdkService.getSquirrelExecutablePath(myGoRoot)));
    commandLine.getEnvironment().putAll(myExtraEnvironment);

    Collection<String> paths = ContainerUtil.newArrayList();
    ContainerUtil.addIfNotNull(paths, StringUtil.nullize(myEnvPath, true));

    commandLine.withWorkDirectory(myWorkDirectory);
    commandLine.addParameters(myParameterList.getList());
    commandLine.setPassParentEnvironment(myPassParentEnvironment);
    commandLine.withCharset(CharsetToolkit.UTF8_CHARSET);
    EncodingEnvironmentUtil.setLocaleEnvironmentIfMac(commandLine);
    return commandLine;
  }

  @NotNull
  private String getPresentableName() {
    return ObjectUtils.notNull(myPresentableName, "squirrel");
  }
}

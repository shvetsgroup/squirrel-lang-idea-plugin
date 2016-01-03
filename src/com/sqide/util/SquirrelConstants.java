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

import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NonNls;

import java.util.Set;

public class SquirrelConstants {
    public static final String MODULE_TYPE_ID = "SQUIRREL_MODULE";
    public static final String SDK_TYPE_ID = "SQUIRREL_SDK";
    @NonNls
    public static final String SQUIRREL_VERSION_FILE_PATH = "include/squirrel.h";
    @NonNls
    public static final String SQUIRREL_COMPILER_NAME = "sq";
    @NonNls
    public static final String SQUIRREL = "squirrel";

    public static final NotificationGroup SQUIRREL_NOTIFICATION_GROUP = NotificationGroup.balloonGroup("Squirrel plugin notifications");
    public static final NotificationGroup SQUIRREL_EXECUTION_NOTIFICATION_GROUP = NotificationGroup.toolWindowGroup("Squirrel Execution", ToolWindowId.RUN);


    private SquirrelConstants() {

    }
}

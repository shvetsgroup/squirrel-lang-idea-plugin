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
  public static final String SDK_TYPE_ID = "Squirrel SDK";

  public static final String PATH = "PATH";
  public static final String SQUIRREL_ROOT = "GOROOT";
  public static final String SQUIRREL_LIBRARIES_SERVICE_NAME = "GoLibraries";
  public static final String SQUIRREL_LIBRARIES_CONFIG_FILE = "goLibraries.xml";
  public static final String SQUIRREL_BUILD_FLAGS_SERVICE_NAME = "GoBuildFlags";
  public static final String SQUIRREL_BUILD_FLAGS_CONFIG_FILE = "goBuildFlags.xml";

  public static final String TESTDATA_NAME = "testdata";
  public static final String TEST_SUFFIX = "_test";
  public static final String TEST_SUFFIX_WITH_EXTENSION = "_test.go";
  public static final String TEST_PREFIX = "Test";
  public static final String BENCHMARK_PREFIX = "Benchmark";
  public static final String EXAMPLE_PREFIX = "Example";
  public static final String TEST_MAIN = "TestMain";
  public static final String MAIN = "main";
  public static final String INIT = "init";
  public static final String DOCUMENTATION = "documentation";
  public static final String C_PATH = "C";
  public static final String TESTING_PATH = "testing";

  public static final NotificationGroup SQUIRREL_NOTIFICATION_GROUP = NotificationGroup.balloonGroup("Squirrel plugin notifications");
  public static final NotificationGroup SQUIRREL_EXECUTION_NOTIFICATION_GROUP = NotificationGroup.toolWindowGroup("Squirrel Execution", ToolWindowId.RUN);

  @NonNls
  public static final String SQUIRREL_VERSION_FILE_PATH = "include/squirrel.h";
  @NonNls
  public static final String SQUIRREL_EXECUTABLE_NAME = "sq";
  public static final String BUILD_FLAG = "+build";

  public static final String LINUX_OS = "linux";
  public static final String ANDROID_OS = "android";

  // see "$GOROOT/src/go/build/syslist.go
  public static final Set<String> KNOWN_OS = ContainerUtil.immutableSet("android", "darwin", "dragonfly", "freebsd", "linux", "nacl",
                                                                        "netbsd", "openbsd", "plan9", "solaris", "windows");
  public static final Set<String> KNOWN_ARCH = ContainerUtil.immutableSet("386", "amd64", "amd64p32", "arm", "armbe", "arm64", "arm64be",
                                                                          "ppc64", "ppc64le", "mips", "mipsle", "mips64", "mips64le",
                                                                          "mips64p32", "mips64p32le", "ppc", "s390", "s390x", "sparc",
                                                                          "sparc64");
  public static final Set<String> KNOWN_VERSIONS = ContainerUtil.immutableSet("go1.1", "go1.2", "go1.3", "go1.4");
  public static final Set<String> KNOWN_CGO = ContainerUtil.immutableSet("darwin/386", "darwin/amd64", "dragonfly/386", "dragonfly/amd64",
                                                                         "freebsd/386", "freebsd/amd64", "freebsd/arm", "linux/386",
                                                                         "linux/amd64", "linux/arm", "linux/arm64", "android/386",
                                                                         "android/amd64", "android/arm", "netbsd/386", "netbsd/amd64",
                                                                         "netbsd/arm", "openbsd/386", "openbsd/amd64", "windows/386",
                                                                         "windows/amd64", "linux/ppc64le");
  public static final Set<String> KNOWN_COMPILERS = ContainerUtil.immutableSet("gc", "gccgo");

  @NonNls
  public static final String NIL = "nil";

  @NonNls
  public static final String SQUIRREL = "Squirrel";

  private SquirrelConstants() {

  }
}

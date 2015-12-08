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

package com.squirrelplugin.parser;

import com.squirrelplugin.psi.SquirrelTypes;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.indexing.IndexingDataKeys;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntProcedure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class SquirrelParserUtil extends GeneratedParserUtilBase {

    public static boolean previousBrace(@NotNull PsiBuilder builder_, @SuppressWarnings("UnusedParameters") int level) {
        LighterASTNode marker = builder_.getLatestDoneMarker();

        if (marker == null) return false;

        String str = marker.toString().replaceAll("\\s+$", "");

        if (str.length() == 0) return false;

        str = "" + str.charAt(str.length() - 1);

        return str.equals("}");
    }
}
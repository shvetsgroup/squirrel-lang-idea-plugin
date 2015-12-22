package com.sqide.sdk;

import com.intellij.openapi.roots.libraries.DummyLibraryProperties;
import com.intellij.openapi.roots.libraries.LibraryKind;
import com.intellij.openapi.roots.libraries.LibraryPresentationProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.sqide.SquirrelIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class SquirrelSdkLibraryPresentationProvider extends LibraryPresentationProvider<DummyLibraryProperties> {
    private static final LibraryKind KIND = LibraryKind.create("go");

    public SquirrelSdkLibraryPresentationProvider() {
        super(KIND);
    }

    @Nullable
    public Icon getIcon() {
        return SquirrelIcons.NUT_FILE;
    }

    @Nullable
    public DummyLibraryProperties detect(@NotNull List<VirtualFile> classesRoots) {
        for (VirtualFile root : classesRoots) {
            if (SquirrelSmallIDEsSdkService.isSquirrelSdkLibRoot(root)) {
                return DummyLibraryProperties.INSTANCE;
            }
        }
        return null;
    }
}

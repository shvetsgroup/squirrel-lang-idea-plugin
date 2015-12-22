package com.sqide;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import static com.sqide.SquirrelTokenTypes.*;
import static com.sqide.SquirrelTokenTypesSets.*;
import org.jetbrains.annotations.Nullable;

public class SquirrelCommenter implements CodeDocumentationAwareCommenter {
    public String getLineCommentPrefix() {
        return "//";
    }

    public String getBlockCommentPrefix() {
        return "/*";
    }

    public String getBlockCommentSuffix() {
        return "*/";
    }

    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    public String getCommentedBlockCommentSuffix() {
        return null;
    }

    @Nullable
    public IElementType getLineCommentTokenType() {
        return SINGLE_LINE_COMMENT;
    }

    @Nullable
    public IElementType getBlockCommentTokenType() {
        return MULTI_LINE_COMMENT;
    }

    public String getDocumentationCommentPrefix() {
        return "/**";
    }

    public String getDocumentationCommentLinePrefix() {
        return "*";
    }

    public String getDocumentationCommentSuffix() {
        return "*/";
    }

    public boolean isDocumentationComment(final PsiComment element) {
        return element.getTokenType() == MULTI_LINE_DOC_COMMENT;
    }

    @Nullable
    public IElementType getDocumentationCommentTokenType() {
        return MULTI_LINE_DOC_COMMENT;
    }
}
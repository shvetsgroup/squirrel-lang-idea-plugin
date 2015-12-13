package com.squirrelplugin.formatter;

import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.BlockWithParent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.squirrelplugin.SquirrelFileType;
import com.squirrelplugin.SquirrelLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.squirrelplugin.SquirrelTokenTypes.*;

public class SquirrelBlock extends AbstractBlock implements BlockWithParent {
    public static final List<SquirrelBlock> Squirrel_EMPTY = Collections.emptyList();

    private static final TokenSet STATEMENTS_WITH_OPTIONAL_BRACES = TokenSet.create(IF, DO, WHILE, FOR, FOREACH, FUNCTION, CLASS);

    private static final TokenSet LAST_TOKENS_IN_SWITCH_CASE = TokenSet.create(BREAK_STATEMENT, CONTINUE_STATEMENT, RETURN_STATEMENT);

    private final SquirrelIndentProcessor myIndentProcessor;
    private SpacingBuilder spacingBuilder; // TODO: Remove
    private final SquirrelSpacingProcessor mySpacingProcessor;
    private final SquirrelWrappingProcessor myWrappingProcessor;
//    private final SquirrelAlignmentProcessor myAlignmentProcessor;
    private final CodeStyleSettings mySettings;
    private Wrap myChildWrap = null;
    private final Indent myIndent;
    private BlockWithParent myParent;
    private List<SquirrelBlock> mySubSquirrelBlocks;

    protected SquirrelBlock(ASTNode node, Wrap wrap, Alignment alignment, CodeStyleSettings settings) {
        super(node, wrap, alignment);
        mySettings = settings;
        myIndentProcessor = new SquirrelIndentProcessor(mySettings.getCommonSettings(SquirrelLanguage.INSTANCE));
        mySpacingProcessor = new SquirrelSpacingProcessor(node, mySettings.getCommonSettings(SquirrelLanguage.INSTANCE));
        myWrappingProcessor = new SquirrelWrappingProcessor(node, mySettings.getCommonSettings(SquirrelLanguage.INSTANCE));
//        myAlignmentProcessor = new SquirrelAlignmentProcessor(node, mySettings.getCommonSettings(SquirrelLanguage.INSTANCE));
        myIndent = myIndentProcessor.getChildIndent(myNode);
    }

    @Override
    public Indent getIndent() {
        return myIndent;
    }

    @Override
    public Spacing getSpacing(Block child1, @NotNull Block child2) {
        //return mySpacingProcessor.getSpacing(child1, child2);
        return null;
    }

    @Override
    protected List<Block> buildChildren() {
        if (isLeaf()) {
            return EMPTY;
        }
        final ArrayList<Block> tlChildren = new ArrayList<Block>();
        for (ASTNode childNode = getNode().getFirstChildNode(); childNode != null; childNode = childNode.getTreeNext()) {
            if (FormatterUtil.containsWhiteSpacesOnly(childNode)) continue;
            final SquirrelBlock childBlock = new SquirrelBlock(childNode, createChildWrap(childNode), createChildAlignment(childNode), mySettings);
            childBlock.setParent(this);
            tlChildren.add(childBlock);
        }
        return tlChildren;
    }

    public Wrap createChildWrap(ASTNode child) {
        final IElementType childType = child.getElementType();
        final Wrap wrap = myWrappingProcessor.createChildWrap(child, Wrap.createWrap(WrapType.NONE, false), myChildWrap);

        if (childType == EQ) {
            myChildWrap = wrap;
        }
        return wrap;
    }

    @Nullable
    protected Alignment createChildAlignment(ASTNode child) {
//        if (child.getElementType() != LPAREN && child.getElementType() != BLOCK) {
//            return myAlignmentProcessor.createChildAlignment();
//        }
        return null;
    }

    @NotNull
    @Override
    public ChildAttributes getChildAttributes(final int newIndex) {
        final IElementType elementType = myNode.getElementType();
        final SquirrelBlock previousBlock = newIndex == 0 ? null : getSubSquirrelBlocks().get(newIndex - 1);
        final IElementType previousType = previousBlock == null ? null : previousBlock.getNode().getElementType();

        if (previousType == LBRACE || previousType == LBRACKET) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }

        if (previousType == RPAREN && STATEMENTS_WITH_OPTIONAL_BRACES.contains(elementType)) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }

        if (previousType == COLON && (elementType == SWITCH_CASE || elementType == DEFAULT_CASE)) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }

        if (previousType == SWITCH_CASE || previousType == DEFAULT_CASE) {
            if (previousBlock != null) {
                final List<SquirrelBlock> subBlocks = previousBlock.getSubSquirrelBlocks();
                if (!subBlocks.isEmpty()) {
                    final SquirrelBlock lastChildInPrevBlock = subBlocks.get(subBlocks.size() - 1);
                    final List<SquirrelBlock> subSubBlocks = lastChildInPrevBlock.getSubSquirrelBlocks();
                    if (isLastTokenInSwitchCase(subSubBlocks)) {
                        return new ChildAttributes(Indent.getNormalIndent(), null);  // e.g. Enter after BREAK_STATEMENT
                    }
                }
            }

            final int indentSize = mySettings.getIndentSize(SquirrelFileType.INSTANCE) * 2;
            return new ChildAttributes(Indent.getIndent(Indent.Type.SPACES, indentSize, false, false), null);
        }

        if (previousBlock == null) {
            return new ChildAttributes(Indent.getNoneIndent(), null);
        }

        if (myParent == null && isIncomplete()) {
            return new ChildAttributes(Indent.getContinuationIndent(), null);
        }
        return new ChildAttributes(previousBlock.getIndent(), previousBlock.getAlignment());
    }

    private static boolean isLastTokenInSwitchCase(@NotNull final List<SquirrelBlock> blocks) {
        int size = blocks.size();
        // No blocks.
        if (size == 0) {
            return false;
        }
        // [return x;]
        SquirrelBlock lastBlock = blocks.get(size - 1);
        final IElementType type = lastBlock.getNode().getElementType();
        if (LAST_TOKENS_IN_SWITCH_CASE.contains(type)) {
            return true;
        }
        // [throw expr][;]
        if (type == SEMICOLON && size > 1) {
            SquirrelBlock lastBlock2 = blocks.get(size - 2);
            return lastBlock2.getNode().getElementType() == THROW_STATEMENT;
        }
        return false;
    }

    public List<SquirrelBlock> getSubSquirrelBlocks() {
        if (mySubSquirrelBlocks == null) {
            mySubSquirrelBlocks = new ArrayList<SquirrelBlock>();
            for (Block block : getSubBlocks()) {
                mySubSquirrelBlocks.add((SquirrelBlock)block);
            }
            mySubSquirrelBlocks = !mySubSquirrelBlocks.isEmpty() ? mySubSquirrelBlocks : Squirrel_EMPTY;
        }
        return mySubSquirrelBlocks;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public BlockWithParent getParent() {
        return myParent;
    }

    @Override
    public void setParent(BlockWithParent newParent) {
        myParent = newParent;
    }
}
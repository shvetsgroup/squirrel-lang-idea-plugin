package com.sqide.actions;

import com.intellij.CommonBundle;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.CreateFileAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.sqide.SquirrelBundle;
import com.sqide.SquirrelFileType;
import com.sqide.SquirrelIcons;
import org.jetbrains.annotations.NotNull;

/**
 * Action to create a new Bash file from a template.
 * <p/>
 * The template data is stored in resources/fileTemplates/internal/Bash Script.sh.ft
 *
 * @author Joachim Ansorg
 */
public class NewSquirrelFileAction extends CreateElementActionBase {
    public NewSquirrelFileAction() {
        super(SquirrelBundle.message("newfile.menu.action.text"), SquirrelBundle.message("newfile.menu.action.description"), SquirrelIcons.NUT_FILE);
    }

    static String computeFilename(String inputFilename) {
        String usedExtension = FileUtilRt.getExtension(inputFilename);
        boolean withExtension = !usedExtension.isEmpty();

        return withExtension ? inputFilename : (inputFilename + "." + SquirrelFileType.EXTENSION);
    }

    protected String getDialogPrompt() {
        return SquirrelBundle.message("newfile.dialog.prompt");
    }

    protected String getDialogTitle() {
        return SquirrelBundle.message("newfile.dialog.title");
    }

    protected String getCommandName() {
        return SquirrelBundle.message("newfile.command.name");
    }

    protected String getActionName(PsiDirectory directory, String newName) {
        return SquirrelBundle.message("newfile.menu.action.text");
    }

    @NotNull
    protected final PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
        final MyInputValidator validator = new MyInputValidator(project, directory);
        Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", validator);

        return validator.getCreatedElements();
    }

    @NotNull
    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        CreateFileAction.MkDirs mkdirs = new CreateFileAction.MkDirs(newName, directory);
        return new PsiElement[]{mkdirs.directory.createFile(computeFilename(mkdirs.newName))};
    }

    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }
}
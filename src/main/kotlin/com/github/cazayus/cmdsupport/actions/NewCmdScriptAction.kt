package com.github.cazayus.cmdsupport.actions

import com.github.cazayus.cmdsupport.CmdFileType
import com.github.cazayus.cmdsupport.MyBundle
import com.intellij.CommonBundle
import com.intellij.ide.actions.CreateElementActionBase
import com.intellij.ide.ui.newItemPopup.NewItemPopupUtil
import com.intellij.ide.ui.newItemPopup.NewItemSimplePopupPanel
import com.intellij.lang.LangBundle
import com.intellij.openapi.application.Experiments
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import java.awt.event.InputEvent
import java.util.function.Consumer

class NewCmdScriptAction : CreateElementActionBase() {
    override fun getActionName(directory: PsiDirectory, newName: String): String = "NewCmdScriptAction"

    override fun getErrorTitle(): String = CommonBundle.getErrorTitle()

    override fun create(newName: String, directory: PsiDirectory): Array<PsiElement> {
        val fixExtension = when (FileUtilRt.getExtension(newName).lowercase()) {
            "cmd", "bat" -> newName
            else -> "$newName.cmd"
        }
        val project = directory.project
        val factory = PsiFileFactory.getInstance(project)
        val file = factory.createFileFromText(fixExtension, CmdFileType.INSTANCE, "")
        return arrayOf(directory.add(file))
    }

    override fun invokeDialog(
        project: Project,
        directory: PsiDirectory,
        elementsConsumer: Consumer<in Array<PsiElement>>
    ) {
        val validator = MyInputValidator(project, directory)
        if (Experiments.getInstance().isFeatureEnabled("show.create.new.element.in.popup")) {
            createLightWeightPopup(validator, elementsConsumer).showCenteredInCurrentWindow(project)
        } else {
            Messages.showInputDialog(
                project, MyBundle.message("Cmd.New.newCmdScriptDialog"),
                MyBundle.message("Cmd.New.newCmdScript"), null, null, validator
            )
            elementsConsumer.accept(validator.createdElements)
        }
    }

    private fun createLightWeightPopup(
        validator: MyInputValidator,
        consumer: Consumer<in Array<PsiElement>>
    ): JBPopup {
        val contentPanel = NewItemSimplePopupPanel()
        val nameField = contentPanel.textField
        val popup =
            NewItemPopupUtil.createNewItemPopup(MyBundle.message("Cmd.New.newCmdScript"), contentPanel, nameField)
        contentPanel.applyAction = com.intellij.util.Consumer { event: InputEvent? ->
            val name = nameField.text
            if (validator.checkInput(name) && validator.canClose(name)) {
                popup.closeOk(event)
                consumer.accept(validator.createdElements)
            } else {
                val errorMessage = if (validator is InputValidatorEx)
                    (validator as InputValidatorEx).getErrorText(name)
                else
                    LangBundle.message("incorrect.name")
                contentPanel.setError(errorMessage)
            }
        }

        return popup
    }
}
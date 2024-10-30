package com.github.cazayus.cmdsupport.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class RunCmdScriptAction : AnAction() {
    private var extensions = setOf("cmd", "bat")

    override fun actionPerformed(e: AnActionEvent) {
        val files = compatibleFiles(e)
        files.forEach {
            val documentManager = FileDocumentManager.getInstance()
            documentManager.saveDocument(documentManager.getDocument(it)!!)
            val path = it.path
            Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", "Run cmd script", path), null, File(it.parent.path))
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val data = e.getData(CommonDataKeys.PROJECT)
        if (data != null && !data.isDefault) {
            e.presentation.isVisible = SystemInfo.isWindows
            e.presentation.isEnabled = compatibleFiles(e).isNotEmpty()
        }
    }

    private fun compatibleFiles(e: AnActionEvent): Array<VirtualFile> {
        return when (val files = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)) {
            null -> emptyArray()
            else -> files.filter { file -> extensions.contains(file.extension) }.toTypedArray()
        }
    }
}
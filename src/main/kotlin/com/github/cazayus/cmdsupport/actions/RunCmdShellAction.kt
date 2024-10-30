package com.github.cazayus.cmdsupport.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.SystemInfo
import java.io.File

class RunCmdShellAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        val path = when {
            file == null -> CommonDataKeys.PROJECT.getData(e.dataContext)!!.basePath!!
            file.isDirectory -> file.path
            else -> file.parent.path
        }
        Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start"), null, File(path))
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = SystemInfo.isWindows
    }
}
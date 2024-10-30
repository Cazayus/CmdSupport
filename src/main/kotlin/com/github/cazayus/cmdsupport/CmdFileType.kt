package com.github.cazayus.cmdsupport

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

@Suppress("CompanionObjectInExtension", "ExtensionClassShouldBeFinalAndNonPublic")
class BatFileType : LanguageFileType(BatLanguage) {
    override fun getIcon(): Icon = MyIcons.BatIcon
    override fun getDefaultExtension() = "bat"
    override fun getDescription() = MyBundle.message("fileType.batScript")
    override fun getName() = "Bat"

    companion object {
        @JvmField
        val INSTANCE = BatFileType()
    }
}

object BatLanguage : Language(
    "Bat",
    "application/x-batch",
    "application/x-bat",
    "text/x-script.bat",
) {
    private fun readResolve(): Any = BatLanguage
}

@Suppress("CompanionObjectInExtension", "ExtensionClassShouldBeFinalAndNonPublic")
class CmdFileType : LanguageFileType(CmdLanguage) {
    override fun getIcon(): Icon = MyIcons.CmdIcon
    override fun getDefaultExtension() = "cmd"
    override fun getDescription() = MyBundle.message("fileType.cmdScript")
    override fun getName() = "Cmd"

    companion object {
        @JvmField
        val INSTANCE = CmdFileType()
    }
}

object CmdLanguage : Language(
    "Cmd",
    "application/x-cmd",
    "text/x-script.cmd"
) {
    private fun readResolve(): Any = CmdLanguage
}
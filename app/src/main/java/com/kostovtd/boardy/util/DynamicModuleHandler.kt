package com.kostovtd.boardy.util

import android.content.Context
import android.content.Intent
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.kostovtd.boardy.BuildConfig

/**
 * Created by tosheto on 1.02.21.
 */
class DynamicModuleHandler(val listener: DynamicModuleListener) {


    fun downloadAndInstallModule(context: Context, moduleName: String) {
        val request = SplitInstallRequest.newBuilder()
            .addModule(moduleName)
            .build()
        val manager = SplitInstallManagerFactory.create(context)

        manager.startInstall(request)

        manager.registerListener {
            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    listener?.onDynamicModuleDownloading()
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    listener?.onDynamicModuleInstalled()
                }
                else -> {
                    listener?.onDynamicModuleError()
                }
            }
        }
    }


    fun startModule(context: Context, packageName: String, activityName: String) {
        val intent = Intent()
        intent.setClassName(
            BuildConfig.APPLICATION_ID,
            "com.kostovtd.${packageName}.views.activities.${activityName}"
        )
        context.startActivity(intent)
    }


    fun isModuleInstalled(context: Context, moduleName: String): Boolean =
        SplitInstallManagerFactory.create(context).installedModules.contains(moduleName)


    fun uninstallModules(context: Context, moduleNames: List<String>) {
        moduleNames.forEach { moduleName ->
            if(isModuleInstalled(context, moduleName)) {
                SplitInstallManagerFactory.create(context).deferredUninstall(moduleNames)
            }
        }
    }
}


interface DynamicModuleListener {
    fun onDynamicModuleDownloading()
    fun onDynamicModuleInstalled()
    fun onDynamicModuleError()
}
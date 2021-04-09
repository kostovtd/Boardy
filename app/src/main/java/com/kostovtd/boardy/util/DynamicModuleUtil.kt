package com.kostovtd.boardy.util

import android.content.Context
import android.content.Intent
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.kostovtd.boardy.BuildConfig
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.PlayerType

/**
 * Created by tosheto on 1.02.21.
 */
fun downloadAndInstallModule(
    context: Context,
    moduleName: String,
    listener: DynamicModuleListener
) {
    val request = SplitInstallRequest.newBuilder()
        .addModule(moduleName)
        .build()
    val manager = SplitInstallManagerFactory.create(context)

    manager.startInstall(request)

    manager.registerListener {
        when (it.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                listener.onDynamicModuleDownloading()
            }
            SplitInstallSessionStatus.INSTALLED -> {
                listener.onDynamicModuleInstalled()
            }
            else -> {
                listener.onDynamicModuleError()
            }
        }
    }
}

fun uninstallModules(context: Context, moduleNames: List<String>) {
    moduleNames.forEach { moduleName ->
        if (isModuleInstalled(context, moduleName)) {
            SplitInstallManagerFactory.create(context).deferredUninstall(moduleNames)
        }
    }
}

fun startBoardGameModuleAs(playerType: PlayerType, context: Context, boardGameGameSession: BoardGameGameSession) {
    val intent = Intent()

    intent.setClassName(
        BuildConfig.APPLICATION_ID,
        "com.kostovtd.${boardGameGameSession.packageName}.views.activities.${boardGameGameSession.activityName}"
    )

    intent.putExtra(Constants.PLAYER_TYPE_KEY, playerType)
    intent.putExtra(Constants.BOARD_GAME_GAME_SESSION_KEY, boardGameGameSession)

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
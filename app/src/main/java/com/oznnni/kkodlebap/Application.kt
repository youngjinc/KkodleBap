package com.oznnni.kkodlebap

import android.app.Application
import timber.log.Timber

class KkodleBapDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement) =
        "${element.fileName}:${element.lineNumber}#${element.methodName}"
}

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(KkodleBapDebugTree())
    }
}

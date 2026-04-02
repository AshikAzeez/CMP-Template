package org.cmp_arch.core

import android.content.Context
import android.os.Build

actual class PlatformContext(
    val androidContext: Context,
)

actual fun dataStoreAbsolutePath(context: PlatformContext): String =
    context.androidContext.filesDir.resolve("cmp_arch.preferences_pb").absolutePath

actual fun runningPlatformName(): String = "Android ${Build.VERSION.SDK_INT}"

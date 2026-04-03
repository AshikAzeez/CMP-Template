package org.cmp_arch.core

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class PlatformContext

actual fun dataStoreAbsolutePath(context: PlatformContext): String {
    val urls = NSFileManager.defaultManager.URLsForDirectory(
        directory = NSDocumentDirectory,
        inDomains = NSUserDomainMask,
    )
    val directory = (urls.firstOrNull() as? NSURL)?.path ?: "."
    return "$directory/cmp_arch.preferences_pb"
}

actual fun runningPlatformName(): String = "iOS"

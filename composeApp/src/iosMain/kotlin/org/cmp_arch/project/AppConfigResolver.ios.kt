package org.cmp_arch.project

import kotlinx.cinterop.ExperimentalForeignApi
import org.cmp_arch.core.PlatformContext
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
internal actual fun readConfigJson(platformContext: PlatformContext): String? {
    val path = NSBundle.mainBundle.pathForResource(name = "config", ofType = "json") ?: return null
    return NSString.stringWithContentsOfFile(
        path = path,
        encoding = 4u,
        error = null,
    )
}

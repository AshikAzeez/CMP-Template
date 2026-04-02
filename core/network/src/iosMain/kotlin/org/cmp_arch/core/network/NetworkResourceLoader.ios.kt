package org.cmp_arch.core.network

import org.cmp_arch.core.PlatformContext
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile

actual class NetworkResourceLoader actual constructor(
    context: PlatformContext,
) {
    actual fun readText(path: String): String? {
        val dotIndex = path.lastIndexOf('.')
        val fileName = if (dotIndex > 0) path.substring(0, dotIndex) else path
        val extension = if (dotIndex > 0) path.substring(dotIndex + 1) else ""

        val bundlePath = NSBundle.mainBundle.pathForResource(
            name = fileName,
            ofType = extension,
        ) ?: return null

        return NSString.stringWithContentsOfFile(
            path = bundlePath,
            encoding = 4u,
            error = null,
        ) as String?
    }
}

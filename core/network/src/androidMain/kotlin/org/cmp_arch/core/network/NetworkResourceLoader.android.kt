package org.cmp_arch.core.network

import org.cmp_arch.core.PlatformContext

actual class NetworkResourceLoader actual constructor(
    private val context: PlatformContext,
) {
    actual fun readText(path: String): String? {
        return runCatching {
            context.androidContext.assets.open(path).bufferedReader().use { it.readText() }
        }.getOrNull()
    }
}

package org.cmp_arch.project

import org.cmp_arch.core.PlatformContext

internal actual fun readConfigJson(platformContext: PlatformContext): String? {
    return runCatching {
        platformContext.androidContext.assets.open("config/config.json").bufferedReader().use { it.readText() }
    }.getOrNull()
}

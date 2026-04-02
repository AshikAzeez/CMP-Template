package org.cmp_arch.core.network

import org.cmp_arch.core.PlatformContext

expect class NetworkResourceLoader(context: PlatformContext) {
    fun readText(path: String): String?
}

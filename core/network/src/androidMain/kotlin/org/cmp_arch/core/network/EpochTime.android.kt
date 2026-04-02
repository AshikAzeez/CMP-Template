package org.cmp_arch.core.network

internal actual fun currentEpochSeconds(): Long {
    return System.currentTimeMillis() / 1_000
}

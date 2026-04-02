package org.cmp_arch.core.network

import platform.Foundation.NSDate

internal actual fun currentEpochSeconds(): Long {
    return NSDate().timeIntervalSince1970.toLong()
}

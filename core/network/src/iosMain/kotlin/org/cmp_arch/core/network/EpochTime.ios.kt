package org.cmp_arch.core.network

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

internal actual fun currentEpochSeconds(): Long {
    return NSDate().timeIntervalSince1970.toLong()
}

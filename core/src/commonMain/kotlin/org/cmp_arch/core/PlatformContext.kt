package org.cmp_arch.core

expect class PlatformContext

expect fun dataStoreAbsolutePath(context: PlatformContext): String
expect fun runningPlatformName(): String

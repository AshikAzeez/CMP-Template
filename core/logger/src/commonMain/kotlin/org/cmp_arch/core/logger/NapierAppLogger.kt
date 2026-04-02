package org.cmp_arch.core.logger

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

class NapierAppLogger : AppLogger {
    override fun verbose(tag: String, message: String) {
        Napier.v(message = message, tag = tag)
    }

    override fun debug(tag: String, message: String) {
        Napier.d(message = message, tag = tag)
    }

    override fun info(tag: String, message: String) {
        Napier.i(message = message, tag = tag)
    }

    override fun warning(tag: String, message: String) {
        Napier.w(message = message, tag = tag)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        Napier.e(message = message, throwable = throwable, tag = tag)
    }
}

private object NoOpAntilog : Antilog() {
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?,
    ) = Unit
}

fun initNapier(enableDebug: Boolean) {
    if (enableDebug) {
        Napier.base(DebugAntilog())
    } else {
        Napier.base(NoOpAntilog)
    }
}

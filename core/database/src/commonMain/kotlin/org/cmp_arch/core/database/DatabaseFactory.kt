package org.cmp_arch.core.database

import org.cmp_arch.core.PlatformContext

expect class DatabaseFactory(context: PlatformContext) {
    fun createTemplateEntityStore(): TemplateEntityStore
}

package org.cmp_arch.project

import androidx.compose.ui.window.ComposeUIViewController
import org.cmp_arch.core.PlatformContext

fun MainViewController() = ComposeUIViewController {
    val platformContext = PlatformContext()
    initKoin(
        platformContext = platformContext,
        config = resolveAppConfig(platformContext),
    )
    App()
}

package org.cmp_arch.project

import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.cmp_arch.core.PlatformContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val platformContext = PlatformContext(applicationContext)
        val resolved = resolveAppConfig(platformContext)
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        initKoin(
            platformContext = platformContext,
            config = resolved.copy(enableNetworkLogs = resolved.enableNetworkLogs && isDebuggable),
        )

        setContent {
            App()
        }
    }
}

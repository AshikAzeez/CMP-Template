package org.cmp_arch.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.cmp_arch.core.ThemeMode
import org.cmp_arch.core.ThemeSettingsStore
import org.cmp_arch.designsystem.theme.CmpTheme
import org.cmp_arch.feature.home.HOME_ROUTE
import org.cmp_arch.feature.home.homeGraph
import org.cmp_arch.feature.home.sampleGraph
import org.cmp_arch.feature.home.SAMPLE_ROUTE
import org.koin.core.context.GlobalContext

@Composable
fun App() {
    val navController = rememberNavController()
    val themeSettingsStore: ThemeSettingsStore = remember { GlobalContext.get().get() }
    val themeMode by themeSettingsStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val isDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    CmpTheme(darkTheme = isDarkTheme) {
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
        ) {
            homeGraph(onSampleRequested = {
                navController.navigate(SAMPLE_ROUTE)
            })

            sampleGraph(onBackRequested = { navController.popBackStack() })
        }
    }
}

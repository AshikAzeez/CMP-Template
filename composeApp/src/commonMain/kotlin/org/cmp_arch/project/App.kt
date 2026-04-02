package org.cmp_arch.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.cmp_arch.designsystem.theme.CmpTheme
import org.cmp_arch.feature.home.HOME_ROUTE
import org.cmp_arch.feature.home.homeGraph
import org.cmp_arch.feature.home.sampleGraph
import org.cmp_arch.feature.home.SAMPLE_ROUTE

@Composable
fun App() {
    val navController = rememberNavController()

    CmpTheme(darkTheme = isSystemInDarkTheme()) {
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

package org.cmp_arch.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
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
import org.koin.mp.KoinPlatform

@Composable
fun App() {
    val navController = rememberNavController()
    val themeSettingsStore: ThemeSettingsStore = remember { KoinPlatform.getKoin().get() }
    val themeMode by themeSettingsStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val isDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    CmpTheme(darkTheme = isDarkTheme) {
        if (isIosPlatform) {
            IosNavHost(navController = navController)
        } else {
            AndroidNavHost(navController = navController)
        }
    }
}

@Composable
private fun AndroidNavHost(navController: androidx.navigation.NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
    ) {
        homeGraph(onSampleRequested = {
            navController.navigate(SAMPLE_ROUTE) {
                launchSingleTop = true
            }
        })

        sampleGraph(onBackRequested = { navController.popBackStack() })
    }
}

@Composable
private fun IosNavHost(navController: androidx.navigation.NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        enterTransition = { iosRouteEnterTransition() },
        exitTransition = { iosRouteExitTransition() },
        popEnterTransition = { iosRoutePopEnterTransition() },
        popExitTransition = { iosRoutePopExitTransition() },
    ) {
        homeGraph(onSampleRequested = {
            navController.navigate(SAMPLE_ROUTE) {
                launchSingleTop = true
            }
        })

        sampleGraph(onBackRequested = { navController.popBackStack() })
    }
}

private fun AnimatedContentTransitionScope<*>.iosRouteEnterTransition(): EnterTransition {
    return slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = IOS_NAV_DURATION_MS,
                easing = IOS_NAV_EASING,
            ),
        )
}

private fun AnimatedContentTransitionScope<*>.iosRouteExitTransition(): ExitTransition {
    return slideOutHorizontally(
            targetOffsetX = { fullWidth -> -(fullWidth / 3) },
            animationSpec = tween(
                durationMillis = IOS_NAV_DURATION_MS,
                easing = IOS_NAV_EASING,
            ),
        )
}

private fun AnimatedContentTransitionScope<*>.iosRoutePopEnterTransition(): EnterTransition {
    return slideInHorizontally(
            initialOffsetX = { fullWidth -> -(fullWidth / 3) },
            animationSpec = tween(
                durationMillis = IOS_NAV_DURATION_MS,
                easing = IOS_NAV_EASING,
            ),
        )
}

private fun AnimatedContentTransitionScope<*>.iosRoutePopExitTransition(): ExitTransition {
    return slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = IOS_NAV_DURATION_MS,
                easing = IOS_NAV_EASING,
            ),
        )
}

private const val IOS_NAV_DURATION_MS = 360
private val IOS_NAV_EASING = CubicBezierEasing(0.32f, 0.72f, 0f, 1f)

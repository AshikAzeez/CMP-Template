package org.cmp_arch.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_ROUTE = "home"
const val SAMPLE_ROUTE = "sample"

fun NavController.navigateToHome() {
    navigate(HOME_ROUTE) {
        popUpTo(HOME_ROUTE) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeGraph(
    onArticleRequested: (String) -> Unit,
    onSampleRequested: () -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onArticleRequested = onArticleRequested,
            onSampleRequested = onSampleRequested,
        )
    }
}

fun NavGraphBuilder.sampleGraph(
    onBackRequested: () -> Unit,
) {
    composable(route = SAMPLE_ROUTE) {
        SampleTemplateScreen(onBackRequested = onBackRequested)
    }
}

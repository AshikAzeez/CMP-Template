package org.cmp_arch.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.cmp_arch.designsystem.components.DsTopAppBar
import org.cmp_arch.designsystem.theme.CmpTheme
import org.cmp_arch.feature.home.HOME_ROUTE
import org.cmp_arch.feature.home.homeGraph
import org.cmp_arch.feature.home.sampleGraph
import org.cmp_arch.feature.home.SAMPLE_ROUTE

private const val ARTICLE_DETAIL_ROUTE = "article/{articleId}"

@Composable
fun App() {
    val navController = rememberNavController()

    CmpTheme(darkTheme = isSystemInDarkTheme()) {
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
        ) {
            homeGraph(onArticleRequested = { articleId ->
                navController.navigate("article/$articleId")
            }, onSampleRequested = {
                navController.navigate(SAMPLE_ROUTE)
            })

            composable(
                route = ARTICLE_DETAIL_ROUTE,
                arguments = listOf(navArgument("articleId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId").orEmpty()
                ArticleDetailScreen(articleId = articleId)
            }

            sampleGraph(onBackRequested = { navController.popBackStack() })
        }
    }
}

@Composable
private fun ArticleDetailScreen(articleId: String) {
    Scaffold(
        topBar = {
            DsTopAppBar(title = "Article")
        },
    ) { paddingValues ->
        Text(
            text = "Requested article: $articleId",
            modifier = Modifier.padding(paddingValues),
        )
    }
}

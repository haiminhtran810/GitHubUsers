package tmh.nhoctax.githubusers.feature.favorites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import tmh.nhoctax.githubusers.core.navigation.AppDeepLinks
import tmh.nhoctax.githubusers.feature.favorites.presentation.FavoriteScreen

// adb shell am start -W -a android.intent.action.VIEW -d "https://nhoctax.com/users" tmh.nhoctax.githubusers.dev
fun NavGraphBuilder.favoriteNavGraph() {
    composable<FavoriteDestination>(
        deepLinks = listOf(
            navDeepLink<FavoriteDestination>(basePath = AppDeepLinks.FAVORITE_PATH)
        )
    ) {
        FavoriteScreen()
    }
}
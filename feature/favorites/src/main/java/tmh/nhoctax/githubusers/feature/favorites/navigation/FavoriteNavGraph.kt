package tmh.nhoctax.githubusers.feature.favorites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import tmh.nhoctax.githubusers.feature.favorites.presentation.FavoriteScreen

fun NavGraphBuilder.favoriteNavGraph() {
    composable<FavoriteDestination> {
        FavoriteScreen()
    }
}
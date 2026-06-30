package tmh.nhoctax.githubusers.feature.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import tmh.nhoctax.githubusers.feature.user.presentation.userdetail.UserDetailScreen
import tmh.nhoctax.githubusers.feature.user.presentation.userlist.UserListContentScreen
import tmh.nhoctax.githubusers.feature.user.presentation.userlist.UserListScreen

fun NavGraphBuilder.usersNavGraph() {
    composable<UserListDestination> {
        UserListScreen()
    }

    composable<UserDetailDestination> {
        UserDetailScreen()
    }
}
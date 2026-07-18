package tmh.nhoctax.githubusers.feature.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import tmh.nhoctax.githubusers.core.navigation.AppDeepLinks
import tmh.nhoctax.githubusers.feature.user.presentation.userdetail.UserDetailScreen
import tmh.nhoctax.githubusers.feature.user.presentation.userlist.UserListScreen

fun NavGraphBuilder.usersNavGraph() {
    // adb shell am start -W -a android.intent.action.VIEW -d "https://nhoctax.com/users" tmh.nhoctax.githubusers.dev
    composable<UserListDestination>(
        deepLinks = listOf(
            navDeepLink<UserListDestination>(basePath = AppDeepLinks.USER_LIST_PATH)
        )
    ) {
        UserListScreen()
    }

    // adb shell am start -W -a android.intent.action.VIEW -d "https://nhoctax.com/users/mojombo" tmh.nhoctax.githubusers.dev
    composable<UserDetailDestination>(
        deepLinks = listOf(
            navDeepLink<UserDetailDestination>(basePath = AppDeepLinks.USER_DETAILS_PATH)
        )
    ) {
        UserDetailScreen()
    }
}
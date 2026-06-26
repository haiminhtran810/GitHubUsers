package tmh.nhoctax.githubusers.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tmh.nhoctax.githubusers.feature.favorites.navigation.FavoriteDestination
import tmh.nhoctax.githubusers.feature.favorites.navigation.favoriteNavGraph
import tmh.nhoctax.githubusers.feature.user.navigation.UserListDestination
import tmh.nhoctax.githubusers.feature.user.navigation.usersNavGraph
import tmh.nhoctax.githubusers.core.navigation.AppNavigator


@Composable
fun AppNavHostScreen(
    appNavigator: AppNavigator
) {
    // Create and remember a NavController to manage app navigation
    val navController = rememberNavController()
    // Observe the navigation state to determine the current destination.
    // This triggers recomposition whenever the user navigates to a new screen.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AppNavigationHandler(
        appNavigator = appNavigator,
        navController = navController
    )

    // Determine if the bottom navigation bar should be visible.
    // We only show it on the top-level destinations (User List and Favorites).
    val showBottomBar = currentDestination?.hasRoute<UserListDestination>() == true ||
            currentDestination?.hasRoute<FavoriteDestination>() == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination.hierarchy.any { it.hasRoute<UserListDestination>() },
                        onClick = {
                            navController.navigateBottomBarItem(UserListDestination)
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Users") },
                        label = { Text(stringResource(tmh.nhoctax.githubusers.R.string.user)) }
                    )

                    NavigationBarItem(
                        selected = currentDestination.hierarchy.any { it.hasRoute<FavoriteDestination>() },
                        onClick = {
                            navController.navigateBottomBarItem(FavoriteDestination)
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                        label = { Text(stringResource(tmh.nhoctax.githubusers.R.string.favorite)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(navController = navController, startDestination = UserListDestination) {
                usersNavGraph()
                favoriteNavGraph()
            }
        }
    }
}

private fun <T : Any> NavController.navigateBottomBarItem(route: T) {
    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true // Save the state of popped destinations
        }
        // Avoid multiple copies of the same destination when
        // Reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

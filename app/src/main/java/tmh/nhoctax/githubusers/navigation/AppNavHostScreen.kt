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
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHostScreen() {
    val navController = rememberNavController()
    // Get the current information screen
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    //
    val showBottomBar = currentDestination?.route in listOf("users", "favorites")


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == "users" } == true,
                        onClick = {
                            navController.navigateBottomBarItem("users")
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Users") },
                        label = { Text("Users") }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == "favorites" } == true,
                        onClick = {
                            navController.navigateBottomBarItem("favorites")
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                        label = { Text("Favorites") }
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
            NavHost(navController = navController, startDestination = "users") {
                composable("users") {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("Users Screen")
                    }
                }
                composable("favorites") {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("Favorites Screen")
                    }
                }
            }
        }
    }
}

private fun NavController.navigateBottomBarItem(route: String) {
    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true // Save the state of popped destinations
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

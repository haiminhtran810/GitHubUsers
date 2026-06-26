package tmh.nhoctax.githubusers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import kotlinx.coroutines.flow.receiveAsFlow
import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.core.navigation.NavigationIntent

@Composable
fun AppNavigationHandler(
    appNavigator: AppNavigator,
    navController: NavController
) {
    LaunchedEffect(appNavigator, navController) {
        appNavigator.navigationChannel.receiveAsFlow().collect { intent ->
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    val backRoute = intent.route
                    if (backRoute != null) {
                        navController.popBackStack(backRoute, intent.inclusive)
                    } else {
                        navController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    val toRoute = intent.route
                    navController.navigate(toRoute) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpRoute ->
                            popUpTo(popUpRoute) {
                                inclusive = intent.inclusive
                            }
                        }
                        intent.builder(this)
                    }
                }
            }
        }
    }
}
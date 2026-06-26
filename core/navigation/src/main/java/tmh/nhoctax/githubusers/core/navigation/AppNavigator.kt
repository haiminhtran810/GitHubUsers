package tmh.nhoctax.githubusers.core.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel

sealed interface NavigationIntent {
    data class NavigateTo(
        val route: Any,
        val popUpToRoute: Any? = null,
        val inclusive: Boolean = false,
        val isSingleTop: Boolean = false,
        val builder: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationIntent

    data class NavigateBack(
        val route: Any? = null,
        val inclusive: Boolean = false
    ) : NavigationIntent
}

interface AppNavigator {
    val navigationChannel: Channel<NavigationIntent>

    fun tryNavigateTo(
        route: Any,
        popUpToRoute: Any? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false,
        builder: NavOptionsBuilder.() -> Unit = {}
    )

    fun tryNavigateBack(route: Any? = null, inclusive: Boolean = false)
}

class AppNavigatorImpl : AppNavigator {
    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Channel.BUFFERED
    )

    override fun tryNavigateTo(
        route: Any,
        popUpToRoute: Any?,
        inclusive: Boolean,
        isSingleTop: Boolean,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        navigationChannel.trySend(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
                builder = builder
            )
        )
    }

    override fun tryNavigateBack(route: Any?, inclusive: Boolean) {
        navigationChannel.trySend(
            NavigationIntent.NavigateBack(
                route = route,
                inclusive = inclusive
            )
        )
    }
}

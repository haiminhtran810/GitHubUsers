package tmh.nhoctax.githubusers.feature.favorites.presentation

import android.R.attr.action
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.core.ui.model.UserListItem
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.GetFavoriteUsersUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.ToggleFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.presentation.mapper.toUserEntity
import tmh.nhoctax.githubusers.feature.favorites.presentation.mapper.toUserListItem
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteUsersUseCase: GetFavoriteUsersUseCase,
    private val toggleFavoriteUserUseCase: ToggleFavoriteUserUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {
    // Automatically observe changes from the DB and map them to UI Items
    // SharingStarted.WhileSubscribed(5000) is a sharing strategy in Kotlin Coroutines,
    // It is used when converting a Cold Flow (such as Flow from a Room DB) into a Hot FLow(StateFlow) using the StateIn() operator
    // WhileSubscribed remains active only while there is at least one action collector.
    // 5000 => specifies a stop timeout. Instead of stopping immediately when there are no collectors, the Flow waits 5 seconds before cancelling the upstream collection
    val favoriteUsers: StateFlow<List<UserListItem>> = getFavoriteUsersUseCase().map { entities ->
        entities.map {
            it.toUserListItem()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onAction(action: FavoriteUIAction) {
        when (action) {
            is FavoriteUIAction.UserClick -> {

            }

            is FavoriteUIAction.AddFavoriteUser -> {
                toggleFavorite(action.user)
            }
        }
    }

    private fun toggleFavorite(userListItem: UserListItem) {
        viewModelScope.launch {
            val userEntity = userListItem.toUserEntity()
            toggleFavoriteUserUseCase(userEntity)
        }
    }
}

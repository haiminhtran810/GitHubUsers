package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.core.ui.model.UserListItem
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.ToggleFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.presentation.mapper.toUserEntity
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUsersUseCase
import tmh.nhoctax.githubusers.feature.user.navigation.UserDetailDestination
import tmh.nhoctax.githubusers.feature.user.presentation.mapper.toUserListUI
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val appNavigator: AppNavigator,
    private val toggleFavoriteUserUseCase: ToggleFavoriteUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserListUIState())
    val state = _state.asStateFlow()

    fun onAction(action: UserListUIAction) {
        when (action) {
            is UserListUIAction.UserClick -> {
                appNavigator.tryNavigateTo(
                    route = UserDetailDestination(
                        username = action.username,
                        avatarUrl = action.avatarUrl
                    )
                )
            }

            is UserListUIAction.AddUserFavorite -> {
                // Add User to Room DB
                addFavorite(action.item)
            }
        }
    }

    private fun addFavorite(item: UserListItem) {
        viewModelScope.launch {
            viewModelScope.launch {
                val userEntity = item.toUserEntity()
                toggleFavoriteUserUseCase(userEntity)
            }
        }
    }

    val userPaging = getUsersUseCase().map { pagingData ->
        pagingData.map {
            it.toUserListUI()
        }
    }.cachedIn(viewModelScope)
}

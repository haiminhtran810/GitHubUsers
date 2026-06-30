package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.core.ui.model.UserListItem
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.ToggleFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.model.FavoriteUser
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.GetFavoriteUserIdsUseCase
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUsersUseCase
import tmh.nhoctax.githubusers.feature.user.navigation.UserDetailDestination
import tmh.nhoctax.githubusers.feature.user.presentation.mapper.toUserListUI
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    getFavoriteUserIdsUseCase: GetFavoriteUserIdsUseCase,
    private val appNavigator: AppNavigator,
    private val toggleFavoriteUserUseCase: ToggleFavoriteUserUseCase,
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

    // 1. Because PagingData is Immutable
    // Unlike a regular List<UserListItem> where you can just find an item and update it
    // PagingData is continuous stream of immutable page events. You cannot directly edit the items inside it.
    // To change an item in Paging3, you must .map transformation to the stream
    private fun addFavorite(item: UserListItem) {
        viewModelScope.launch {
            viewModelScope.launch {
                //Just toggle in the DB, it will automatically emit new IDs
                val favoriteUser = FavoriteUser(item.id, item.username, item.avatarUrl)
                toggleFavoriteUserUseCase(favoriteUser)
            }
        }
    }

    val userPaging = getUsersUseCase().cachedIn(viewModelScope)
        //Combine with the Database Flow
        .combine(getFavoriteUserIdsUseCase()) { pagingData, ids ->
            pagingData.map { user ->
                val uiItem = user.toUserListUI()
                if (ids.contains(uiItem.id)) {
                    uiItem.copy(
                        isFavorite = !uiItem.isFavorite
                    )
                } else {
                    uiItem
                }
            }
        }
}

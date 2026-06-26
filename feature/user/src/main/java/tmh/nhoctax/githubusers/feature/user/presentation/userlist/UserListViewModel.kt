package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import tmh.nhoctax.githubusers.core.common.dispatcher.AppCoroutineDispatchers
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.feature.user.presentation.mapper.toUIItem
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUsersUseCase
import javax.inject.Inject

import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.feature.user.navigation.UserDetailDestination

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _state = MutableStateFlow(UserListUIState())
    val state = _state.asStateFlow()

    init {
        loadUsers()
    }

    fun onActive(action: UserListUIAction) {
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
            }
        }
    }

    private fun loadUsers() {
        Timber.d("loadUsers")
        viewModelScope.launch {
            getUsersUseCase().collect { result ->
                when (result) {
                    is ResultWrapper.InProgress -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is ResultWrapper.Success -> {
                        Timber.d("UserList: ${result.data}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                users = result.data.map { user -> user.toUIItem() })
                        }
                    }

                    is ResultWrapper.GenericError -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }

                    is ResultWrapper.NetworkError -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Network Error"
                            )
                        }
                    }
                }
            }
        }
    }
}

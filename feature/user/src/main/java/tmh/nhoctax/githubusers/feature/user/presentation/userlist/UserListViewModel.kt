package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUsersUseCase
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserListUIState())
    val state = _state.asStateFlow()

    init {
        loadUsers()
    }

    fun onActive(action: UserListUIAction) {
        when (action) {
            is UserListUIAction.UserClick -> {
                // Navigation to UserDetail
            }
            is UserListUIAction.AddUserFavorite -> {
                // Add User to Room DB
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val users = getUsersUseCase()
                _state.update { it.copy(isLoading = false, users = users) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}

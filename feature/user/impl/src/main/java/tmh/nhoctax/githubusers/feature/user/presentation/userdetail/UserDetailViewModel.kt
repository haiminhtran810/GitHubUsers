package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.IsFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.ToggleFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.model.FavoriteUser
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUserDetailUseCase
import javax.inject.Inject

import tmh.nhoctax.githubusers.feature.user.presentation.mapper.toUIItem

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator,
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val toggleFavoriteUserUseCase: ToggleFavoriteUserUseCase,
    private val isFavoriteUserUseCase: IsFavoriteUserUseCase

) : ViewModel() {

    private val username: String = checkNotNull(savedStateHandle.get<String>("username"))

    private val _state = MutableStateFlow(UserDetailUIState())
    val state = _state.asStateFlow()

    init {
        getUserDetail(username)
    }

    fun onAction(action: UserDetailUIAction) {
        when (action) {
            is UserDetailUIAction.onBackAction -> {
                onBack()
            }

            is UserDetailUIAction.AddUserFavorite -> {
                addFavoriteUser()
            }
        }
    }

    private fun checkUserFavorite(id: Int) {
        viewModelScope.launch {
            val isFavorite = isFavoriteUserUseCase.invoke(id).first()
            _state.update { currentState ->
                currentState.copy(
                    user = currentState.user?.copy(isFavorite = isFavorite)
                )
            }
        }
    }

    private fun updateFavoriteUser(isFavorite: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                user = currentState.user?.copy(isFavorite = isFavorite)
            )
        }
    }

    private fun addFavoriteUser() {
        viewModelScope.launch {
            state.value.user?.let {
                val favoriteUser = FavoriteUser(it.id, it.username, it.avatarUrl)
                val isFavorite = it.isFavorite
                toggleFavoriteUserUseCase.invoke(favoriteUser)
                updateFavoriteUser(!isFavorite)
            }
        }
    }

    private fun onBack() {
        viewModelScope.launch(Dispatchers.Main) {
            appNavigator.tryNavigateBack()
        }
    }

    private fun getUserDetail(userName: String) {
        viewModelScope.launch {
            getUserDetailUseCase(userName).collect { result ->
                when (result) {
                    is ResultWrapper.InProgress -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is ResultWrapper.Success -> {
                        Timber.d("getUserDetail: ${result.data}")
                        val userId = result.data.id
                        checkUserFavorite(userId)
                        _state.update { it.copy(isLoading = false, user = result.data.toUIItem()) }
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
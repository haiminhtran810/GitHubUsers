package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUserDetailUseCase
import javax.inject.Inject

import tmh.nhoctax.githubusers.feature.user.presentation.mapper.toUIItem

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator,
    private val getUserDetailUseCase: GetUserDetailUseCase
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
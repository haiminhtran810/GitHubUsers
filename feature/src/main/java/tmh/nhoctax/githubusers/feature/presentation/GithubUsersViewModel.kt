package tmh.nhoctax.githubusers.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmh.nhoctax.githubusers.feature.domain.model.User
import tmh.nhoctax.githubusers.feature.domain.usecase.GetUsersUseCase
import tmh.nhoctax.githubusers.feature.domain.usecase.SearchUsersUseCase

data class GithubUsersState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)

class GithubUsersViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GithubUsersState())
    val state: StateFlow<GithubUsersState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val users = getUsersUseCase()
                _state.update { it.copy(isLoading = false, users = users) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "An unexpected error occurred") }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L) // debounce
            if (query.isBlank()) {
                loadUsers()
                return@launch
            }
            
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val searchResults = searchUsersUseCase(query)
                _state.update { it.copy(isLoading = false, users = searchResults) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Search failed") }
            }
        }
    }
}
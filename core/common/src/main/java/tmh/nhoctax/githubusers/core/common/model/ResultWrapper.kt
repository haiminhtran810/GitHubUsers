package tmh.nhoctax.githubusers.core.common.model

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class GenericError(val code: Int, val message: String = "Unknown Error") : ResultWrapper<Nothing>()
    data class InProgress(val isLoadMore: Boolean = false) : ResultWrapper<Nothing>()
    object NetworkError : ResultWrapper<Nothing>()
}
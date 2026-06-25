package tmh.nhoctax.githubusers.core.network.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.core.network.common.ApiResponseCode
import java.io.IOException

abstract class BaseRepo {
    fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): Flow<ResultWrapper<T>> {
        return flow {
            emit(ResultWrapper.InProgress())
            val result = apiCall()
            emit(ResultWrapper.Success(result))
        }.catch { ex ->
            val resultError = when (ex) {
                is IOException -> {
                    Timber.e("Exception: IOException $ex")
                    ResultWrapper.NetworkError
                }

                is HttpException -> {
                    Timber.e("Exception: HttpException $ex")
                    ResultWrapper.GenericError(
                        ex.code(), ex.message()
                    )
                }

                else -> {
                    Timber.e("Exception: GenericError $ex")
                    ResultWrapper.GenericError(
                        ApiResponseCode.ERROR_CODE_UNKNOW, ex.message ?: "Unknown Error"
                    )
                }
            }
            emit(resultError)
        }.flowOn(dispatcher)
    }
}
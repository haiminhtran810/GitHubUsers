package tmh.nhoctax.githubusers.core.network.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
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
                    val errorBody = ex.response()?.errorBody()?.string()
                    val apiErrorMessage = parseErrorMessage(errorBody) ?: ex.message()
                    ResultWrapper.GenericError(
                        ex.code(), apiErrorMessage
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

private fun parseErrorMessage(errorBody: String?): String? {
    if (errorBody.isNullOrEmpty()) return null
    return try {
        val jsonObject = JSONObject(errorBody)
        jsonObject.optString("message", null)
    } catch (e: Exception) {
        Timber.e("parseErrorMessage Exception: $e")
        null
    }
}
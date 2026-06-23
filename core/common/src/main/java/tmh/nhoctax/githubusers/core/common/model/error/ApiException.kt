package tmh.nhoctax.githubusers.core.common.model.error

class ApiException(
    val code: Int = 0,
    message: String = "",
) : Exception(message)
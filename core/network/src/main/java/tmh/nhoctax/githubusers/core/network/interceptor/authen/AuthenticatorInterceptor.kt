package tmh.nhoctax.githubusers.core.network.interceptor.authen

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class AuthenticatorInterceptor() : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.e("[AuthenticatorInterceptor] 401 Unauthorized!")
        Timber.d("[AuthenticatorInterceptor] URL is error: ${response.request.url}")
        return null
    }
}
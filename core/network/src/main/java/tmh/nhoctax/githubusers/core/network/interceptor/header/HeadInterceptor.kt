package tmh.nhoctax.githubusers.core.network.interceptor.header

import okhttp3.Interceptor
import okhttp3.Response

internal class HeadInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("X-TOKEN", "bear xxxxxxxNhocTaxxxxxxxx")
            .build()
        return chain.proceed(request)
    }
}
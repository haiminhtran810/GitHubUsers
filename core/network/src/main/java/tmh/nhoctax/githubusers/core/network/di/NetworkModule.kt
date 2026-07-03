package tmh.nhoctax.githubusers.core.network.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import tmh.nhoctax.githubusers.core.network.BuildConfig
import tmh.nhoctax.githubusers.core.network.interceptor.authen.AuthenticatorInterceptor
import tmh.nhoctax.githubusers.core.network.interceptor.header.HeadInterceptor
import tmh.nhoctax.githubusers.core.security.AppSecrets
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    @Provides
    @Singleton
    fun provideCertificatePinner(appSecrets: AppSecrets): CertificatePinner {
        // CertificatePinner expects a hostname or wildcard (e.g.,api.github.com or *.github.com).
        // Do not pass the full URL https://api.github.com/ from BuildConfig.BASE_URL.
        // Passing a full URL with the protocol (https://) and path causes java.lang.IllegalArgumentException: Invalid pattern: ... to be thrown.
        val host = URL(BuildConfig.BASE_URL).host
        return CertificatePinner.Builder().add(
            pattern = host,
            "sha256/" + appSecrets.certificatePinnerKey
        ).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        certificatePinner: CertificatePinner,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .addInterceptor(HeadInterceptor())
            .authenticator(AuthenticatorInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

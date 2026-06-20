package tmh.nhoctax.githubusers

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tmh.nhoctax.githubusers.core.di.coreModule
import tmh.nhoctax.githubusers.feature.user.di.featureModule

class GithubUsersApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@GithubUsersApp)
            modules(coreModule, featureModule)
        }
    }
}
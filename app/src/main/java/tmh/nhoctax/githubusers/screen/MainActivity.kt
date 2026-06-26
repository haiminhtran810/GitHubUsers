package tmh.nhoctax.githubusers.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import tmh.nhoctax.githubusers.navigation.AppNavHostScreen
import tmh.nhoctax.githubusers.core.ui.theme.GithubUsersTheme

import tmh.nhoctax.githubusers.core.navigation.AppNavigator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubUsersTheme {
                AppNavHostScreen(appNavigator = appNavigator)
            }
        }
    }
}

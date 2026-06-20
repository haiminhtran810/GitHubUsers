package tmh.nhoctax.githubusers.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import tmh.nhoctax.githubusers.navigation.AppNavHostScreen
import tmh.nhoctax.githubusers.ui.theme.GithubUsersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubUsersTheme {
                AppNavHostScreen()
            }
        }
    }
}

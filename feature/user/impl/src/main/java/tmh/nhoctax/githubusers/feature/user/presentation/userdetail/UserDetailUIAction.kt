package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

sealed interface UserDetailUIAction {
    object onBackAction : UserDetailUIAction
    object AddUserFavorite : UserDetailUIAction
}
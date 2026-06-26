package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

sealed interface UserDetailUIAction {
    object onBackAction : UserDetailUIAction
    data class AddUserFavorite(val id: String) : UserDetailUIAction
}
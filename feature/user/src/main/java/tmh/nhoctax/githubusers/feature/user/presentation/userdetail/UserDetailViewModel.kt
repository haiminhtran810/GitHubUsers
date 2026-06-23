package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

import androidx.lifecycle.ViewModel
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUserDetailUseCase
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUsersUseCase
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

}
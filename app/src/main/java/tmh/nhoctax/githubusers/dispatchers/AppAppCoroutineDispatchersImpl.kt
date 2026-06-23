package tmh.nhoctax.githubusers.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import tmh.nhoctax.githubusers.core.common.dispatcher.AppCoroutineDispatchers
import javax.inject.Inject

// It is primary purpose is to improve the testability and flexibility of your application by using Dependency Injection
class AppAppCoroutineDispatchersImpl @Inject constructor() : AppCoroutineDispatchers {
    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO
    override val DEFAULT: CoroutineDispatcher
        get() = Dispatchers.Default
    override val MAIN: CoroutineDispatcher
        get() = Dispatchers.Main
}
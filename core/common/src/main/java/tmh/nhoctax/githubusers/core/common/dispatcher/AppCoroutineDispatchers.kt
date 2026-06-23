package tmh.nhoctax.githubusers.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

// CoroutineDispatcher
// It is abstract class that determines which thread or thread pool the coroutines will be executed on
// Dispatchers (The Object containing build-in dispatchers)
// Purpose: Create interface like CoroutineDispatchers is excellent Best Practise in Android for DI and Unit Testing

interface AppCoroutineDispatchers {
    val IO: CoroutineDispatcher
    val DEFAULT: CoroutineDispatcher
    val MAIN: CoroutineDispatcher
}
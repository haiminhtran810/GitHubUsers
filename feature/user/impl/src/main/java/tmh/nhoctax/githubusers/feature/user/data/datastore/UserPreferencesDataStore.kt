package tmh.nhoctax.githubusers.feature.user.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface UserPreferencesDataStore {
    suspend fun updateLastTimeToSaveUsers(lastTimeUpdated: Long)
    fun getLastTimeUpdatedUsers(): Flow<Long>
}

class UserPreferencesDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesDataStore {

    private val LAST_TIME_UPDATED_KEY = longPreferencesKey("last_time_updated")

    override suspend fun updateLastTimeToSaveUsers(lastTimeUpdated: Long) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[LAST_TIME_UPDATED_KEY] = lastTimeUpdated
            }
        }
    }

    override fun getLastTimeUpdatedUsers(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[LAST_TIME_UPDATED_KEY] ?: 0
        }
    }

}

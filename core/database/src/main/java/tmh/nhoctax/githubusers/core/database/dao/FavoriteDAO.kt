package tmh.nhoctax.githubusers.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.database.model.UserEntity

// A DAO defines all database operations
// Think of DAO as the interface between your kotlin code and SQL
@Dao
interface FavoriteDAO {
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT id FROM users")
    fun getFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query ("DELETE FROM users WHERE id = :id")
    suspend fun removeUser(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}
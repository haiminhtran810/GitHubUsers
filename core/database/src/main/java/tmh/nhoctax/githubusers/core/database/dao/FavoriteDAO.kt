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
    // 1. Only retrieve actual favorited users
    @Query("SELECT * FROM users WHERE is_favorite = 1")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT id FROM users WHERE is_favorite = 1")
    fun getFavoriteIds(): Flow<List<Int>>

    // @Insert(REPLACE) may produce similar results in simple case. but they are not same
    // Inserts if row doesn’t exist => same
    // Updates if row exists => Upsert (Yes) - @Insert(REPLACE) (Can trigger cascade deletes)
    // Preserves the existing row => Upsert (Yes) - @Insert(REPLACE) (Deletes and inserts a new row)
    // Safe with foreign keys => Upsert (Yes) - @Insert(REPLACE) (Can trigger cascade deletes)
    // Preserves the existing row => Upsert (Yes) - @Insert(REPLACE) (May create a new row)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET is_favorite = 0 WHERE id = :id")
    suspend fun removeFavoriteUser(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :id AND is_favorite = 1)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Query("UPDATE users SET is_favorite = :isFavorite WHERE id = :userId")
    suspend fun updateFavoriteStatus(userId: Int, isFavorite: Boolean)
}
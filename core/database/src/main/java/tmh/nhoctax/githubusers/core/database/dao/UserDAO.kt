package tmh.nhoctax.githubusers.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import androidx.paging.PagingSource
import tmh.nhoctax.githubusers.core.database.model.UserEntity

@Dao
interface UserDAO {
    // Upset in Room combines Inset and Update into a single operator
    // If the row does not exist => UPDATE
    // If the row already exist => INSERT
    @Upsert
    suspend fun insertAll(data: List<UserEntity>)

    // Order by id ASC to guarantee sequential ordering matching the API's pagination keys
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getPagingSource(): PagingSource<Int, UserEntity>


    @Query("DELETE FROM users WHERE is_favorite = 0")
    suspend fun deleteNonFavorites()

    @Query("SELECT id FROM users WHERE is_favorite = 1")
    suspend fun getFavoriteIds(): List<Int>

}
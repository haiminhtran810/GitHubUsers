package tmh.nhoctax.githubusers.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import androidx.paging.PagingSource
import tmh.nhoctax.githubusers.core.database.model.UserEntity

@Dao
interface UserDAO {
    @Upsert
    suspend fun insertAll(data: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getPagingSource(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM users WHERE is_favorite = 0")
    suspend fun deleteNonFavorites()


}
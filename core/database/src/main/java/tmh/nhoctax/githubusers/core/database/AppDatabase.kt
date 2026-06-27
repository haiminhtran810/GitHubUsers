package tmh.nhoctax.githubusers.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tmh.nhoctax.githubusers.core.database.dao.FavoriteDAO
import tmh.nhoctax.githubusers.core.database.model.UserEntity


// exportSchema = true
// to save the schema history
// With true, each builds project , Room will create JSON file to describe the DB structure
// To check migrate from V1 to V2
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO
}
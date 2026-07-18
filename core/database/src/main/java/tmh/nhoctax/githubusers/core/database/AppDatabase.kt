package tmh.nhoctax.githubusers.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import tmh.nhoctax.githubusers.core.database.dao.FavoriteDAO
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import tmh.nhoctax.githubusers.core.database.model.UserEntity


// exportSchema = true
// to save the schema history
// With true, each builds project , Room will create JSON file to describe the DB structure
// To check migrate from V1 to V2.
// Note: when your app is already installed on a user`s device, it has a local SQLite database file based on your old schema (Version 1).
// if you update your app and bump the Room DB version to 2 (Because you add a table , change a column, etc.)
// Room notices the version mismatch when app launches => throw IllegalStateException
@Database(
    entities = [UserEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO
    abstract fun userDAO(): UserDAO

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Empty migration: schema changed only to add defaultValue="0" which matches 
                // the existing DEFAULT 0 added in MIGRATION_1_2.
            }
        }
    }
}


package tmh.nhoctax.githubusers.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// An Entity represents a table in database

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("username")
    val username: String,
    @ColumnInfo("url")
    val url: String
)

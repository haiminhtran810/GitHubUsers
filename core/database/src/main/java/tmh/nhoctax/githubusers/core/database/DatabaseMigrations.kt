package tmh.nhoctax.githubusers.core.database

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

// You only need an AutoMigrationSpec when Room cannot guess what you did.
// For example, if you rename a column, delete a column, or rename a table.
internal object DatabaseMigrations {
    @RenameColumn(
        tableName = "users",
        fromColumnName = "oldColumnName",
        toColumnName = "newColumnName",
    )
    class Schema2to3 : AutoMigrationSpec

    @DeleteColumn(
        tableName = "news_resources",
        columnName = "episode_id",
    )
    @DeleteTable.Entries(
        DeleteTable(
            tableName = "episodes_authors",
        ),
        DeleteTable(
            tableName = "episodes",
        ),
    )
    class Schema3to4 : AutoMigrationSpec
}
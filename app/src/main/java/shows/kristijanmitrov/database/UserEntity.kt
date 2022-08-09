package shows.kristijanmitrov.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "image_url") var imageUrl: String?
)
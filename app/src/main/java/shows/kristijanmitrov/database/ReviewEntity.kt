package shows.kristijanmitrov.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class ReviewEntity (
    @ColumnInfo(name = "id") @PrimaryKey val id: String,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "image_url") var imageUrl: String?

)
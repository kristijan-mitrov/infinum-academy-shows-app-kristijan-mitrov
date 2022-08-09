package shows.kristijanmitrov.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Show(
    @SerialName("id") val id: String,
    @SerialName("average_rating") @ColumnInfo(name = "average_rating") val averageRating: Float?,
    @SerialName("description")val description: String?,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("no_of_reviews") val noOfReviews: Int,
    @SerialName("title") val title: String
) : Parcelable
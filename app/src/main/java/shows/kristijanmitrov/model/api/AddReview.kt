package shows.kristijanmitrov.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.Review

@Serializable
data class AddReviewRequest(
    @SerialName("rating") val rating: Int,
    @SerialName("comment") val comment: String,
    @SerialName("show_id") val showId: String
)

data class AddReviewResponse(
    val isSuccessful: Boolean,
    val body: AddReviewResponseBody? = null
)

@Serializable
data class AddReviewResponseBody(
    @SerialName("review") val review: Review
)

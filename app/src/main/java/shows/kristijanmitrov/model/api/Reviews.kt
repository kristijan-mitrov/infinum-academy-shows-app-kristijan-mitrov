package shows.kristijanmitrov.model.api

import Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.Review

data class ReviewResponse(
    val isSuccessful: Boolean,
    val body: ReviewResponseBody? = null
)

@Serializable
data class ReviewResponseBody(
    @SerialName("reviews") val reviews: ArrayList<Review>,
    @SerialName("meta") val meta: Meta
)
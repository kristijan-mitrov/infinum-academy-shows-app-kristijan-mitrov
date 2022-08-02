package shows.kristijanmitrov.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.Show

data class TopRatedShowsResponse(
    val isSuccessful: Boolean,
    val body: TopRatedShowsResponseBody? = null
)

@Serializable
data class TopRatedShowsResponseBody(
    @SerialName("shows") val shows: ArrayList<Show>,
)

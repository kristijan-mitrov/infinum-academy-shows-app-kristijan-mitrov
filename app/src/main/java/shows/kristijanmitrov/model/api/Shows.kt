package shows.kristijanmitrov.model.api

import Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.Show

data class ShowsResponse(
    val isSuccessful: Boolean,
    val body: ShowsResponseBody? = null
)

@Serializable
data class ShowsResponseBody(
    @SerialName("shows") val shows: ArrayList<Show>,
    @SerialName("meta") val meta: Meta?
)

package shows.kristijanmitrov.model.api

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
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination
)

@Serializable
data class Pagination(
    @SerialName("count") val count: Int,
    @SerialName("page") val page: Int,
    @SerialName("items") val items: Int,
    @SerialName("pages") val pages: Int
)

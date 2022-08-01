package shows.kristijanmitrov.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.User

data class UpdateUserResponse(
    val isSuccessful: Boolean,
    val body: UpdateUserResponseBody? = null
)

@Serializable
data class UpdateUserResponseBody(
    @SerialName("user") val user: User
)

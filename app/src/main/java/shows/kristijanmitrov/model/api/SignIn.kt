package shows.kristijanmitrov.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.User

@Serializable
data class SignInRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

data class SignInResponse(
    val isSuccessful: Boolean,
    val header: SignInResponseHeader? = null,
    val body: SignInResponseBody? = null
)

data class SignInResponseHeader(
    val accessToken: String? = null,
    val client: String? = null,
    val expiry: String? = null,
    val uid: String? = null
)

@Serializable
data class SignInResponseBody(
    @SerialName("user") val user: User
)

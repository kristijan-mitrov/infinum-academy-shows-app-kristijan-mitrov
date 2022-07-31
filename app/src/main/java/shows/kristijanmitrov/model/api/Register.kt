package shows.kristijanmitrov.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shows.kristijanmitrov.model.User

@Serializable
data class RegisterRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String
)

data class RegisterResponse(
    val isSuccessful: Boolean,
    val body: RegisterResponseBody? = null
)

@Serializable
data class RegisterResponseBody(
    @SerialName("user") val user: User
)


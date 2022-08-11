package shows.kristijanmitrov.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") var imageUrl: String?
) {
    fun getUsername() = email.split("@")[0]
}
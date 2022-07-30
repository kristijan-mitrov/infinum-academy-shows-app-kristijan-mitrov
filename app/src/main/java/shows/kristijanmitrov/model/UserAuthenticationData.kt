package shows.kristijanmitrov.model

data class UserAuthenticationData(
    val isSuccessful: Boolean,
    val accessToken: String? = null,
    val client: String? = null,
    val expiry: String? = null,
    val uid: String? = null
)

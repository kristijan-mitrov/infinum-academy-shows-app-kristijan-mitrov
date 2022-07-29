package shows.kristijanmitrov.model

data class User(
    val username: String,
    val email: String,
    var profilePhoto: String?
) {
}
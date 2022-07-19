package shows.kristijanmitrov.model

class Review(
    val id: String,
    val showId: String,
    val user: String,
    val ratingValue: Int,
    val descriptionText: String
)
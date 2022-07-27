package shows.kristijanmitrov.model

class Review(
    val user: User,
    val ratingValue: Int,
    val descriptionText: String
)
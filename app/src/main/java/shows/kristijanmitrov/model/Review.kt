package shows.kristijanmitrov.model

import java.util.UUID

data class Review(
    val id: UUID,
    val user: User,
    val ratingValue: Int,
    val descriptionText: String
)
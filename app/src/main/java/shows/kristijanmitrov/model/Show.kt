package shows.kristijanmitrov.model

import androidx.annotation.DrawableRes

class Show(
    val id: String,
    val title: String,
    val descriptionText: String,
    @DrawableRes val image: Int
    )
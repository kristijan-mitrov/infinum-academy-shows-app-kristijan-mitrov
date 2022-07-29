package shows.kristijanmitrov.model


import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    val id: String?,
    val title: String?,
    val descriptionText: String?,
    @DrawableRes val image: Int) : Parcelable
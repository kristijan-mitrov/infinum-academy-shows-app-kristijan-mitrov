package shows.kristijanmitrov.viewModel

import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shows.kristijanmitrov.infinumacademyshows.R
import shows.kristijanmitrov.model.Show
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.ui.ReviewAdapter

class ShowDetailsViewModel: ViewModel() {

    private val _show: MutableLiveData<Show> = MutableLiveData()
    private val _reviewText: MutableLiveData<String> = MutableLiveData()
    private val _ratingBar: MutableLiveData<Float> = MutableLiveData()

    val show: LiveData<Show> = _show
    val reviewText: LiveData<String> = _reviewText
    val ratingBar: LiveData<Float> = _ratingBar

    fun setShow(show: Show){
        _show.value = show
    }

    fun addReview(adapter: ReviewAdapter, user: User, comment: String, rating: Int){
        adapter.addReview(
            user,
            comment,
            rating
        )

        val numOfReviews = adapter.itemCount
        val averageRating = adapter.getAverage()
        val reviewTextStr = String.format("%d REVIEWS, %.2f AVERAGE", numOfReviews, averageRating)

        _reviewText.value = reviewTextStr
        _ratingBar.value = averageRating
    }
}
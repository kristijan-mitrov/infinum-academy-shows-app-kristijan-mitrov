package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import shows.kristijanmitrov.model.RatingData
import shows.kristijanmitrov.model.Review
import shows.kristijanmitrov.model.Show
import shows.kristijanmitrov.model.User

class ShowDetailsViewModel: ViewModel() {

    private val reviewList = ArrayList<Review>()
    private fun getAverageReview() =  reviewList.sumOf { review -> review.ratingValue }/reviewList.count().toFloat()

    private val _show: MutableLiveData<Show> = MutableLiveData()
    private val _ratingData: MutableLiveData<RatingData> = MutableLiveData()
    private val _ratingBar: MutableLiveData<Float> = MutableLiveData()
    private val _reviews: MutableLiveData<MutableList<Review>> = MutableLiveData()

    val show: LiveData<Show> = _show
    val ratingData: LiveData<RatingData> = _ratingData
    val ratingBar: LiveData<Float> = _ratingBar
    val reviews: LiveData<MutableList<Review>> = _reviews


    fun init(show: Show){
        _show.value = show
    }

    fun addReview(user: User, descriptionText: String, ratingValue: Int){
        val newReview = Review(UUID.randomUUID(), user, ratingValue, descriptionText)

        reviewList.add(newReview)

        val averageRating = getAverageReview()
        val numOfReviews = reviewList.count()

        _ratingData.value = RatingData(averageRating, numOfReviews)
        _ratingBar.value = averageRating
        _reviews.value = reviewList
    }
}
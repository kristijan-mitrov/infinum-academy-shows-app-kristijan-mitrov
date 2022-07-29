package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import shows.kristijanmitrov.model.Review
import shows.kristijanmitrov.model.Show
import shows.kristijanmitrov.model.User

class ShowDetailsViewModel: ViewModel() {

    private val reviewList = ArrayList<Review>()
    private fun getAverageReview() =  reviewList.sumOf { review -> review.ratingValue }/reviewList.count().toFloat()

    private val _show: MutableLiveData<Show> = MutableLiveData()
    private val _reviewText: MutableLiveData<String> = MutableLiveData()
    private val _ratingBar: MutableLiveData<Float> = MutableLiveData()
    private val _reviews: MutableLiveData<List<Review>> = MutableLiveData()

    val show: LiveData<Show> = _show
    val reviewText: LiveData<String> = _reviewText
    val ratingBar: LiveData<Float> = _ratingBar
    val reviews: LiveData<List<Review>> = _reviews


    fun init(show: Show){
        _show.value = show
    }

    fun addReview(user: User, descriptionText: String, ratingValue: Int){
        val newReview = Review(UUID.randomUUID(), user, ratingValue, descriptionText)

        reviewList.add(newReview)

        val numOfReviews = reviewList.count()
        val averageRating = getAverageReview()
        val reviewTextStr = String.format("%d REVIEWS, %.2f AVERAGE", numOfReviews, averageRating)

        _reviewText.value = reviewTextStr
        _ratingBar.value = averageRating
        _reviews.value = reviewList
    }
}
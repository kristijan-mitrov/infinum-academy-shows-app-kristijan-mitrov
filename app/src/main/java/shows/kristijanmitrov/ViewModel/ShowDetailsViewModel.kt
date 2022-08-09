package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.database.ReviewEntity
import shows.kristijanmitrov.database.ShowsDatabase
import shows.kristijanmitrov.database.UserEntity
import shows.kristijanmitrov.model.Show
import shows.kristijanmitrov.model.api.AddReviewRequest
import shows.kristijanmitrov.model.api.AddReviewResponse
import shows.kristijanmitrov.model.api.AddReviewResponseBody
import shows.kristijanmitrov.model.api.ReviewResponse
import shows.kristijanmitrov.model.api.ReviewResponseBody
import shows.kristijanmitrov.networking.ApiModule

class ShowDetailsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val _show: MutableLiveData<Show> = MutableLiveData()
    private val reviewsResponseLiveData: MutableLiveData<ReviewResponse> by lazy { MutableLiveData<ReviewResponse>() }
    private val reviewsLiveData: MutableLiveData<List<ReviewEntity>> by lazy { MutableLiveData<List<ReviewEntity>>() }
    private val addReviewsResponseLiveData: MutableLiveData<AddReviewResponse> by lazy { MutableLiveData<AddReviewResponse>() }

    val show: LiveData<Show> = _show

    fun getReviewsResultLiveData(): LiveData<ReviewResponse> {
        return reviewsResponseLiveData
    }

    fun getAddReviewsResultLiveData(): LiveData<AddReviewResponse> {
        return addReviewsResponseLiveData
    }

    fun getReviewsLiveData(showId: String, page: Int): LiveData<List<ReviewEntity>> {
        return database.reviewDao().getAllReviewsForShow(showId, page)
    }

    fun getUserById(userId: String): LiveData<UserEntity> {
        return database.userDao().getUser(userId)
    }

    fun getReviews(showId: String, page: Int, accessToken: String, client: String, expiry: String, uid: String) {

        ApiModule.retrofit.reviews(showId, page, accessToken, client, expiry, uid)
            .enqueue(object : Callback<ReviewResponseBody> {
                override fun onResponse(call: Call<ReviewResponseBody>, response: Response<ReviewResponseBody>) {
                    val reviews = response.body()?.reviews
                    reviews?.let {

                        Executors.newSingleThreadExecutor().execute {
                            //Insert reviews in database
                            database.reviewDao().insertAllReviews(
                                reviews.map { review ->
                                    ReviewEntity(
                                        review.id,
                                        review.comment,
                                        review.rating,
                                        review.showId.toString(),
                                        review.user.id,
                                        page
                                    )
                                }
                            )

                            //Insert users in database
                            database.userDao().insertAllUsers(
                                reviews.map { review ->
                                    UserEntity(review.user.id, review.user.email, review.user.imageUrl)
                                }
                            )

                        }

                        //Setting reviews Live Data
                        reviewsLiveData.value = reviews.map { review ->
                            ReviewEntity(
                                review.id,
                                review.comment,
                                review.rating,
                                review.showId.toString(),
                                review.user.id,
                                page
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewResponseBody>, t: Throwable) {
                    val reviewResponse = ReviewResponse(
                        isSuccessful = false
                    )
                    reviewsResponseLiveData.value = reviewResponse
                }

            })
    }

    fun init(show: Show, accessToken: String, client: String, expiry: String, uid: String) {
        _show.value = show

        getReviews(show.id, 1, accessToken, client, expiry, uid)
    }

    fun addReview(rating: Int, comment: String, show: Show, accessToken: String, client: String, expiry: String, uid: String) {

        val addReviewRequest = AddReviewRequest(
            rating = rating,
            comment = comment,
            showId = show.id
        )

        ApiModule.retrofit.addReview(accessToken, client, expiry, uid, addReviewRequest)
            .enqueue(object : Callback<AddReviewResponseBody> {
                override fun onResponse(call: Call<AddReviewResponseBody>, response: Response<AddReviewResponseBody>) {
                    val addReviewResponse = AddReviewResponse(
                        isSuccessful = response.isSuccessful,
                        body = response.body()
                    )

                    if (response.isSuccessful)
                        getReviews(show.id, 1, accessToken, client, expiry, uid)

                    addReviewsResponseLiveData.value = addReviewResponse
                }

                override fun onFailure(call: Call<AddReviewResponseBody>, t: Throwable) {
                    val addReviewResponse = AddReviewResponse(
                        isSuccessful = false
                    )

                    addReviewsResponseLiveData.value = addReviewResponse
                }
            })
    }
}
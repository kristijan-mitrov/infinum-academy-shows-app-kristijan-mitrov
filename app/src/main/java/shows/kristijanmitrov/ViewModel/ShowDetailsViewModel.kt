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
import shows.kristijanmitrov.model.Show
import shows.kristijanmitrov.model.api.AddReviewRequest
import shows.kristijanmitrov.model.api.AddReviewResponseBody
import shows.kristijanmitrov.model.api.ReviewResponse
import shows.kristijanmitrov.model.api.ReviewResponseBody
import shows.kristijanmitrov.networking.ApiModule

class ShowDetailsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val _show: MutableLiveData<Show> = MutableLiveData()
    private val reviewsResponseLiveData: MutableLiveData<ReviewResponse> by lazy { MutableLiveData<ReviewResponse>() }

    val show: LiveData<Show> = _show

    fun getReviewsResultLiveData(): LiveData<ReviewResponse> {
        return reviewsResponseLiveData
    }

    fun getReviewsLiveData(showId: String): LiveData<List<ReviewEntity>> {
        return database.reviewDao().getAllReviewsForShow(showId)
    }

    fun getPage(showId: String, page: Int, accessToken: String, client: String, expiry: String, uid: String){
        ApiModule.retrofit.reviews(showId, page, accessToken, client, expiry, uid)
            .enqueue(object : Callback<ReviewResponseBody> {
                override fun onResponse(call: Call<ReviewResponseBody>, response: Response<ReviewResponseBody>) {
                    response.body()?.meta?.pagination?.pages?.let{
                        if(page < it)
                            getPage(showId, page+1, accessToken, client, expiry, uid)
                    }

                    val reviews = response.body()?.reviews
                    reviews?.let {

                        val reviewEntities = reviews.map { review ->
                            ReviewEntity(
                                review.id,
                                review.comment,
                                review.rating,
                                review.showId.toString(),
                                review.user.id,
                                review.user.email,
                                review.user.imageUrl
                            )
                        }


                        //Insert reviews in database
                        Executors.newSingleThreadExecutor().execute {
                            database.reviewDao().insertAllReviews(reviewEntities)
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewResponseBody>, t: Throwable) { }

            })

    }

    fun getReviews(showId: String, accessToken: String, client: String, expiry: String, uid: String) {
        getPage(showId, 1, accessToken, client, expiry, uid)
    }

    fun init(show: Show, accessToken: String, client: String, expiry: String, uid: String) {
        _show.value = show

        getReviews(show.id, accessToken, client, expiry, uid)
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
                    if (response.isSuccessful)
                        getReviews(show.id, accessToken, client, expiry, uid)

                }

                override fun onFailure(call: Call<AddReviewResponseBody>, t: Throwable) { }
            })
    }
}
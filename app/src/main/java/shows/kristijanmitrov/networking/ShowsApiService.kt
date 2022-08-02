package shows.kristijanmitrov.networking

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import shows.kristijanmitrov.model.api.AddReviewRequest
import shows.kristijanmitrov.model.api.AddReviewResponseBody
import shows.kristijanmitrov.model.api.RegisterRequest
import shows.kristijanmitrov.model.api.RegisterResponseBody
import shows.kristijanmitrov.model.api.ReviewResponseBody
import shows.kristijanmitrov.model.api.ShowsResponseBody
import shows.kristijanmitrov.model.api.SignInRequest
import shows.kristijanmitrov.model.api.SignInResponseBody
import shows.kristijanmitrov.model.api.TopRatedShowsResponseBody
import shows.kristijanmitrov.model.api.UpdateUserResponseBody

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponseBody>

    @POST("/users/sign_in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponseBody>

    @Headers("token-type: Bearer")
    @GET("/shows")
    fun shows(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
    ): Call<ShowsResponseBody>

    @Headers("token-type: Bearer")
    @GET("/shows/top_rated")
    fun topRatedShows(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
    ): Call<TopRatedShowsResponseBody>

    @Headers("token-type: Bearer")
    @GET("/shows/{show_id}/reviews")
    fun reviews(
        @Path(value = "show_id", encoded = true) showId: String,
        @Query("page") page: Int,
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
    ): Call<ReviewResponseBody>

    @Headers("token-type: Bearer")
    @POST("/reviews")
    fun addReview(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
        @Body request: AddReviewRequest
    ): Call<AddReviewResponseBody>

    @Headers("token-type: Bearer")
    @Multipart
    @PUT("/users")
    fun updateUser(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
        @Part image: MultipartBody.Part
    ): Call<UpdateUserResponseBody>

}
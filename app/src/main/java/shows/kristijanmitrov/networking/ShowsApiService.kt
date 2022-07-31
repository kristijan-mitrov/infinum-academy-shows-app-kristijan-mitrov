package shows.kristijanmitrov.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import shows.kristijanmitrov.model.api.RegisterRequest
import shows.kristijanmitrov.model.api.RegisterResponseBody
import shows.kristijanmitrov.model.api.ShowsResponseBody
import shows.kristijanmitrov.model.api.SignInRequest
import shows.kristijanmitrov.model.api.SignInResponseBody

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
}
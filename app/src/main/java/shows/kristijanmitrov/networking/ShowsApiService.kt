package shows.kristijanmitrov.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import shows.kristijanmitrov.networking.model.RegisterRequest
import shows.kristijanmitrov.networking.model.RegisterResponse
import shows.kristijanmitrov.networking.model.ShowsResponse
import shows.kristijanmitrov.networking.model.SignInRequest
import shows.kristijanmitrov.networking.model.SignInResponse

interface ShowsApiService {

    @POST("/users/sign_in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @Headers("token-type: Bearer")
    @GET("/shows")
    fun shows(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
    ): Call<ShowsResponse>
}
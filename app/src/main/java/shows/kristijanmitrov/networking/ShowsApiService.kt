package shows.kristijanmitrov.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import shows.kristijanmitrov.networking.model.RegisterRequest
import shows.kristijanmitrov.networking.model.RegisterResponse
import shows.kristijanmitrov.networking.model.SignInRequest
import shows.kristijanmitrov.networking.model.SignInResponse

interface ShowsApiService {

    @POST("/users/sign_in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

}
package shows.kristijanmitrov.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import shows.kristijanmitrov.networking.model.RegisterRequest
import shows.kristijanmitrov.networking.model.RegisterResponse

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}
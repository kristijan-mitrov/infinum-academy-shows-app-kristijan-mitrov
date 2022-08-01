package shows.kristijanmitrov.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.model.api.ShowsResponse
import shows.kristijanmitrov.model.api.ShowsResponseBody
import shows.kristijanmitrov.model.api.UpdateUserResponse
import shows.kristijanmitrov.model.api.UpdateUserResponseBody
import shows.kristijanmitrov.networking.ApiModule

class ShowsViewModel : ViewModel() {

    private val _profilePhoto: MutableLiveData<String> = MutableLiveData()
    private val showsResponseLiveData: MutableLiveData<ShowsResponse> by lazy { MutableLiveData<ShowsResponse>() }
    private val updateUserResponseLiveData: MutableLiveData<UpdateUserResponse> by lazy { MutableLiveData<UpdateUserResponse>() }

    val profilePhoto: LiveData<String> = _profilePhoto

    fun getShowsResultLiveData(): LiveData<ShowsResponse> {
        return showsResponseLiveData
    }

    fun getUpdateUserResultLiveData(): LiveData<UpdateUserResponse> {
        return updateUserResponseLiveData
    }

    fun onProfilePhotoChanged(uri: Uri, accessToken: String, client: String, expiry: String, uid: String) {
        uri.path?.let { path ->
            val file = File(path)

            ApiModule.retrofit.updateUser(
                accessToken = accessToken,
                client = client,
                expiry = expiry,
                uid = uid,
                image = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    file.asRequestBody("image/png".toMediaType())
                )
            ).enqueue(object : Callback<UpdateUserResponseBody> {
                override fun onResponse(call: Call<UpdateUserResponseBody>, response: Response<UpdateUserResponseBody>) {
                    val updateUserResponse = UpdateUserResponse(
                        isSuccessful = response.isSuccessful,
                        body = response.body()
                    )
                    updateUserResponseLiveData.value = updateUserResponse

                    response.body()?.user?.imageUrl?.let { url ->
                        _profilePhoto.value = url
                    }
                }

                override fun onFailure(call: Call<UpdateUserResponseBody>, t: Throwable) {
                    val updateUserResponse = UpdateUserResponse(
                        isSuccessful = false
                    )
                    updateUserResponseLiveData.value = updateUserResponse
                }
            })
        }
    }

    fun init(accessToken: String, client: String, expiry: String, uid: String) {
        ApiModule.retrofit.shows(accessToken, client, expiry, uid)
            .enqueue(object : Callback<ShowsResponseBody> {
                override fun onResponse(call: Call<ShowsResponseBody>, response: Response<ShowsResponseBody>) {
                    val showsResponse = ShowsResponse(
                        isSuccessful = response.isSuccessful,
                        body = response.body()
                    )

                    showsResponseLiveData.value = showsResponse
                }

                override fun onFailure(call: Call<ShowsResponseBody>, t: Throwable) {

                    val showsResponse = ShowsResponse(
                        isSuccessful = false
                    )

                    showsResponseLiveData.value = showsResponse
                }

            })
    }

}
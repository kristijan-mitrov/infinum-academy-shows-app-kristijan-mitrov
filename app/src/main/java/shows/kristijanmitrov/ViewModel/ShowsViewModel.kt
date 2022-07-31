package shows.kristijanmitrov.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.model.api.ShowsResponse
import shows.kristijanmitrov.model.api.ShowsResponseBody
import shows.kristijanmitrov.networking.ApiModule

class ShowsViewModel : ViewModel() {

    private val _profilePhoto: MutableLiveData<Uri> = MutableLiveData()
    private val showsResponseLiveData: MutableLiveData<ShowsResponse> by lazy { MutableLiveData<ShowsResponse>() }

    val profilePhoto: LiveData<Uri> = _profilePhoto

    fun getShowsResultLiveData(): LiveData<ShowsResponse> {
        return showsResponseLiveData
    }

    fun onProfilePhotoChanged(uri: Uri) {
        _profilePhoto.value = uri
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
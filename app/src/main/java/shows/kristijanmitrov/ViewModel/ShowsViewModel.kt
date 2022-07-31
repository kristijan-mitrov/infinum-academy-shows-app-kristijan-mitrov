package shows.kristijanmitrov.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.model.ShowsResultData
import shows.kristijanmitrov.networking.ApiModule
import shows.kristijanmitrov.networking.model.ShowsResponse

class ShowsViewModel : ViewModel() {

    private val _profilePhoto: MutableLiveData<Uri> = MutableLiveData()
    private val showsResultLiveData: MutableLiveData<ShowsResultData> by lazy { MutableLiveData<ShowsResultData>() }

    val profilePhoto: LiveData<Uri> = _profilePhoto

    fun getShowsResultLiveData(): LiveData<ShowsResultData> {
        return showsResultLiveData
    }

    fun onProfilePhotoChanged(uri: Uri) {
        _profilePhoto.value = uri
    }

    fun init(accessToken: String, client: String, expiry: String, uid: String) {
        ApiModule.retrofit.shows(accessToken, client, expiry, uid)
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    val showsResultData = response.body()?.let {
                        ShowsResultData(
                            isSuccessful = response.isSuccessful,
                            shows = it.shows
                        )
                    }
                    showsResultLiveData.value = showsResultData
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    val showsResultData = ShowsResultData(
                        isSuccessful = false
                    )
                    showsResultLiveData.value = showsResultData
                }

            })
    }

}
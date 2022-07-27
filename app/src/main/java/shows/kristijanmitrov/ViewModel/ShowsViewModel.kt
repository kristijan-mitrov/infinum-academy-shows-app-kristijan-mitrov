package shows.kristijanmitrov.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shows.kristijanmitrov.infinumacademyshows.R
import shows.kristijanmitrov.model.Show

class ShowsViewModel: ViewModel() {

    val showsList = arrayListOf(
        Show("1", "Stranger Things", "desc", R.drawable.stranger_things),
        Show("2", "Dark", "desc", R.drawable.dark),
        Show("3", "How I Met Your Mother", "desc", R.drawable.himym),
        Show("4", "Game of Thrones", "desc", R.drawable.game_of_thrones),
        Show("5", "Sex Education", "desc", R.drawable.sex_education),
        Show("6", "Young Royals", "desc", R.drawable.young_royals),
        Show("7", "The Crown", "desc", R.drawable.the_crown),
        Show("8", "The Queens Gambit", "desc", R.drawable.the_queens_gambit)
    )

    private val _profilePhoto: MutableLiveData<Uri> = MutableLiveData()
    val profilePhoto: LiveData<Uri> = _profilePhoto

    fun setProfilePhoto(uri: Uri){
        _profilePhoto.value = uri
    }

}
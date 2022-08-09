package shows.kristijanmitrov.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import shows.kristijanmitrov.database.ShowsDatabase

class ShowViewModelFactory(
    val database: ShowsDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        }
        throw IllegalArgumentException("Unassailable with non ShowsViewModel classes")
    }

}
package shows.kristijanmitrov.infinumacademyshows

import android.app.Application
import shows.kristijanmitrov.database.ShowsDatabase

class ShowsApplication: Application() {

    val database by lazy {
        ShowsDatabase.getDatabase(this)
    }
}
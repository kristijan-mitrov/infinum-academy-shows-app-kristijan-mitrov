package shows.kristijanmitrov.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import shows.kristijanmitrov.database.dao.ReviewDao
import shows.kristijanmitrov.database.dao.ShowDao

@Database(
    entities = [
        ShowEntity::class,
        ReviewEntity::class
    ],
    version = 1
)
abstract class ShowsDatabase: RoomDatabase() {

    companion object{
        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this){
                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = database
                database
            }
        }

    }

    abstract fun showDao(): ShowDao
    abstract fun reviewDao(): ReviewDao
}
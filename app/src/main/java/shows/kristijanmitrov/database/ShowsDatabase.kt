package shows.kristijanmitrov.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ShowEntity::class,
        ReviewEntity::class,
        UserEntity::class
    ],
    version = 1
)
abstract class Database: RoomDatabase() {

    abstract fun dao()

}
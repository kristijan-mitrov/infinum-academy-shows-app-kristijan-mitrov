package shows.kristijanmitrov.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import shows.kristijanmitrov.database.ReviewEntity

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE show_id IS :showId AND page IS :page")
    fun getAllReviewsForShow(showId: String, page: Int): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    fun getReviews(reviewId: String): LiveData<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(reviews: ReviewEntity)
}
package shows.kristijanmitrov.infinumacademyshows

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityShowDetailsBinding
import shows.kristijanmitrov.infinumacademyshows.databinding.DialogAddReviewBinding
import shows.kristijanmitrov.ui.ReviewAdapter

import android.widget.RatingBar.OnRatingBarChangeListener
import shows.kristijanmitrov.model.Show

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }


    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initShowInformation()
        initReviewRecycler()
        initWriteReviewButton()
    }

    private fun initToolbar() = with(binding) {
        //title
        nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollBounds = Rect()
            nestedScrollView.getHitRect(scrollBounds)
            if (title.getLocalVisibleRect(scrollBounds)) {
                toolbar.title = null
            } else {
                toolbar.title = binding.title.text
            }
        }

        //back
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }


    private fun initShowInformation() {
        val show = intent.getParcelableExtra<Show>("show")

        show?.let {
            with(binding) {
                title.text = show.title
                image.setImageResource(show.image)
                descriptionText.text = show.descriptionText
            }
        }
    }


    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            showWriteReviewBottomSheet()
        }
    }

    private fun showWriteReviewBottomSheet() {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        //init rating
        bottomSheetBinding.ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, rating, _ ->
                bottomSheetBinding.submitButton.isEnabled = rating > 0
            }

        //init submit button
        bottomSheetBinding.submitButton.setOnClickListener {
            val username = intent.extras?.getString("username")

            username?.let { _username ->
                adapter.addReview(
                    _username,
                    bottomSheetBinding.commentText.text.toString(),
                    bottomSheetBinding.ratingBar.rating.toInt()
                )

                val numOfReviews = adapter.itemCount
                val averageRating = adapter.getAverage()
                val reviewTextStr = String.format(getString(R.string.d_reviews_2f_average), numOfReviews, averageRating )
                with(binding) {
                    reviewText.text = reviewTextStr
                    ratingBar.rating = averageRating
                    emptyStateLayout.isVisible = false
                    reviewPanel.isVisible = true
                }
            }

            dialog.dismiss()
        }

        //init close icon
        bottomSheetBinding.close.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun initReviewRecycler() {
        adapter = ReviewAdapter()

        binding.reviewsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}



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





class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter: ReviewAdapter
    private lateinit var reviewTextTemplate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewTextTemplate = binding.reviewText.text.toString()
        val title = intent.extras?.getString("title")
        val image = intent.extras?.getInt("image")
        val descriptionText = intent.extras?.getString("descriptionText")

        binding.title.text = title
        image?.let { binding.image.setImageResource(it) }
        binding.descriptionText.text = descriptionText

        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollBounds = Rect()
            binding.nestedScrollView.getHitRect(scrollBounds)
            if (binding.title.getLocalVisibleRect(scrollBounds)) {
                binding.toolbar.title = null
            } else {
                binding.toolbar.title = title
            }
        }



        binding.toolbar.setNavigationOnClickListener{
            finish()
        }

        initReviewRecycler()
        initWriteReviewButton()
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

        bottomSheetBinding.ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, rating, _ ->
                bottomSheetBinding.submitButton.isEnabled = rating > 0
            }

        bottomSheetBinding.submitButton.setOnClickListener {
            adapter.addReview(
                bottomSheetBinding.commentText.text.toString(),
                bottomSheetBinding.ratingBar.rating.toInt()
            )

            val numOfReviews = adapter.itemCount
            val averageRating = adapter.getAverage()
            val reviewTextStr = String.format(reviewTextTemplate, numOfReviews, averageRating )
            binding.reviewText.text = reviewTextStr
            binding.ratingBar.rating = averageRating
            binding.emptyStateLayout.isVisible = false
            binding.reviewPanel.isVisible = true
            dialog.dismiss()
        }

        bottomSheetBinding.close.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun initReviewRecycler() {
        adapter = ReviewAdapter(emptyList())

        binding.reviewsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}



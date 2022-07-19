package shows.kristijanmitrov.infinumacademyshows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityShowDetailsBinding
import shows.kristijanmitrov.infinumacademyshows.databinding.DialogAddReviewBinding
import shows.kristijanmitrov.model.Review
import shows.kristijanmitrov.ui.ReviewAdapter
import shows.kristijanmitrov.ui.ShowsAdapter

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    private val items = listOf(
        Review("1", "1", "kiko.mitrov", 1, "comment"),
        Review("1", "1", "kiko.mitrov", 1, "comment"),
        Review("1", "1", "kiko.mitrov", 1, "comment"),
        Review("1", "1", "kiko.mitrov", 1, "comment"),
        Review("1", "1", "kiko.mitrov", 1, "comment")
    )

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter: ReviewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.extras?.getString("title")
        val image = intent.extras?.getInt("image")
        val descriptionText = intent.extras?.getString("descriptionText")

        binding.title.text = title
        image?.let { binding.image.setImageResource(it) }
        binding.descriptionText.text = descriptionText

        initReviewRecycler()
        initWriteReviewButton()
    }

    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener{
            showWriteReviewBottomSheet()
        }
    }

    private fun showWriteReviewBottomSheet() {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submitButton.setOnClickListener {
            adapter.addReview(bottomSheetBinding.commentText.text.toString(), bottomSheetBinding.rating.rating.toInt())
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun initReviewRecycler() {
        adapter = ReviewAdapter(items)

        binding.reviewsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}
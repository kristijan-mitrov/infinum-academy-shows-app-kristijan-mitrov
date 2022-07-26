package shows.kristijanmitrov.infinumacademyshows

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import shows.kristijanmitrov.infinumacademyshows.databinding.DialogAddReviewBinding
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentShowDetailsBinding
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.ui.ReviewAdapter

private const val USER = "USER"

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        user = sharedPreferences.getUser(USER, null)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                toolbar.title = args.show.title
            }
        }

        //back
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initShowInformation() {
        val show = args.show

        with(binding) {
            title.text = show.title
            image.setImageResource(show.image)
            descriptionText.text = show.descriptionText
        }
    }

    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            showWriteReviewBottomSheet()
        }
    }

    private fun showWriteReviewBottomSheet() {
        val dialog = context?.let { BottomSheetDialog(it) }

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(bottomSheetBinding.root)

        //init rating
        bottomSheetBinding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                bottomSheetBinding.submitButton.isEnabled = rating > 0
            }

        //init submit button
        bottomSheetBinding.submitButton.setOnClickListener {

            user.username?.let { username ->
                adapter.addReview(
                    username,
                    bottomSheetBinding.commentText.text.toString(),
                    bottomSheetBinding.ratingBar.rating.toInt()
                )

                val numOfReviews = adapter.itemCount
                val averageRating = adapter.getAverage()
                val reviewTextStr = getString(R.string.d_reviews_2f_average, numOfReviews, averageRating)
                with(binding) {
                    reviewText.text = reviewTextStr
                    ratingBar.rating = averageRating
                    emptyStateLayout.isVisible = false
                    reviewPanel.isVisible = true
                }
                dialog?.dismiss()
            }
        }

        //init close icon
        bottomSheetBinding.close.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun initReviewRecycler() {
        adapter = ReviewAdapter()

        binding.reviewsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

    private fun SharedPreferences.getUser(s: String, default: String?): User? {
        val userJson = getString(s, default)
        return userJson?.let { Json.decodeFromString(userJson) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
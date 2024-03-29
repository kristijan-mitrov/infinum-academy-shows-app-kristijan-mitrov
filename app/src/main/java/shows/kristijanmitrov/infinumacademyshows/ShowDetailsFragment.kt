package shows.kristijanmitrov.infinumacademyshows

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import shows.kristijanmitrov.infinumacademyshows.databinding.DialogAddReviewBinding
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentShowDetailsBinding
import shows.kristijanmitrov.model.Review
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.networking.ApiModule
import shows.kristijanmitrov.ui.ReviewAdapter
import shows.kristijanmitrov.viewModel.ShowDetailsViewModel
import shows.kristijanmitrov.viewModel.ShowDetailsViewModelFactory

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private val viewModel: ShowDetailsViewModel by viewModels {
        ShowDetailsViewModelFactory((activity?.application as ShowsApplication).database)
    }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: User
    private lateinit var accessToken: String
    private lateinit var client: String
    private lateinit var expiry: String
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(Constants.LOGIN_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApiModule.initRetrofit(requireContext())

        val _accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, null)
        val _client = sharedPreferences.getString(Constants.CLIENT, null)
        val _expiry = sharedPreferences.getString(Constants.CLIENT, null)
        val _uid = sharedPreferences.getString(Constants.UID, null)
        val id = sharedPreferences.getString(Constants.ID, null)
        val email = sharedPreferences.getString(Constants.EMAIL, null)
        val imageUrl = sharedPreferences.getString(Constants.IMAGE, null)

        if (id == null || email == null || _accessToken == null || _client == null || _expiry == null || _uid == null) {
            findNavController().popBackStack()
        } else {
            accessToken = _accessToken
            client = _client
            expiry = _expiry
            uid = _uid
            user = User(id, email, imageUrl)
        }

        viewModel.init(args.show, accessToken, client, expiry, uid)

        initObservers()
        initToolbar()
        initReviewRecycler()
        initWriteReviewButton()
    }

    private fun initObservers() = with(binding) {
        viewModel.show.observe(viewLifecycleOwner) { show ->
            toolbarTitle.text = show.title
            title.text = show.title
            Glide.with(requireContext()).load(show.imageUrl).into(binding.image)
            descriptionText.text = show.description
            if (show.noOfReviews > 0 && show.averageRating != null) {
                reviewText.text = getString(R.string.d_reviews_2f_average, show.noOfReviews, show.averageRating)
                ratingBar.setRating(show.averageRating)
                emptyStateLayout.isVisible = false
                reviewPanel.isVisible = true
            }
        }

        viewModel.getReviewsLiveData(args.show.id).observe(viewLifecycleOwner) { reviews ->
            if (reviews.isNotEmpty()) {

                reviewsRecycler.isVisible = true
                emptyStateLayout.isVisible = false

                adapter.submitList(reviews.map { review ->
                    Review(review.id, review.comment, review.rating, review.showId.toInt(), User(review.userId, review.email, review.imageUrl))
                })
            } else {
                reviewsRecycler.isVisible = false
                emptyStateLayout.isVisible = true
            }

            shimmerPlaceholder.stopShimmerAnimation()
            shimmerPlaceholder.visibility = View.GONE
        }

        viewModel.getReviewsResultLiveData().observe(viewLifecycleOwner) { ReviewsResponse ->
            if (!ReviewsResponse.isSuccessful){
                Toast.makeText(requireContext(), "Reviews failed to load from API", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun initToolbar() = with(binding) {
        //title
        nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollBounds = Rect()
            nestedScrollView.getHitRect(scrollBounds)
            toolbarTitle.isVisible = !title.getLocalVisibleRect(scrollBounds)
        }

        //back
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            showWriteReviewBottomSheet()
        }
    }

    private fun showWriteReviewBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        //init rating
        bottomSheetBinding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                bottomSheetBinding.submitButton.isEnabled = rating > 0
            }

        //init submit button
        bottomSheetBinding.submitButton.setOnClickListener {
            viewModel.addReview(
                bottomSheetBinding.ratingBar.rating.toInt(),
                bottomSheetBinding.commentText.text.toString(),
                args.show,
                accessToken,
                client,
                expiry,
                uid
            )
            dialog.dismiss()
        }

        //init close icon
        bottomSheetBinding.close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initReviewRecycler() {
        adapter = ReviewAdapter()

        with(binding) {
            reviewsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            reviewsRecycler.adapter = adapter
            reviewsRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
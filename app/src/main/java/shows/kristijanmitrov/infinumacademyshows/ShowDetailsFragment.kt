package shows.kristijanmitrov.infinumacademyshows

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
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
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.ui.ReviewAdapter
import shows.kristijanmitrov.viewModel.ShowDetailsViewModel

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private val viewModel by viewModels<ShowDetailsViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(Constants.LOGIN_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.USERNAME, null)
        val email = sharedPreferences.getString(Constants.EMAIL, null)
        val profilePhoto = sharedPreferences.getString(Constants.PROFILE_PHOTO, null)

        if(username == null || email == null){
            val directions = ShowsFragmentDirections.toLoginFragment()
            findNavController().navigate(directions)
        }else user = User(username, email, profilePhoto)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(args.show)

        viewModel.show.observe(viewLifecycleOwner) { show ->
            with(binding) {
                toolbarTitle.text = show.title
                title.text = show.title
                Glide.with(requireContext()).load(show.imageUrl).into(binding.image)
                descriptionText.text = show.description
            }
        }

        viewModel.ratingData.observe(viewLifecycleOwner) { ratingData ->
            binding.reviewText.text = getString(R.string.d_reviews_2f_average, ratingData.numOfReviews, ratingData.averageRating)
        }

        viewModel.ratingBar.observe(viewLifecycleOwner) { averageRating ->
            with(binding) {
                ratingBar.rating = averageRating
                emptyStateLayout.isVisible = false
                reviewPanel.isVisible = true
            }
        }

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            adapter.submitList(reviews)
        }

        initToolbar()
        initReviewRecycler()
        initWriteReviewButton()
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
            viewModel.addReview(user, bottomSheetBinding.commentText.text.toString(), bottomSheetBinding.ratingBar.rating.toInt())
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
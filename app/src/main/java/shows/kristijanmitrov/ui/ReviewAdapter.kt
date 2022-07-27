package shows.kristijanmitrov.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewReviewItemBinding
import shows.kristijanmitrov.model.Review
import shows.kristijanmitrov.model.User

class ReviewAdapter(
    private val items: MutableList<Review> = ArrayList(),
): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun addReview(user: User, commentText: String, rating: Int) {
        val review = Review(user, rating, commentText)
        items.add(review)
        notifyItemInserted(items.lastIndex)
    }

    fun getAverage() = items.sumOf { it.ratingValue }/itemCount.toFloat()

    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) = with(binding){
            item.user.profilePhoto?.let{
                val profilePhotoUri = Uri.parse(item.user.profilePhoto)
                profilePhoto.setImageURI(profilePhotoUri)
            }
            username.text = item.user.username
            descriptionText.text = item.descriptionText
            ratingValue.text = item.ratingValue.toString()
        }
    }

}
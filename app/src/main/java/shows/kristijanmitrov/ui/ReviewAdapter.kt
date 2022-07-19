package shows.kristijanmitrov.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewReviewItemBinding
import shows.kristijanmitrov.model.Review

class ReviewAdapter(
    private var items: List<Review>,
): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun addReview(commentText: String, rating: Int) {
        items = items + Review(items.count().toString(), "1", "1", rating, commentText)
        notifyItemInserted(items.lastIndex)
    }


    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            binding.user.text = item.user
            binding.descriptionText.text = item.descriptionText
            binding.ratingValue.text = item.ratingValue.toString()
        }
    }

}
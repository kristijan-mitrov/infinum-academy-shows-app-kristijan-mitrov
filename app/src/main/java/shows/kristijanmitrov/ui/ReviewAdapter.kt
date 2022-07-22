package shows.kristijanmitrov.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewReviewItemBinding
import shows.kristijanmitrov.model.Review

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

    fun addReview(username: String, commentText: String, rating: Int) {
        val review = Review( username, rating, commentText)
        items.add(review)
        notifyItemInserted(items.lastIndex)
    }

    fun getAverage() = items.sumOf { it.ratingValue }/itemCount.toFloat()


    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) = with(binding){
            username.text = item.username
            descriptionText.text = item.descriptionText
            ratingValue.text = item.ratingValue.toString()
        }
    }

}
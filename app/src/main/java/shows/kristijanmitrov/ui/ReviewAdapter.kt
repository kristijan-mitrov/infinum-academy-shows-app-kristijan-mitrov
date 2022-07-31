package shows.kristijanmitrov.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewReviewItemBinding
import shows.kristijanmitrov.model.Review

class ReviewAdapter: ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(
    object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean = oldItem == newItem
    }){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) = with(binding){
            item.user.imageUrl?.let{
                if(URLUtil.isValidUrl(item.user.imageUrl)){
                    Glide.with(profilePhoto.context).load(item.user.imageUrl).into(profilePhoto)
                }else{
                val profilePhotoUri = Uri.parse(item.user.imageUrl)
                profilePhoto.setImageURI(profilePhotoUri)
                    }
            }
            username.text = item.user.getUsername()
            descriptionText.text = item.comment
            ratingValue.text = item.rating.toString()
        }
    }

}
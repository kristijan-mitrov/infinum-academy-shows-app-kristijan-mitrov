package shows.kristijanmitrov.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewShowItemBinding
import shows.kristijanmitrov.model.Show

class ShowsAdapter(
    private val items: MutableList<Show> = ArrayList(),
    private val onItemClickCallback: (Show) -> Unit
): RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun setShows(shows: List<Show>) {
        items.addAll(shows)
        notifyItemInserted(items.lastIndex)
    }

    fun clearShows() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) = with(binding) {
            title.text = item.title
            descriptionText.text = item.description
            Glide.with(binding.image.context).load(item.imageUrl).into(binding.image)
            card.setOnClickListener{
                onItemClickCallback(item)
            }
        }
    }
}
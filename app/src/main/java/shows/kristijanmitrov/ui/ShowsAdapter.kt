package shows.kristijanmitrov.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewShowItemBinding
import shows.kristijanmitrov.model.Show

class ShowsAdapter(
    private var items: List<Show>,
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
        items = shows
        notifyDataSetChanged()
    }


    inner class ShowViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.title.text = item.title
            binding.descriptionText.text = item.descriptionText
            binding.image.setImageResource(item.image)
            binding.card.setOnClickListener{
                onItemClickCallback(item)
            }
        }
    }
}
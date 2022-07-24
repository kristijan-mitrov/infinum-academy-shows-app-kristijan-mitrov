package shows.kristijanmitrov.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shows.kristijanmitrov.infinumacademyshows.R
import shows.kristijanmitrov.infinumacademyshows.databinding.ViewShowItemBinding
import shows.kristijanmitrov.model.Show

class ShowsAdapter(
    private val items: MutableList<Show> = ArrayList(),
    private val onItemClickCallback: (Show) -> Unit
): RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>(){

    //Shows List
    private val shows = arrayListOf(
        Show("1", "Stranger Things", "desc", R.drawable.stranger_things),
        Show("2", "Dark", "desc", R.drawable.dark),
        Show("3", "How I Met Your Mother", "desc", R.drawable.himym),
        Show("4", "Game of Thrones", "desc", R.drawable.game_of_thrones),
        Show("5", "Sex Education", "desc", R.drawable.sex_education),
        Show("6", "Young Royals", "desc", R.drawable.young_royals),
        Show("7", "The Crown", "desc", R.drawable.the_crown),
        Show("8", "The Queens Gambit", "desc", R.drawable.the_queens_gambit)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun setShows() {
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
            descriptionText.text = item.descriptionText
            image.setImageResource(item.image)
            card.setOnClickListener{
                onItemClickCallback(item)
            }
        }
    }
}
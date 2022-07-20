package shows.kristijanmitrov.infinumacademyshows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityShowsBinding
import shows.kristijanmitrov.model.Show
import shows.kristijanmitrov.ui.ShowsAdapter

class ShowsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }
    }

    //Shows List
    private val shows = listOf(
        Show("1", "Stranger Things", "desc", R.drawable.stranger_things),
        Show("2", "Dark", "desc", R.drawable.dark),
        Show("3", "How I Met Your Mother", "desc", R.drawable.himym),
        Show("4", "Game of Thrones", "desc", R.drawable.game_of_thrones),
        Show("5", "Sex Education", "desc", R.drawable.sex_education),
        Show("6", "Young Royals", "desc", R.drawable.young_royals),
        Show("7", "The Crown", "desc", R.drawable.the_crown),
        Show("8", "The Queens Gambit", "desc", R.drawable.the_queens_gambit)
    )

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowRecycler()
        initShowHideButton()
    }

    private fun initShowRecycler() {
        adapter = ShowsAdapter(shows) { show ->
            val intent = ShowDetailsActivity.buildIntent(this)
            intent.putExtra("id", show.id)
            intent.putExtra("title", show.title)
            intent.putExtra("image", show.image)
            intent.putExtra("descriptionText", show.descriptionText)
            startActivity(intent)
        }

        binding.showsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = adapter
    }

    private fun initShowHideButton() {
        binding.showHideButton.setOnClickListener {
            if (adapter.itemCount == 0) {
                adapter.setShows(shows)
                binding.showsRecycler.isVisible = true
                binding.emptyStateLayout.isVisible = false
                binding.showHideButton.text = "Hide"
            } else {
                adapter.setShows(emptyList())
                binding.showsRecycler.isVisible = false
                binding.emptyStateLayout.isVisible = true
                binding.showHideButton.text = "Show"
            }
        }
    }
}
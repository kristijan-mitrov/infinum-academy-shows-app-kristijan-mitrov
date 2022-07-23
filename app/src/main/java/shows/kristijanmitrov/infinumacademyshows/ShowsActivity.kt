package shows.kristijanmitrov.infinumacademyshows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityShowsBinding
import shows.kristijanmitrov.ui.ShowsAdapter

class ShowsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }
    }

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
        val username = intent.extras?.getString("username")

        adapter = ShowsAdapter { show ->
            val intent = ShowDetailsActivity.buildIntent(this)
            intent.putExtra("show", show)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        adapter.setShows()

        binding.showsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = adapter
    }

    private fun initShowHideButton() = with(binding) {
        binding.showHideButton.setOnClickListener{
            if (adapter.itemCount == 0) {
                adapter.setShows()
                showsRecycler.isVisible = true
                emptyStateLayout.isVisible = false
                showHideButton.text = getString(R.string.hide)
            } else {
                adapter.clearShows()
                showsRecycler.isVisible = false
                emptyStateLayout.isVisible = true
                showHideButton.text = getString(R.string.show)
            }
        }
    }
}
package shows.kristijanmitrov.infinumacademyshows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentShowsBinding
import shows.kristijanmitrov.ui.ShowsAdapter

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShowsAdapter
    private val args by navArgs<ShowsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowRecycler()
        initShowHideButton()
        initLogoutButton()
    }

    private fun initLogoutButton() {
        binding.logoutIcon.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun initShowRecycler() {
        adapter = ShowsAdapter { show ->
            val directions = ShowsFragmentDirections.toShowDetailsFragment(args.username, show)
            findNavController().navigate(directions)
        }

        binding.showsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.showsRecycler.adapter = adapter

        adapter.setShows()
    }

    private fun initShowHideButton() = with(binding) {
        binding.showHideButton.setOnClickListener {
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
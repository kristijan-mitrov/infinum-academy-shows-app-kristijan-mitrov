package shows.kristijanmitrov.infinumacademyshows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
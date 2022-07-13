package shows.kristijanmitrov.infinumacademyshows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeText.text = intent.extras?.getString("username")

    }
}
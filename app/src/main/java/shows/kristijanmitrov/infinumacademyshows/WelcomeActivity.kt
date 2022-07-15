package shows.kristijanmitrov.infinumacademyshows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val welcomeMessage = "Welcome, ${intent.extras?.getString("USERNAME_ARG")}!"

        binding.welcomeText.text = welcomeMessage

    }
}
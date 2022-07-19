package shows.kristijanmitrov.infinumacademyshows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private fun check() {
        if (!(binding.emailInput.error != null || binding.emailText.text == null || binding.passwordInput.error != null || binding.passwordText.text == null))
            binding.loginButton.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.emailText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))) {
                binding.emailInput.error = null
                check()
            } else {
                binding.emailInput.error = "Email must be in correct format!"
                binding.loginButton.isEnabled = false
            }
        }

        binding.passwordText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.length < 6) {
                    binding.passwordInput.error = "Password must be at least 6 characters long!"
                    binding.loginButton.isEnabled = false
                } else {
                    binding.passwordInput.error = null
                    check()
                }
            }
        }

        binding.loginButton.setOnClickListener {

            val username = binding.emailText.text.toString().split("@")[0]

            val intent = ShowsActivity.buildIntent(this)
            intent.putExtra("username", username)
            startActivity(intent)

        }
    }
}
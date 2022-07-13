package shows.kristijanmitrov.infinumacademyshows

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import shows.kristijanmitrov.infinumacademyshows.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private fun validate(){
        if(binding.emailInput.error == null && binding.passwordInput.error == null) {
            binding.loginButton.isEnabled = true
            binding.loginButton.setTextColor(Color.parseColor("#52368C"))
            binding.loginButton.setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }

    private fun invalidate(){
        binding.loginButton.isEnabled = false
        binding.loginButton.setTextColor(Color.parseColor("#ffffff"))
        binding.loginButton.setBackgroundColor(Color.parseColor("#BBBBBB"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.emailText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))) {
                binding.emailInput.error = null
                validate()
            } else{
                binding.emailInput.error = "Email must be in correct format!"
                invalidate()
            }
        }

        binding.passwordText.doOnTextChanged { text, _, _, _ ->
            if(text!!.length < 6) {
                binding.passwordInput.error = "Password must be at least 6 characters long!"
                invalidate()
            }else {
                validate()
                binding.passwordInput.error = null
            }
        }

        binding.loginButton.setOnClickListener{
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("username", "Welcome, ${binding.emailText.text.toString().split("@")[0]}!")
            startActivity(intent)
        }
    }
}
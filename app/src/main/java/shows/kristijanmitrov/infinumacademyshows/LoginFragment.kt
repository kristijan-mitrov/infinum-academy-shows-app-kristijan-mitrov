package shows.kristijanmitrov.infinumacademyshows

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentLoginBinding
import shows.kristijanmitrov.model.User
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import shows.kristijanmitrov.ViewModel.LoginViewModel

private const val REMEMBER_ME = "REMEMBER_ME"
private const val USER = "USER"

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(sharedPreferences.getBoolean(REMEMBER_ME, false)){

            val directions = LoginFragmentDirections.toShowsFragment()
            findNavController().navigate(directions)
        }



        initEmailInput()
        initPasswordInput()
        initLoginButton()
    }

    private fun check() {
        if (!(binding.emailInput.error != null || binding.emailText.text == null || binding.passwordInput.error != null || binding.passwordText.text == null))
            binding.loginButton.isEnabled = true
    }

    private fun initEmailInput() {
        binding.emailText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))) {
                binding.emailInput.error = null
                check()
            } else {
                binding.emailInput.error = "Email must be in correct format!"
                binding.loginButton.isEnabled = false
            }
        }
    }

    private fun initPasswordInput() {
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
    }

    private fun initLoginButton() {

        binding.loginButton.setOnClickListener {
            val username = binding.emailText.text.toString().split("@")[0]
            val user = User(username, binding.emailText.text.toString(), R.drawable.squidward)

            sharedPreferences.edit {
                if (binding.rememberMeCheckbox.isChecked) putBoolean(REMEMBER_ME, true)
                putUser(USER, user)
            }

            val directions = LoginFragmentDirections.toShowsFragment()

            findNavController().navigate(directions)
        }
    }

    private fun SharedPreferences.Editor.putUser(s: String, user: User?) {
        // TODO: When not checking REMEMBER ME should this be set to null, does this release the memory or does it still use it?
        val json = if (user!=null) Json.encodeToString(user) else null
        putString(s, json)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



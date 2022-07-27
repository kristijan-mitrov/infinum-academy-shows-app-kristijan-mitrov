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
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.viewModel.LoginViewModel

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

        //Observers
        viewModel.isLoginButtonEnabled.observe(viewLifecycleOwner){ isLoginButtonEnabled ->
            binding.loginButton.isEnabled = isLoginButtonEnabled
        }
        viewModel.emailError.observe(viewLifecycleOwner){ emailError ->
            binding.emailInput.error = emailError
        }
        viewModel.passwordError.observe(viewLifecycleOwner){ passwordError ->
            binding.passwordInput.error = passwordError
        }

        initListeners()
        initLoginButton()
    }

    private fun initListeners() {
        binding.emailText.doOnTextChanged { text, _, _, _ ->
            viewModel.checkLoginValidity(text.toString(), binding.passwordText.text.toString())
        }
        binding.passwordText.doOnTextChanged { text, _, _, _ ->
            viewModel.checkLoginValidity(binding.emailText.text.toString(), text.toString())
        }
    }

    private fun initLoginButton() {

        binding.loginButton.setOnClickListener {
            val username = binding.emailText.text.toString().split("@")[0]
            val email = binding.emailText.text.toString()
            val user = User(username, email, null)
            sharedPreferences.edit {
                if (binding.rememberMeCheckbox.isChecked) putBoolean(REMEMBER_ME, true)
                putUser(USER, user)
            }

            val directions = LoginFragmentDirections.toShowsFragment()

            findNavController().navigate(directions)
        }
    }

    private fun SharedPreferences.Editor.putUser(s: String, user: User) {
        val json = Json.encodeToString(user)
        putString(s, json)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



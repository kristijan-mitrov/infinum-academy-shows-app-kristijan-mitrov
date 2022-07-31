package shows.kristijanmitrov.infinumacademyshows

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentLoginBinding
import shows.kristijanmitrov.networking.ApiModule
import shows.kristijanmitrov.viewModel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private val args by navArgs<LoginFragmentArgs>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(Constants.LOGIN_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedPreferences.getBoolean(Constants.REMEMBER_ME, false)) {
            val directions = LoginFragmentDirections.toShowsFragment()
            findNavController().navigate(directions)
        }

        if(!args.initial){
            binding.loginText.text = getString(R.string.registration_successful)
            binding.loginText.textSize = 30F
            binding.registerButton.isVisible = false
        }

        ApiModule.initRetrofit(requireContext())

        initObservers()
        initListeners()
        initLoginButton()
        initRegisterButton()
    }

    private fun initObservers() = with(viewModel) {
        isLoginButtonEnabled.observe(viewLifecycleOwner) { isLoginButtonEnabled ->
            binding.loginButton.isEnabled = isLoginButtonEnabled
        }
        emailError.observe(viewLifecycleOwner) { emailError ->
            binding.emailInput.error = if (emailError == null) null else getString(emailError)
        }
        passwordError.observe(viewLifecycleOwner) { passwordError ->
            binding.passwordInput.error = if (passwordError == null) null else getString(passwordError)
        }
        getSignInResponseLiveData().observe(viewLifecycleOwner){ signInResponse ->
            if(signInResponse.isSuccessful){
                sharedPreferences.edit {
                    if (binding.rememberMeCheckbox.isChecked) putBoolean(Constants.REMEMBER_ME, true)
                    putString(Constants.ID, signInResponse.body?.user?.id)
                    putString(Constants.EMAIL, signInResponse.body?.user?.email)
                    putString(Constants.IMAGE, signInResponse.body?.user?.imageUrl)
                    putString(Constants.ACCESS_TOKEN, signInResponse.header?.accessToken)
                    putString(Constants.CLIENT, signInResponse.header?.client)
                    putString(Constants.EXPIRY, signInResponse.header?.expiry)
                    putString(Constants.UID, signInResponse.header?.uid)
                }

                val directions = LoginFragmentDirections.toShowsFragment()
                findNavController().navigate(directions)
            }else{
                Toast.makeText(requireContext(), getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            val directions = LoginFragmentDirections.toRegisterFragment()
            findNavController().navigate(directions)
        }
    }

    private fun initListeners() {
        binding.emailText.doOnTextChanged { text, _, _, _ ->
            viewModel.onLoginInputChanged(text.toString(), binding.passwordText.text.toString())
        }
        binding.passwordText.doOnTextChanged { text, _, _, _ ->
            viewModel.onLoginInputChanged(binding.emailText.text.toString(), text.toString())
        }
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            viewModel.onLogInButtonClicked(binding.emailText.text.toString(), binding.passwordText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



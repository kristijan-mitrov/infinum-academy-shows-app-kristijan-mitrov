package shows.kristijanmitrov.infinumacademyshows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            val directions = LoginFragmentDirections.toShowsFragment(username)

            findNavController().navigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
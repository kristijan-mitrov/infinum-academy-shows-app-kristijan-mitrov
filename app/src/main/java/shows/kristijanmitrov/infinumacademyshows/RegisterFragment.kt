package shows.kristijanmitrov.infinumacademyshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentRegisterBinding
import shows.kristijanmitrov.viewModel.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observers
        viewModel.isRegisterButtonEnabled.observe(viewLifecycleOwner){ isRegisterButtonEnabled ->
            binding.registerButton.isEnabled = isRegisterButtonEnabled
        }
        viewModel.emailError.observe(viewLifecycleOwner){ emailError ->
            binding.emailInput.error = if (emailError == null) null else getString(emailError)
        }
        viewModel.passwordError.observe(viewLifecycleOwner){ passwordError ->
            binding.passwordInput.error = if (passwordError == null) null else getString(passwordError)
        }
        viewModel.repeatPasswordError.observe(viewLifecycleOwner){ repeatPasswordError ->
            binding.repeatPasswordInput.error = if (repeatPasswordError == null) null else getString(repeatPasswordError)
        }

        initListeners()
        initRegisterButton()
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            val directions = RegisterFragmentDirections.toLoginFragment(false)
            findNavController().navigate(directions)
        }
    }

    private fun initListeners() {
        binding.emailText.doOnTextChanged { text, _, _, _ ->
            viewModel.checkRegisterValidity(text.toString(), binding.passwordText.text.toString(), binding.repeatPasswordText.text.toString())
        }
        binding.passwordText.doOnTextChanged { text, _, _, _ ->
            viewModel.checkRegisterValidity(binding.emailText.text.toString(), text.toString(), binding.repeatPasswordText.text.toString())
        }
        binding.repeatPasswordText.doOnTextChanged { text, _, _, _ ->
            viewModel.checkRegisterValidity(binding.emailText.text.toString(), binding.passwordText.text.toString(), text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
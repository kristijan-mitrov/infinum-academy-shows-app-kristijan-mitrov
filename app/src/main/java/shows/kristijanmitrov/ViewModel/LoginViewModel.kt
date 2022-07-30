package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shows.kristijanmitrov.infinumacademyshows.R

class LoginViewModel : ViewModel() {

    private val _isLoginButtonEnabled = MutableLiveData(false)
    private val _emailError:MutableLiveData<Int?> = MutableLiveData()
    private val _passwordError:MutableLiveData<Int?> = MutableLiveData()

    val isLoginButtonEnabled: LiveData<Boolean> = _isLoginButtonEnabled
    val emailError: LiveData<Int?> = _emailError
    val passwordError: LiveData<Int?> = _passwordError

    fun onLoginInputChanged(emailText:String, passwordText:String) {
        val validEmail = emailText.matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))
        val validPassword = passwordText.length >= 6

        _emailError.value = if(validEmail || emailText.isEmpty()) null else R.string.email_error
        _passwordError.value = if(validPassword || passwordText.isEmpty()) null else R.string.password_error
        _isLoginButtonEnabled.value = validEmail && validPassword
    }
}
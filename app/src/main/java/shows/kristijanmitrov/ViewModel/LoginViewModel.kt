package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _isLoginButtonEnabled = MutableLiveData(false)
    private val _emailError:MutableLiveData<String> = MutableLiveData()
    private val _passwordError:MutableLiveData<String> = MutableLiveData()

    val isLoginButtonEnabled: LiveData<Boolean> = _isLoginButtonEnabled
    val emailError: LiveData<String> = _emailError
    val passwordError: LiveData<String> = _passwordError

    fun checkLoginValidity(emailText:String, passwordText:String) {
        val validEmail = emailText.matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))
        val validPassword = passwordText.length >= 6

        if(validEmail && validPassword) {
            _emailError.value = ""
            _passwordError.value = ""
            _isLoginButtonEnabled.value = true
        }else {
            _emailError.value = if(!validEmail) "Email must be in correct format!" else ""
            _passwordError.value = if(!validPassword) "Password must be at least 6 characters long!" else ""
            _isLoginButtonEnabled.value = false
        }
    }
}
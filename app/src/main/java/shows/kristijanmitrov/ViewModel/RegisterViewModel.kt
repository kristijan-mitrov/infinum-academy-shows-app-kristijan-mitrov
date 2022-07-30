package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shows.kristijanmitrov.infinumacademyshows.R

class RegisterViewModel: ViewModel() {

    private val _isRegisterButtonEnabled = MutableLiveData(false)
    private val _emailError = MutableLiveData<Int>()
    private val _passwordError:MutableLiveData<Int> = MutableLiveData()
    private val _repeatPasswordError:MutableLiveData<Int> = MutableLiveData()

    val isRegisterButtonEnabled: LiveData<Boolean> = _isRegisterButtonEnabled
    val emailError: LiveData<Int> = _emailError
    val passwordError: LiveData<Int> = _passwordError
    val repeatPasswordError: LiveData<Int> = _repeatPasswordError

    fun checkRegisterValidity(emailText: String, passwordText: String, repeatPasswordText: String){
        val validEmail = emailText.matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))
        val validPassword = passwordText.length >= 6
        val validRepeatPassword = passwordText == repeatPasswordText

        _emailError.value = if(validEmail || emailText.isEmpty()) null else R.string.email_error
        _passwordError.value = if(validPassword || passwordText.isEmpty()) null else R.string.password_error
        _repeatPasswordError.value = if(validRepeatPassword || repeatPasswordText.isEmpty()) null else R.string.repeat_password_error
        _isRegisterButtonEnabled.value = validEmail && validPassword && validRepeatPassword
    }

}
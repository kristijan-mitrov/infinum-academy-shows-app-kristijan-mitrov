package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.infinumacademyshows.R
import shows.kristijanmitrov.networking.ApiModule
import shows.kristijanmitrov.networking.model.RegisterRequest
import shows.kristijanmitrov.networking.model.RegisterResponse

class RegisterViewModel: ViewModel() {

    private val _isRegisterButtonEnabled = MutableLiveData(false)
    private val _emailError = MutableLiveData<Int>()
    private val _passwordError:MutableLiveData<Int> = MutableLiveData()
    private val _repeatPasswordError:MutableLiveData<Int> = MutableLiveData()
    private val registrationResultLiveData: MutableLiveData<Boolean> by lazy {MutableLiveData<Boolean>()}

    val isRegisterButtonEnabled: LiveData<Boolean> = _isRegisterButtonEnabled
    val emailError: LiveData<Int> = _emailError
    val passwordError: LiveData<Int> = _passwordError
    val repeatPasswordError: LiveData<Int> = _repeatPasswordError

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registrationResultLiveData
    }

    fun onRegisterButtonClicked (email: String, password: String, passwordConfirmation: String) {
        val registerRequest = RegisterRequest(
            email = email,
            password = password,
            passwordConfirmation = passwordConfirmation
        )

        ApiModule.retrofit.register(registerRequest)
            .enqueue(object: Callback<RegisterResponse>{
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    registrationResultLiveData.value = response.isSuccessful
                    if(!response.isSuccessful) _emailError.value = R.string.email_is_already_taken
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registrationResultLiveData.value = false
                }

            })
    }

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
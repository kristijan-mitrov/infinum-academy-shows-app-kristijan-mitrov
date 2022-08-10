package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.infinumacademyshows.Constants
import shows.kristijanmitrov.infinumacademyshows.R
import shows.kristijanmitrov.model.api.SignInRequest
import shows.kristijanmitrov.model.api.SignInResponse
import shows.kristijanmitrov.model.api.SignInResponseBody
import shows.kristijanmitrov.model.api.SignInResponseHeader
import shows.kristijanmitrov.networking.ApiModule

class LoginViewModel : ViewModel() {

    private val _isLoginButtonEnabled = MutableLiveData(false)
    private val _emailError: MutableLiveData<Int?> = MutableLiveData()
    private val _passwordError: MutableLiveData<Int?> = MutableLiveData()
    private val signInResponseLiveData: MutableLiveData<SignInResponse> by lazy { MutableLiveData<SignInResponse>() }

    val isLoginButtonEnabled: LiveData<Boolean> = _isLoginButtonEnabled
    val emailError: LiveData<Int?> = _emailError
    val passwordError: LiveData<Int?> = _passwordError

    fun getSignInResponseLiveData(): LiveData<SignInResponse> {
        return signInResponseLiveData
    }

    fun onLoginInputChanged(emailText: String, passwordText: String) {
        val validEmail = emailText.matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))
        val validPassword = passwordText.length >= 6

        _emailError.value = if (validEmail || emailText.isEmpty()) null else R.string.email_error
        _passwordError.value = if (validPassword || passwordText.isEmpty()) null else R.string.password_error
        _isLoginButtonEnabled.value = validEmail && validPassword
    }

    fun onLogInButtonClicked(email: String, password: String) {
        val signInRequest = SignInRequest(
            email = email,
            password = password
        )

        ApiModule.retrofit.signIn(signInRequest)
            .enqueue(object : Callback<SignInResponseBody> {
                override fun onResponse(call: Call<SignInResponseBody>, response: Response<SignInResponseBody>) {
                    val header = SignInResponseHeader(
                        accessToken = response.headers()[Constants.ACCESS_TOKEN],
                        client = response.headers()[Constants.CLIENT],
                        expiry = response.headers()[Constants.EXPIRY],
                        uid = response.headers()[Constants.UID]
                    )

                    val signInResponse = SignInResponse(
                        isSuccessful = response.isSuccessful,
                        header = header,
                        body = response.body()
                    )

                    signInResponseLiveData.value = signInResponse
                }

                override fun onFailure(call: Call<SignInResponseBody>, t: Throwable) {
                    val signInResponse = SignInResponse(
                        isSuccessful = false
                    )
                    signInResponseLiveData.value = signInResponse
                }
            })
    }
}
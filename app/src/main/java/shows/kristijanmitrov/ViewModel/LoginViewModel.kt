package shows.kristijanmitrov.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shows.kristijanmitrov.infinumacademyshows.Constants
import shows.kristijanmitrov.infinumacademyshows.R
import shows.kristijanmitrov.model.UserAuthenticationData
import shows.kristijanmitrov.networking.ApiModule
import shows.kristijanmitrov.networking.model.SignInRequest
import shows.kristijanmitrov.networking.model.SignInResponse

class LoginViewModel : ViewModel() {

    private val _isLoginButtonEnabled = MutableLiveData(false)
    private val _emailError:MutableLiveData<Int?> = MutableLiveData()
    private val _passwordError:MutableLiveData<Int?> = MutableLiveData()
    private val signInResultLiveData: MutableLiveData<UserAuthenticationData> by lazy {MutableLiveData<UserAuthenticationData>()}


    val isLoginButtonEnabled: LiveData<Boolean> = _isLoginButtonEnabled
    val emailError: LiveData<Int?> = _emailError
    val passwordError: LiveData<Int?> = _passwordError

    fun getSignInResultLiveData(): LiveData<UserAuthenticationData> {
        return signInResultLiveData
    }

    fun onLoginInputChanged(emailText:String, passwordText:String) {
        val validEmail = emailText.matches(Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$"))
        val validPassword = passwordText.length >= 6

        _emailError.value = if(validEmail || emailText.isEmpty()) null else R.string.email_error
        _passwordError.value = if(validPassword || passwordText.isEmpty()) null else R.string.password_error
        _isLoginButtonEnabled.value = validEmail && validPassword
    }

    fun onLogInButtonClicked (email: String, password: String) {
        val signInRequest = SignInRequest(
            email = email,
            password = password
        )

        ApiModule.retrofit.signIn(signInRequest)
            .enqueue(object: Callback<SignInResponse> {
                override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                    val userAuthenticationData = UserAuthenticationData(
                        isSuccessful = response.isSuccessful,
                        accessToken = response.headers()[Constants.ACCESS_TOKEN],
                        client = response.headers()[Constants.CLIENT],
                        expiry = response.headers()[Constants.EXPIRY],
                        uid = response.headers()[Constants.UID]
                    )
                    signInResultLiveData.value = userAuthenticationData
                }

                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    val userAuthenticationData = UserAuthenticationData(
                        isSuccessful = false
                    )
                    signInResultLiveData.value = userAuthenticationData

                }

            })
    }
}
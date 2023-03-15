package com.example.kotlinjetpack.view_model

import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.const.NAME
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.model.LoginResponse
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()
    // results = 0: Loading, 1: Success, 2: Failure

    var loginData: LoginResponse? = null
    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun login(phone: String, password: String) {
        resultLiveData.postValue(0)

        val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
        val loginBody = HashMap<String, String>()
        loginBody["phone"] = phone
        loginBody["password"] = password
        GlobalScope.launch {
            val response = quotesApi.login(loginBody)
            if (response.isSuccessful) {
                loginData = response.body()
                ACCESS_TOKEN = loginData!!.token!!

                val profileResponse = quotesApi.getProfile(ACCESS_TOKEN)
                if (profileResponse.isSuccessful) {
                    PHONE = profileResponse.body()!!.profile.phone
                    NAME = profileResponse.body()!!.profile.name

                    print("$PHONE $NAME")
                }

                resultLiveData.postValue(1)
            } else {
                val errorObject = JSONObject(response.errorBody()!!.string())
                errorMessage = errorObject.getString("message")
                resultLiveData.postValue(2)
            }
        }
    }
}

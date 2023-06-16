package com.example.kotlinjetpack.view_model

import RetrofitApi
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.const.NAME
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
class LoginAndRegisterViewModel() : ViewModel() {

    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun login(
        phone: String,
        password: String,
        onLoading: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        retrofitApi: RetrofitApi = ApiService.getInstance().create(RetrofitApi::class.java)
    ) {
        onLoading?.invoke()
        GlobalScope.launch {
            runCatching {
                val loginBody = HashMap<String, String>()
                loginBody["phone"] = phone
                loginBody["password"] = password
                val loginResponse = retrofitApi.login(loginBody)
                if (loginResponse.isSuccessful) {
                    ACCESS_TOKEN = loginResponse.body()!!.token!!
                } else {
                    val errorObject = JSONObject(loginResponse.errorBody()!!.string())
                    errorMessage = errorObject.getString("message")
                    onError?.invoke()
                }
            }.onSuccess {
                val profileResponse = retrofitApi.getProfile(ACCESS_TOKEN)
                if (profileResponse.isSuccessful) {
                    PHONE = profileResponse.body()!!.profile.phone
                    NAME = profileResponse.body()!!.profile.name
                    onSuccess?.invoke()
                } else {
                    val errorObject = JSONObject(profileResponse.errorBody()!!.string())
                    errorMessage = errorObject.getString("message")
                    onError?.invoke()
                }
            }.onFailure {
                errorMessage = it.message
                onError?.invoke()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun register(
        phone: String,
        password: String,
        reEnterPassword: String,
        name: String,
        dateOfBirth: String,
        onLoading: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        retrofitApi: RetrofitApi = ApiService.getInstance().create(RetrofitApi::class.java)
    ) {
        onLoading?.invoke()

        if (password != reEnterPassword) {
            errorMessage = "Please re-enter correct password"
            onError?.invoke()
        } else if (name.isEmpty()) {
            errorMessage = "Name must not be empty"
            onError?.invoke()
        } else if (dateOfBirth.isEmpty()) {
            errorMessage = "Birthday must not be empty"
            onError?.invoke()
        } else {
            GlobalScope.launch {
                runCatching {
                    val registerBody = HashMap<String, String>()
                    registerBody["phone"] = phone
                    registerBody["password"] = password
                    val registerResponse = retrofitApi.register(registerBody)
                    if (registerResponse.isSuccessful) {
                        val addProfileBody = HashMap<String, String>()
                        addProfileBody["phone"] = phone
                        addProfileBody["name"] = name
                        addProfileBody["birthday"] = dateOfBirth
                        addProfileBody["avatar"] = ""
                        val addProfileResponse = retrofitApi.addProfile(addProfileBody)
                        if (addProfileResponse.isSuccessful) {
                            val loginBody = HashMap<String, String>()
                            loginBody["phone"] = phone
                            loginBody["password"] = password
                            val loginResponse = retrofitApi.login(loginBody)
                            if (loginResponse.isSuccessful) {
                                val tempAccessToken = loginResponse.body()!!.token
                                retrofitApi.createContact(tempAccessToken!!)
                                onSuccess?.invoke()
                            } else {
                                val errorObject = JSONObject(loginResponse.errorBody()!!.string())
                                errorMessage = errorObject.getString("message")
                                onError?.invoke()
                            }
                        } else {
                            val errorObject = JSONObject(addProfileResponse.errorBody()!!.string())
                            errorMessage = errorObject.getString("message")
                            onError?.invoke()
                        }
                    } else {
                        val errorObject = JSONObject(registerResponse.errorBody()!!.string())
                        errorMessage = errorObject.getString("message")
                        onError?.invoke()
                    }
                }.onFailure {
                    errorMessage = it.message
                    onError?.invoke()
                }
            }
        }

    }
}
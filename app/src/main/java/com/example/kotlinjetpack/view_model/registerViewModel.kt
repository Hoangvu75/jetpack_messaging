package com.example.kotlinjetpack.view_model

import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.model.AddProfileResponse
import com.example.kotlinjetpack.model.RegisterResponse
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()

    var registerData: RegisterResponse? = null
    var addProfileData: AddProfileResponse? = null

    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun register(
        phone: String,
        password: String,
        reEnterPassword: String,
        name: String,
        dateOfBirth: String
    ) {
        resultLiveData.postValue(0)

        if (password != reEnterPassword) {
            errorMessage = "Please re-enter correct password"
            resultLiveData.postValue(2)
        } else if (name.isEmpty()) {
            errorMessage = "Name must not be empty"
            resultLiveData.postValue(2)
        } else if (dateOfBirth.isEmpty()) {
            errorMessage = "Birthday must not be empty"
            resultLiveData.postValue(2)
        } else {
            val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
            val registerBody = HashMap<String, String>()
            registerBody["phone"] = phone
            registerBody["password"] = password
            GlobalScope.launch {
                val registerResponse = quotesApi.register(registerBody)
                if (registerResponse.isSuccessful) {
                    registerData = registerResponse.body()

                    val addProfileBody = HashMap<String, String>()
                    addProfileBody["phone"] = phone
                    addProfileBody["name"] = name
                    addProfileBody["birthday"] = dateOfBirth
                    addProfileBody["avatar"] = ""
                    GlobalScope.launch {
                        val addProfileResponse = quotesApi.addProfile(addProfileBody)
                        if (addProfileResponse.isSuccessful) {
                            addProfileData = addProfileResponse.body()
                            println(addProfileData)

                            GlobalScope.launch {
                                val loginBody = HashMap<String, String>()
                                loginBody["phone"] = phone
                                loginBody["password"] = password
                                val loginResponse = quotesApi.login(loginBody)

                                if (loginResponse.isSuccessful) {
                                    val tempAccessToken = loginResponse.body()!!.token
                                    quotesApi.createContact(tempAccessToken!!)
                                    resultLiveData.postValue(1)
                                } else {
                                    val errorObject = JSONObject(loginResponse.errorBody()!!.string())
                                    errorMessage = errorObject.getString("message")
                                    resultLiveData.postValue(2)
                                }
                            }

                            resultLiveData.postValue(1)
                        } else {
                            val errorObject = JSONObject(addProfileResponse.errorBody()!!.string())
                            errorMessage = errorObject.getString("message")
                            resultLiveData.postValue(2)
                        }
                    }
                } else {
                    val errorObject = JSONObject(registerResponse.errorBody()!!.string())
                    errorMessage = errorObject.getString("message")
                    resultLiveData.postValue(2)
                }
            }
        }
    }
}
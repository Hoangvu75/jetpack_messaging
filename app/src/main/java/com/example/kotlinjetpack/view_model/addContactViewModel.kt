package com.example.kotlinjetpack.view_model

import CreateChatRequestBody
import CreateChatUser
import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.const.NAME
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.model.AddContactResponse
import com.example.kotlinjetpack.model.User
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddContactViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()

    var addContactData: AddContactResponse? = null

    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun addContact(phone: String) {
        resultLiveData.postValue(0)
        if (phone.length != 10) {
            errorMessage = "Please enter valid phone number"
            resultLiveData.postValue(2)
        } else {
            val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
            GlobalScope.launch {
                val addContactResponse = quotesApi.addContact(ACCESS_TOKEN, phone)
                if (addContactResponse.isSuccessful) {
                    addContactData = addContactResponse.body()

                    val createChatRequestBody = CreateChatRequestBody(
                        user_1 = CreateChatUser(
                            phone = PHONE,
                            name = NAME
                        ),
                        user_2 = CreateChatUser(
                            phone = addContactData!!.contact!!.contactList!!.last().phone,
                            name = addContactData!!.contact!!.contactList!!.last().name
                        )
                    )
                    quotesApi.createChat(createChatRequestBody)

                    resultLiveData.postValue(1)
                } else {
                    val errorObject = JSONObject(addContactResponse.errorBody()!!.string())
                    errorMessage = errorObject.getString("message")
                    resultLiveData.postValue(2)
                }
            }
        }
    }
}

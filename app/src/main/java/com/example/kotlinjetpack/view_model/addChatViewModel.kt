package com.example.kotlinjetpack.view_model

import AddChatRequestBody
import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddChatViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()

    @OptIn(DelicateCoroutinesApi::class)
    fun addChat(addChatRequestBody: AddChatRequestBody) {
        resultLiveData.postValue(0)

        val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
        GlobalScope.launch {
            val addChatResponse = quotesApi.addChat(addChatRequestBody)
            if (addChatResponse.isSuccessful) {
                resultLiveData.postValue(1)
                println("success")
            } else {
                val errorObject = JSONObject(addChatResponse.errorBody()!!.string())
                resultLiveData.postValue(2)
                println("failed")
            }

        }
    }
}

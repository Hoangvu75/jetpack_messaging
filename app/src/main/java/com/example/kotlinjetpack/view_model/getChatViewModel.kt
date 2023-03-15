package com.example.kotlinjetpack.view_model

import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.model.GetChatListResponse
import com.example.kotlinjetpack.model.GetChatResponse
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class GetChatViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()

    var getChatData: GetChatResponse? = null
    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun getChat(chatId: String) {
        resultLiveData.postValue(0)

        val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
        GlobalScope.launch {
            val response = quotesApi.getChat(chatId)
            if (response.isSuccessful) {
                getChatData = response.body()
                resultLiveData.postValue(1)
            } else {
                val errorObject = JSONObject(response.errorBody()!!.string())
                errorMessage = errorObject.getString("message")
                resultLiveData.postValue(2)
            }
        }
    }
}
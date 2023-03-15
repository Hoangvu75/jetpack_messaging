package com.example.kotlinjetpack.view_model

import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.model.GetChatListResponse
import com.example.kotlinjetpack.model.GetContactResponse
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class GetChatListViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()

    var getChatListData: GetChatListResponse? = null
    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun getChatList() {
        resultLiveData.postValue(0)

        val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
        GlobalScope.launch {
            val response = quotesApi.getChatList(ACCESS_TOKEN)
            if (response.isSuccessful) {
                getChatListData = response.body()
                resultLiveData.postValue(1)
            } else {
                val errorObject = JSONObject(response.errorBody()!!.string())
                errorMessage = errorObject.getString("message")
                resultLiveData.postValue(2)
            }
        }
    }
}
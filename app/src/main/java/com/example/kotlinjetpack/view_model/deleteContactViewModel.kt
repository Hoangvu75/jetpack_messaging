package com.example.kotlinjetpack.view_model

import RetrofitApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.model.DeleteContactResponse
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class DeleteContactViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<Int>()

    var deleteContactData: DeleteContactResponse? = null

    var errorMessage: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteContact(phone: String) {
        resultLiveData.postValue(0)

        val quotesApi = ApiService.getInstance().create(RetrofitApi::class.java)
        GlobalScope.launch {
            val deleteContactResponse = quotesApi.deleteContact(ACCESS_TOKEN, phone)
            if (deleteContactResponse.isSuccessful) {
                deleteContactData = deleteContactResponse.body()
                resultLiveData.postValue(1)
            } else {
                val errorObject = JSONObject(deleteContactResponse.errorBody()!!.string())
                errorMessage = errorObject.getString("message")
                resultLiveData.postValue(2)
            }
        }

    }
}
package com.example.kotlinjetpack.view_model

import RetrofitApi
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpack.const.ACCESS_TOKEN
import com.example.kotlinjetpack.const.NAME
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class JoinViewModel : ViewModel()  {

    var errorMessage: String? = null
    var roomId: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun createRoom(
        authToken: String,
        onLoading: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        retrofitApi: RetrofitApi = ApiService.getInstanceVideoSdk().create(RetrofitApi::class.java)
    ) {
        onLoading?.invoke()

        GlobalScope.launch {
            runCatching {
                val apiResponse = retrofitApi.createVideoCallRoom(authHeader = authToken)
                if (apiResponse.isSuccessful) {
                    roomId = apiResponse.body()!!.roomId
                } else {
                    val errorObject = JSONObject(apiResponse.errorBody()!!.string())
                    errorMessage = errorObject.getString("error")
                    onError?.invoke()
                }
            }.onSuccess {
                onSuccess?.invoke()
            }.onFailure {
                errorMessage = it.message
                onError?.invoke()
            }
        }
    }
}
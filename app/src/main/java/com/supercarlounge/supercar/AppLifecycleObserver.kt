package com.supercarlounge.supercar

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.TokenService
import com.supercarlounge.supercar.data.ResultTokenData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AppLifecycleObserver(var application: MainApplication) : LifecycleObserver {
    companion object {
        var isBackgroundState = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onForeground() {
        isBackgroundState = false
        var u_phone = application.u_phone
        var u_token = application.push_token
        getToken(u_phone,u_token)
        Log.d("포그라운드 테스트", "onForeground: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        isBackgroundState = true
    }
    fun getToken(u_phone: String,u_token:String) {
        Log.d("체크 번호", u_token)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(TokenService::class.java).getToken(u_phone,u_token
            )
            d!!.enqueue(object : retrofit2.Callback<ResultTokenData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultTokenData>,
                    response: Response<ResultTokenData>
                ) {

                    var data = response.body()
                    if (data != null) {
                        MainApplication.token = "Bearer "+response.body()?.token.toString()
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultTokenData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

}


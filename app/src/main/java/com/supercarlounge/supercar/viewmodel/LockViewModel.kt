package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PasswordService
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultPasswordSettiongData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class  LockViewModel() : ViewModel() {

    var titleText = MutableLiveData<String>("")
    var time = MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")

    var passwordHintText = MutableLiveData<String>("비밀번호를 입력해주세요.")
    var passwordType = MutableLiveData<Boolean>(true)
    var u_seq = MutableLiveData<String>("")
    var u_lock_screen = MutableLiveData<String>("")
    var passwordList = arrayListOf<String>()
    var passwordListSize = MutableLiveData<Int>(0)
    var passwordTrueFalse = MutableLiveData<Boolean>(false)
    var state = MutableLiveData<Int>(0)
    var passwordReset = MutableLiveData<Boolean>(false)
    var bubbleRick = MutableLiveData<Boolean>( false)
    fun addPassword(n: Int) {
        var ns = n.toString()

        if (passwordList.size < 4) {
            passwordList.add(ns)

            if (passwordList.size == 4) {

                var pass = ""

                pass =
                    concatenate(passwordList[0], passwordList[1], passwordList[2], passwordList[3])
                 if (!passwordTrueFalse.value!!) {

                    u_seq.value?.let {
                        setPassword(it, pass)

                    }
                } else {
                    u_seq.value?.let {

                        checkPassword(it, pass)
                    }
                }
            }

        } else {

        }
        passwordListSize.value = passwordList.size
        Log.d("addPassword", passwordList.toString())
        Log.d("passwordListSize", passwordListSize.toString())
    }


    fun concatenate(vararg string: String?): String {
        var result = ""
        string.forEach { result = result.plus(it) }
        return result
    }

    fun setPassword(u_seq: String, u_lock: String ) {
        if ( bubbleRick.value ==false) {

            bubbleRick.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d =
                    NetworkManager.serveradapter.create(PasswordService::class.java).setPassward(
                        u_seq,
                        u_lock,
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            Log.d("setPassword", data.message)
                            if (data.message.equals("잠금 비밀번호가 설정되었습니다")) {
                                passwordReset.value = true
                            }

                        }
                        bubbleRick.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", t.toString())
                        bubbleRick.value = false
                    }

                })

            }
        }

    }


    fun textInit(passwordTrueFalse: Boolean) {
        Log.d("textInit", passwordTrueFalse.toString())
        if (passwordTrueFalse) {
            mainText.value = "잠금 해제"
        } else {
            mainText.value = "잠금 설정"
        }


    }

    fun passwordSettingTrueFalse(u_seq: String) {

        if ( bubbleRick.value ==false) {

            bubbleRick.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(PasswordService::class.java)
                    .passwordSettingTrueFalse(
                        u_seq,
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultPasswordSettiongData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultPasswordSettiongData>,
                        response: Response<ResultPasswordSettiongData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            Log.d("setPassword", data.message)

                            passwordTrueFalse.value = data.message == "비밀번호 존재"
                            textInit(passwordTrueFalse.value!!)
                        }
                        bubbleRick.value = false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultPasswordSettiongData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                        bubbleRick.value = false
                    }

                })

            }
        }

    }


    fun checkPassword(u_seq: String, u_lock: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(PasswordService::class.java).checkPassword(
                u_seq,
                u_lock,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("checkPassword", data.message)

                        if (data.type == "fail"){
                            passwordHintText.value = "비밀번호가 틀립니다. 다시 입력해 주세요."
                            passwordType.value = false
                        }
                        if (data.message .equals("비밀번호 맞습니다")){
                            state.value = 1
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

    fun delete() {
        if (passwordList.size != 0) {
            passwordList.removeAt(passwordList.size - 1)
        }
        passwordListSize.value = passwordList.size
        passwordHintText.value = "비밀번호를 입력해주세요."
        passwordType.value = true
        Log.d("delete", passwordList.toString())

    }

    fun allDelete() {
        if (passwordList.size != 0) {
            passwordList.clear()
        }
        passwordListSize.value = passwordList.size
        passwordType.value = true
        passwordHintText.value = "비밀번호를 입력해주세요."
        Log.d("allDelete", passwordList.toString())
    }
}

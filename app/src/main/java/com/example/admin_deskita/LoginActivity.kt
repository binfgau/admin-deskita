package com.example.admin_deskita

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import kotlinx.android.synthetic.main.act_login.*
import okhttp3.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    val client = OkHttpClient()
    val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        btnLogin.setOnClickListener {
            try{
//code that may throw exception
                val login: RequestBody = FormBody.Builder()
                    .add("email",inputEmail.text.toString())
                    .add("password",inputPassword.text.toString())
                    .build()

                val request = Request.Builder()
                    .method("POST", login)
                    .url("https://deskita-ecommerce.herokuapp.com/api/v1/admin/login")
                    .build()

                val response = client.newCall(request).execute()
                val jsonData = response.body()?.string();
                val json: JSONObject = JSONObject(jsonData)
                val token=json.getString("token")
                val preferences= this.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                preferences?.edit()?.putString("TOKEN",token)?.apply()

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }catch(e: Exception){
//code that handles exception
                Log.d("errorrrrr", e.message.toString())
            }

        }
        btnForgotPassword.setOnClickListener{
            val intent = Intent(this,ForgotPasswordAct::class.java)
            startActivity(intent)
        }
    }
}
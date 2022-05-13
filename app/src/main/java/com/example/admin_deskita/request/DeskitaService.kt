package com.example.admin_deskita.request

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*



class DeskitaService : Fragment() {
    val client = OkHttpClient()
    val url="https://deskita-ecommerce.herokuapp.com/api/v1"


    fun getOrders(): JSONArray {
        val request = Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .url(url+"/all/orders")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        val json: JSONObject = JSONObject(jsonData)
        return json.getJSONArray("orders")

    }

    fun getOrderById(id:String,token:String):JSONObject{

        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .url(url+"/admin/order/${id}?userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun updateStatusOrder(id:String,orderStatus:String,token: String):JSONObject{

        val formBody: RequestBody = FormBody.Builder()
            .add("orderStatus", orderStatus)
            .build()
        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("PUT", formBody)
            .url(url+"/admin/order/${id}?userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun getUsers(type:String,token: String):JSONObject{

        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .url(url+"/mobile/admin/user?type=${type}&userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun getUserById(id:String,token: String):JSONObject{

        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .url(url+"/admin/user/${id}?userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun updateRole(id:String,token: String):JSONObject{

             val formBody: RequestBody = FormBody.Builder()
            .add("role","admin")
            .build()
        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("PUT",formBody)
            .url(url+"/admin/user/${id}?userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun getAnalyticsByDate(type: String?, dateStart:String, dateEnd:String,token: String):JSONObject{

        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("GET",null)
            .url(url+"/analytics-by-date?type=${type}&dateStart=${dateStart}&dateEnd=${dateEnd}&userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun sendEmail(email: String?):JSONObject{
        val formBody: RequestBody = FormBody.Builder()
            .add("email", email)
            .build()
        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("POST",formBody)
            .url(url+"/user/password/forgot")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }

    fun getProfile(token: String):JSONObject{
        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("GET",null)
            .url(url+"/me?userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }
    fun updateProfile(token: String, img: String, name: String, email: String, birth: String, phone: String, place: String):JSONObject{
        val data: Map<String, Any> = mapOf(
            "name" to name,
            "emailUser" to email,
            "dateOfBirth" to birth,
            "phoneNumber" to phone,
            "placeOfBirth" to place
        )
//        var requestBody =  data.toReq
        val formBody: RequestBody = FormBody.Builder()
            .add("avatarPR",img)
            .add("data", data.toString())
            .build()
        val request=Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json")
            .method("PUT",formBody)
            .url(url+"/user/update-profile?userToken=${token}")
            .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }
}
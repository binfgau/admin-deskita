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
        var params = JSONObject().put("userToken", token)
        var data = JSONObject()
        data.put("name", name)
        data.put("placeOfBirth", place)
        data.put("dateOfBirth", birth)
        data.put("phoneNumber", phone)
        data.put("emailUser", email)
        data.put("role", "admin")
        var json = JSONObject()
        json.put("params", params)
        json.put("data", data)
        json.put("avatarPr",img)

        val url = "https://deskita-ecommerce.herokuapp.com/api/v1/user/update-profile"
        val jsonType = MediaType.parse("application/json; charset=utf-8")
        val reqBody = RequestBody.create(jsonType, json.toString())
        val request = Request.Builder().put(reqBody).url(url).build()
        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val jsonData = response.body()?.string();
        return JSONObject(jsonData)
    }
}
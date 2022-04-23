package com.example.admin_deskita.request

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject


class DeskitaService {
    val client = OkHttpClient()
    val token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyMTdiNTYwNjA1YzAzMjg0YzRlZTc0NiIsImlhdCI6MTY1MDYxNDYxMywiZXhwIjoxNjUxMjE5NDEzfQ.nGFBO-zqOf7rEUt4hkuSdlEGTjurVBYNuEMwVQpv7FI";
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

    fun getOrderById(id:String):JSONObject{
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

    fun updateStatusOrder(id:String,orderStatus:String):JSONObject{
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

    fun getUsers(type:String):JSONObject{
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

    fun getUserById(id:String):JSONObject{
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

    fun updateRole(id:String):JSONObject{
        val json = """
"data":{
    "role":"admin"
   
}
""".trimIndent()


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
}
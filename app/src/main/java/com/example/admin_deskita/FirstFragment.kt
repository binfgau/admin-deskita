package com.example.admin_deskita

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.admin_deskita.databinding.FragmentFirstBinding
import okhttp3.*
import org.json.JSONObject


class objLogin{
    var email=""
    var password=""
}
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    val client = OkHttpClient()



    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")!!

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        binding.btnLogin.setOnClickListener {
            try{
//code that may throw exception
                val login:RequestBody= FormBody.Builder()
                    .add("email",binding.inputEmail.text.toString())
                    .add("password",binding.inputPassword.text.toString())
                    .build()

                val request = Request.Builder()
                    .method("POST", login)
                    .url("https://deskita-ecommerce.herokuapp.com/api/v1/user/login")
                    .build()

                val response = client.newCall(request).execute()
                val jsonData = response.body()?.string();
                val json:JSONObject= JSONObject(jsonData)
                val token=json.getString("token")
                val preferences= activity?.getSharedPreferences("admin_deskita",Context.MODE_PRIVATE)
                preferences?.edit()?.putString("TOKEN",token)?.apply()
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }catch(e: Exception){
//code that handles exception
               Log.d("testcoi",e.stackTraceToString())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
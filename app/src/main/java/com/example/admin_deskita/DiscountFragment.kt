package com.example.admin_deskita

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin_deskita.adapter.DiscountAdapter
import com.example.admin_deskita.databinding.FragmentDiscountBinding
import com.example.admin_deskita.databinding.FragmentProductBinding
import com.example.admin_deskita.entity.Discount
import com.example.admin_deskita.entity.ListDiscountContainer
import com.example.admin_deskita.entity.ListProductContainer
import com.example.admin_deskita.entity.Product
import com.example.admin_deskita.request.DeskitaService
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_product.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val client = OkHttpClient()

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!
    val ACT_UPDATE = 1
    val ACT_ADD = 2
    lateinit var lstDiscount: ArrayList<Discount>
    //lateinit var lstProduct: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        getAllDiscount()
    }

    private fun getAllDiscount() {
        val prefs = activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
       // val token = prefs?.getString("TOKEN", null)!!
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyMTdiNTYwNjA1YzAzMjg0YzRlZTc0NiIsImlhdCI6MTY1MjUxMDUyNywiZXhwIjoxNjUzMTE1MzI3fQ.-I1V7B56LzVwnql5l8dfXW8ICtTD1MGgVWbVIZxjQmw"

        try {
            val request = Request.Builder()
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .url("https://deskita-ecommerce.herokuapp.com/api/v1/admin/discounts?keyword=&userToken=${token}")
                .build()
            val response = client.newCall(request).execute()
            val jsonData = response.body()?.string();
            val container = Gson().fromJson(jsonData, ListDiscountContainer::class.java)
           // lstDiscount = container.discounts
            binding.lvDiscount.adapter = DiscountAdapter(requireContext(), container.discounts)

        }   catch (e: Exception) {
        //code that handles exception
        Log.d("testcoi", e.stackTraceToString())
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiscountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
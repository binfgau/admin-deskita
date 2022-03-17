package com.example.admin_deskita

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.admin_deskita.databinding.FragmentProductBinding
import com.example.admin_deskita.support.SelectRecipeDetailFragment
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class Token{
    public var userToken:String=""


}

class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val client = OkHttpClient()
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private var productIds=ArrayList<String>()
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

        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            var products: JSONArray?=null

            val request = Request.Builder()
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .url("https://deskita-ecommerce.herokuapp.com/api/v1/all-products")
                .build()
            val response = client.newCall(request).execute()
            val jsonData = response.body()?.string();
            val json: JSONObject = JSONObject(jsonData)
            products= json.getJSONArray("products")
            products?.let { loadItems(it) }


        }catch(e: Exception){
//code that handles exception
            Log.d("testcoi",e.stackTraceToString())
        }

        binding.products.onItemClickListener=  AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->

            try {
                print(position)

                val id=productIds.get(position)
                val preferences= activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                preferences?.edit()?.putString("product_id",id)?.apply()






                findNavController().navigate(R.id.to_product_detail_fragment)


            }catch (e: Exception){
                print(e.stackTraceToString())
                Log.d("testcoi",e.stackTraceToString())

            }

        }




    }


    fun loadItems(products:JSONArray){
        val languages= arrayListOf<String>()
        val quantities= arrayListOf<String>()
        val urlImages= arrayListOf<String>()
        for(i in 0 until products.length()){
            val product =products.getJSONObject(i)
            productIds.add(product.getString("_id"))
            languages.add(product.getString("name"))
            quantities.add(Integer.toString(product.getInt("stock")))
            val urlImage=product.getJSONArray("images").getJSONObject(0).getString("url")
            urlImages.add(urlImage)
        }


        val itemAdapter= activity?.let { ProductsAdapter(context = it,languages,quantities,urlImages) }
        binding.products.adapter=itemAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
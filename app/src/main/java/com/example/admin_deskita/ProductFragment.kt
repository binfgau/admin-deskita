package com.example.admin_deskita

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.example.admin_deskita.entity.ListProductContainer
import com.example.admin_deskita.entity.Product
import com.example.admin_deskita.entity.ProductContainer
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_product.*
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

class Token {
    public var userToken: String = ""


}

class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val client = OkHttpClient()
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    val ACT_UPDATE = 1
    val ACT_ADD = 2
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

        getAllProduct()

        binding.lvProducts.setOnItemClickListener { adapterView, view, i, l ->
            try {
                val clickProduct = adapterView.getItemAtPosition(i) as Product
                val id = clickProduct._id

                val intent = Intent(requireContext(),AddUpdateProductAct::class.java)
                intent.putExtra("product",clickProduct)
                startActivityForResult(intent,ACT_UPDATE)
            } catch (e: Exception) {
                print(e.stackTraceToString())
                Log.d("testcoi", e.stackTraceToString())
            }
        }

        binding.imgAddProduct.setOnClickListener{
            val intent = Intent(requireContext(),AddUpdateProductAct::class.java)
            startActivityForResult(intent,ACT_ADD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK && requestCode==ACT_UPDATE){
            var product = data?.getSerializableExtra("productUpdate") as Product
            (lvProducts.adapter as ProductsAdapter).updateProduct(product)
        }
    }

    private fun getAllProduct() {
        try {
            val request = Request.Builder()
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .url("https://deskita-ecommerce.herokuapp.com/api/v1/all-products")
                .build()
            val response = client.newCall(request).execute()
            val jsonData = response.body()?.string();
            val container = Gson().fromJson(jsonData, ListProductContainer::class.java)
            binding.lvProducts.adapter = ProductsAdapter(requireContext(),container.products)
        } catch (e: Exception) {
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
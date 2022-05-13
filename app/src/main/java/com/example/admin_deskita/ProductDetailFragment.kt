package com.example.admin_deskita

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.admin_deskita.databinding.FragmentProductDetailBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    val client = OkHttpClient()
    lateinit var imageUri:Uri
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

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //classiify
        val classifies = arrayOf("Women", "Men", "Kid");
        var adapter= context?.let { ArrayAdapter<Any?>(it,
            android.R.layout.simple_spinner_dropdown_item,classifies) }

        binding.classify.adapter=adapter
        binding.description.setHorizontallyScrolling(false)
        binding.description.maxLines = 20
        //category
        val categories= arrayOf("jacketsCoats","hoodiesSweatshirts","cardiganJumpers","tshirtTanks",
            "shoes","shirts","basics","blazersSuits","shorts","trousers","jeans","swimwear",
            "underwear","socks")

        adapter= context?.let { ArrayAdapter<Any?>(it,
            android.R.layout.simple_spinner_dropdown_item,categories) }
        binding.category.adapter=adapter

        //edit product
        val prefs=activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val productId =    prefs?.getString("product_id",null)

        if(productId!=null){
            val request = Request.Builder()
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .url("https://deskita-ecommerce.herokuapp.com/api/v1/product/"+productId)
                .build()
            val response = client.newCall(request).execute()
            val jsonData = response.body()?.string();
            val res = JSONObject(jsonData)
            binding.name.setText(res?.getJSONObject("product")?.getString("name"))
            binding.price.setText(res?.getJSONObject("product")?.getDouble("price").toString())
            binding.description.setText(res?.getJSONObject("product")?.getString("description"))
            binding.stock.setText(res?.getJSONObject("product")?.getInt("stock").toString())
            setSpinText(binding.classify,res?.getJSONObject("product").getString("classify"))
            setSpinText(binding.category,res?.getJSONObject("product").getString("category"))


            val url= URL(res?.getJSONObject("product")?.getJSONArray("images")?.
                getJSONObject(0)?.getString("url"))
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            binding.imgProductDetail.setImageBitmap(bmp)
        }
        //new product
        else{
            val url= URL("https://www.chanchao.com.tw/vietnamwood/images/default.jpg")
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            binding.imgProductDetail.setImageBitmap(bmp)
        }

        //select image
        binding.selectImage.setOnClickListener{
            selectImage()
        }

        //save
        binding.save.setOnClickListener{
            save()
        }

    }

    fun save(){
        val inputStream: InputStream? =context?.contentResolver?.openInputStream(imageUri)
        val selectedImage = BitmapFactory.decodeStream(inputStream)
        val encodedImage="data:"+context?.contentResolver?.getType(imageUri)+";base64,"+BitMapToString(selectedImage)

    }

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun selectImage(){
        val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100&&resultCode== Activity.RESULT_OK){

            try {
                imageUri = data!!.data!!
                binding.imgProductDetail.setImageURI(imageUri)

            }catch (e:Exception){
                Log.d("error_error",e.stackTraceToString())
            }

        }
    }

    fun setSpinText(spin: Spinner, text: String?) {
        for (i in 0 until spin.adapter.count) {
            if (spin.adapter.getItem(i).toString().contains(text!!)) {
                spin.setSelection(i)
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
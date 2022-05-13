package com.example.admin_deskita

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.admin_deskita.entity.Product
import com.example.admin_deskita.entity.ProductContainer
import com.google.gson.Gson
import kotlinx.android.synthetic.main.act_add_update_product.*
import kotlinx.android.synthetic.main.act_add_update_product.category
import kotlinx.android.synthetic.main.act_add_update_product.classify
import kotlinx.android.synthetic.main.act_add_update_product.description
import kotlinx.android.synthetic.main.act_add_update_product.editName
import kotlinx.android.synthetic.main.act_add_update_product.editPrice
import kotlinx.android.synthetic.main.act_add_update_product.imgProductDetail
import kotlinx.android.synthetic.main.act_add_update_product.save
import kotlinx.android.synthetic.main.act_add_update_product.stock
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URL

class AddUpdateProductAct : AppCompatActivity() {

    val client = OkHttpClient()
    lateinit var imageUri: Uri
    lateinit var product: Product
    var action = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_add_update_product)
        val classifies = arrayOf("Women", "Men", "Kid");
        var adapter= this?.let { ArrayAdapter<Any?>(it,
            android.R.layout.simple_spinner_dropdown_item,classifies) }

        classify.adapter=adapter
        description.setHorizontallyScrolling(false)
        description.maxLines = 20
        //category
        val categories= arrayOf("jacketsCoats","hoodiesSweatshirts","cardiganJumpers","tshirtTanks",
            "shoes","shirts","basics","blazersSuits","shorts","trousers","jeans","swimwear",
            "underwear","socks")

        adapter= this?.let { ArrayAdapter<Any?>(it,
            android.R.layout.simple_spinner_dropdown_item,categories) }
        category.adapter=adapter


        product = intent.getSerializableExtra("product") as Product

        if(product != null){
            val request = Request.Builder()
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .url("https://deskita-ecommerce.herokuapp.com/api/v1/product/"+product._id)
                .build()
            val response = client.newCall(request).execute()
            val jsonData = response.body()?.string();
            val res = JSONObject(jsonData)
            editName.setText(res?.getJSONObject("product")?.getString("name"))
            editPrice.setText(res?.getJSONObject("product")?.getDouble("price").toString())
            description.setText(res?.getJSONObject("product")?.getString("description"))
            stock.setText(res?.getJSONObject("product")?.getInt("stock").toString())
            setSpinText(classify,res?.getJSONObject("product").getString("classify"))
            setSpinText(category,res?.getJSONObject("product").getString("category"))


            val url= URL(res?.getJSONObject("product")?.getJSONArray("images")?.
            getJSONObject(0)?.getString("url"))
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            imgProductDetail.setImageBitmap(bmp)
        }
        //new product
        else{
            action =1
            val url= URL("https://www.chanchao.com.tw/vietnamwood/images/default.jpg")
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            imgProductDetail.setImageBitmap(bmp)
        }

        //select image
        select_image.setOnClickListener{
            selectImage()
        }

        //save
        save.setOnClickListener{
            save()
        }
    }

    fun save(){
        val bitmap = (imgProductDetail.getDrawable() as BitmapDrawable).toBitmap()
        val encodedImage="data:image/jpeg;base64,"+BitMapToString(bitmap)

        val prefs= getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val token = prefs?.getString("TOKEN",null)!!

        val url = "https://deskita-ecommerce.herokuapp.com/api/v1/admin/product/"+product._id

        val params = JSONObject().put("userToken",token)
        val data = JSONObject()

        data.put("price",editPrice.text)
        data.put("name",editName.text)
        data.put("description",description.text)
        data.put("classify",classify.selectedItem.toString())
        data.put("category",category.selectedItem.toString())
        data.put("stock",Integer.parseInt(stock.text.toString()))
        data.put("image",encodedImage)

        val JSON = JSONObject()
        JSON.put("params",params)
        JSON.put("data",data)
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val reqBody = RequestBody.create(mediaType,JSON.toString())
        val request = Request.Builder().put(reqBody).url(url).build()
        val client = OkHttpClient()

        val response = client.newCall(request).execute()
        val resJson = JSONObject(response.body()?.string())
        if (resJson.has("success")){
            Toast.makeText(this,"Change save!", Toast.LENGTH_SHORT)

            val productContainer:ProductContainer = Gson().fromJson(resJson.toString(),ProductContainer::class.java)
            val data = Intent()
            data.putExtra("productUpdate",productContainer.product)
            setResult(RESULT_OK,data)
            finish()
        }else{
            Toast.makeText(this,"Failure!", Toast.LENGTH_SHORT)
        }
    }

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun selectImage(){
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100&&resultCode== RESULT_OK){

            try {
                imageUri = data!!.data!!
                imgProductDetail.setImageURI(imageUri)

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

    fun goBack(view: View) {
        onBackPressed()
    }
}
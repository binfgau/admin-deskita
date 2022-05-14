package com.example.admin_deskita

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.admin_deskita.entity.Product
import com.squareup.picasso.Picasso
import java.net.URL

class ProductViewHolder(val view:View){
    val imgProduct:ImageView
    val txtProductName:TextView
    val txtProductPrice:TextView
    init {
        imgProduct = view.findViewById(R.id.imgProduct)
        txtProductName = view.findViewById(R.id.txtProductName)
        txtProductPrice = view.findViewById(R.id.txtProductPrice)
    }
}

class ProductsAdapter(val context:Context,var lstProduct: ArrayList<Product>) :BaseAdapter(){
    override fun getCount(): Int {
        return lstProduct.size
    }

    override fun getItem(p0: Int): Product {
        return lstProduct.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, convertView: View?, p2: ViewGroup?): View? {
        var view:View?
        var viewHolder:ProductViewHolder?
        if (convertView ==null){
            view = LayoutInflater.from(context).inflate(R.layout.list_product,null)
            viewHolder = ProductViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ProductViewHolder
        }
        viewHolder.txtProductName.text = lstProduct.get(p0).name
        viewHolder.txtProductPrice.text = lstProduct.get(p0).price.toString()
        Picasso.get().load(lstProduct.get(p0).images.get(0).url).into(viewHolder.imgProduct)

        return view
    }

    fun updateProduct(updateProduct: Product){
        for (p in lstProduct){
            if (p._id.compareTo(updateProduct._id)==0){
                val i = lstProduct.indexOf(p)
                lstProduct.remove(p)
                lstProduct.add(i,updateProduct)
                break
            }
        }
        notifyDataSetChanged()
    }

    fun addNewProduct(newProduct: Product){
        lstProduct.add(newProduct)
        notifyDataSetChanged()
    }
}
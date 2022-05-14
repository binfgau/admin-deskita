package com.example.admin_deskita.adapter

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
import com.example.admin_deskita.ProductViewHolder
import com.example.admin_deskita.R
import com.example.admin_deskita.entity.Discount
import com.example.admin_deskita.entity.Product
import com.squareup.picasso.Picasso
import java.net.URL

class DiscountViewHolder(val view: View) {
    val txtDiscountName: TextView
    val txtValidDate:TextView
    val txtDiscountValue:TextView
    val txtQuantity:TextView
    val txtCategory:TextView

    init {
        txtDiscountName = view.findViewById(R.id.txtDiscountName)
        txtValidDate = view.findViewById(R.id.txtDiscountDate)
        txtDiscountValue = view.findViewById(R.id.txtDiscountValue)
        txtQuantity = view.findViewById(R.id.txtDiscountQuantity)
        txtCategory = view.findViewById(R.id.lbDiscountCategory)

    }
}
class DiscountAdapter(val context:Context,var lstDiscount: ArrayList<Discount>) :BaseAdapter(){
    override fun getCount(): Int {
        return lstDiscount.size
    }

    override fun getItem(p0: Int): Discount {
        return lstDiscount.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, convertView: View?, p2: ViewGroup?): View? {
        var view:View?
        var viewHolder: DiscountViewHolder?
        if (convertView ==null){
            view = LayoutInflater.from(context).inflate(R.layout.list_discount,null)
            viewHolder = DiscountViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as DiscountViewHolder
        }
        viewHolder.txtDiscountName.text = lstDiscount.get(p0).name
        viewHolder.txtValidDate.text = lstDiscount.get(p0).validDate
        viewHolder.txtDiscountValue.text = lstDiscount.get(p0).value.toString()
        viewHolder.txtQuantity.text = lstDiscount.get(p0).quantity.toString()
        viewHolder.txtCategory.text = lstDiscount.get(p0).categoryProduct
        return view
    }


//    fun addNewDiscount(newDiscount: Discount){
//        lstProduct.add(newProduct)
//        notifyDataSetChanged()
//    }
}
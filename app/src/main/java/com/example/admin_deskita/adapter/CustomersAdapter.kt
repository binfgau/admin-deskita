package com.example.admin_deskita.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.admin_deskita.R
import com.example.admin_deskita.entity.Customer
import java.net.URL

class CustomersAdapter(private val context: Context,
                       private val idLayout: Int,
                       private val listCustomer: List<Customer>?):BaseAdapter() {
    private var positionSelect = -1
    override fun getCount(): Int {
        return if (listCustomer!!.size != 0 && !listCustomer.isEmpty()) {
            listCustomer.size
        } else 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(idLayout, parent, false)
        }
        val customer= listCustomer?.get(position)

        val imageUrl = URL(customer!!.image)
        val image= convertView?.findViewById(R.id.customer_user_image) as ImageView
        val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
        image.setImageBitmap(bitmap)
        image.requestLayout();
        image.layoutParams.width=400;
        image.layoutParams.height=400;

        val name=convertView.findViewById(R.id.customer_user_info) as TextView
        name.setText("Tên khách hàng: "+customer.name+"\n"
        +"Email: "+customer.email+"\n"
        +"Số điện thoại: "+customer.phoneNum)



        return convertView
    }
}
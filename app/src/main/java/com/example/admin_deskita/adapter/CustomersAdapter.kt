package com.example.admin_deskita.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.admin_deskita.R
import com.example.admin_deskita.entity.Customer
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CustomerViewHolder(val view:View){
    var imgCustomerAvatar : CircleImageView
    var txtFullname : TextView
    var txtEmail : TextView
    var txtPhoneNumber : TextView
    init {
        imgCustomerAvatar = view.findViewById(R.id.imgCustomerAvatar)
        txtFullname = view.findViewById(R.id.txtFullName)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber)
    }
}

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
        var view:View
        var viewHolder:CustomerViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(idLayout, parent, false)
            viewHolder = CustomerViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as CustomerViewHolder
        }
        val customer= listCustomer?.get(position)

        Picasso.get().load(customer?.image).placeholder(R.drawable.user_avatar).into(viewHolder.imgCustomerAvatar)
        viewHolder.txtFullname.text = customer?.name
        viewHolder.txtEmail.text = customer?.email
        viewHolder.txtPhoneNumber.text = customer?.phoneNum

        return view
    }
}
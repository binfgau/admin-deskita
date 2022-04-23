package com.example.admin_deskita.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.admin_deskita.entity.OrderItem
import org.intellij.lang.annotations.Language
import com.example.admin_deskita.R
import java.net.URL

class OrderItemsAdapter(
    private val context: Context,
    private val idLayout: Int,
    private val listOrderItem: List<OrderItem>?
) :
    BaseAdapter() {
    private var positionSelect = -1
    override fun getCount(): Int {
        return if (listOrderItem!!.size != 0 && !listOrderItem.isEmpty()) {
            listOrderItem.size
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
        val orderItem= listOrderItem?.get(position)

        val imageUrl = URL(orderItem!!.image)
        val image= convertView?.findViewById(R.id.order_items_image) as ImageView
        val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
        image.setImageBitmap(bitmap)
        image.requestLayout();
        image.layoutParams.width=400;
        image.layoutParams.height=400;

        val name=convertView.findViewById(R.id.order_items_name) as TextView
        name.setText(orderItem.name)
        val price=convertView.findViewById(R.id.order_items_price) as TextView
        price.setText(""+orderItem.price)
        val quantity=convertView.findViewById(R.id.order_items_quantity) as TextView
        quantity.setText(""+orderItem.quantity)



        return convertView
    }
}



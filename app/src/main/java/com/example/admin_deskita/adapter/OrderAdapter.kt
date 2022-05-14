package com.example.admin_deskita.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.admin_deskita.R
import com.example.admin_deskita.entity.Order
import com.example.admin_deskita.entity.OrderStatus
import com.example.admin_deskita.orderstatusfrags.ConfirmedFragment
import com.example.admin_deskita.orderstatusfrags.DeliveredFragment
import com.google.android.material.internal.ContextUtils.getActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

class OrderViewHolder(val view:View){
    var lstOrderItem :ListView
    var txtTotalOrder: TextView
    var txtOrderId: TextView
    var bttAction: Button
    var txtOrderDate: TextView
    var bttDeleteOrder:TextView
    init {
        lstOrderItem = view.findViewById(R.id.lstOrderItems)
        txtTotalOrder = view.findViewById(R.id.txtTotalOrder)
        txtOrderId = view.findViewById(R.id.txtOrderId)
        bttAction = view.findViewById(R.id.bttAction)
        txtOrderDate = view.findViewById(R.id.txtOrderDate)
        bttDeleteOrder = view.findViewById(R.id.bttDeleteOrder)
    }
}
class OrderAdapter(var context: Context,var lstOrders:ArrayList<Order>, var nextFragment: Fragment?):BaseAdapter() {

    val dateFormat : SimpleDateFormat = SimpleDateFormat("dd/MM/yy HH:mm a")
    override fun getCount(): Int {
        return lstOrders.size
    }

    override fun getItem(position: Int): Any {
        return lstOrders.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View
        var viewHolder: OrderViewHolder
        if (convertView==null){
            val layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.order_row,null)
            viewHolder = OrderViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as OrderViewHolder
        }
        viewHolder.txtOrderId.text = lstOrders.get(position)._id
        viewHolder.txtTotalOrder.text = lstOrders.get(position).totalPrice.toString()+" $"
        viewHolder.lstOrderItem.adapter = OrderItemAdapter(context,lstOrders.get(position).orderItems, lstOrders.get(position).orderStatus)
        viewHolder.txtOrderDate.text = dateFormat.format(lstOrders.get(position).createAt)
        justifyListViewHeightBasedOnChildren(viewHolder.lstOrderItem)
        setViewListener(viewHolder,lstOrders.get(position))
        return view
    }

    fun setViewListener(viewHolder: OrderViewHolder,order:Order) {
        var bttAction = viewHolder.bttAction
        var bttDeleteOrder = viewHolder.bttDeleteOrder
        when(order.orderStatus){
            OrderStatus.Processing ->{
                bttDeleteOrder.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("Hủy đơn hàng")
                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
                        .setPositiveButton("Hãy hủy đơn hàng", { dialog, which ->
                            deleteOrder(order)
                        }).setNegativeButton("Bỏ",null)
                        .show()
                }
                bttAction.setText("Confirm")
                bttAction.setOnClickListener {
                    changeStatus(order,OrderStatus.Confirmed)
                }
            }
            OrderStatus.Confirmed->{
                bttDeleteOrder.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("Hủy đơn hàng")
                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
                        .setPositiveButton("Hãy hủy đơn hàng", { dialog, which ->
                            deleteOrder(order)
                        }).setNegativeButton("Bỏ",null)
                        .show()
                }
                bttAction.setText("Deliver")
                bttAction.setOnClickListener {
                    changeStatus(order,OrderStatus.Delivered)
                }
            }
            OrderStatus.Delivered->{
                bttAction.visibility = View.GONE
                bttDeleteOrder.visibility = View.GONE
            }
            OrderStatus.Complete->{
                bttAction.visibility=View.GONE
                bttDeleteOrder.visibility = View.GONE
            }
        }
    }

    fun changeStatus(order: Order,status: OrderStatus){
        val url = "https://deskita-ecommerce.herokuapp.com/api/v1/admin/order/"+order._id
        val sharePref = context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        val prefs = context.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val userToken = prefs?.getString("TOKEN", null)!!

        val params = JSONObject().put("userToken",userToken)
        var jsonSend = JSONObject()
        jsonSend.put("params",params)
        jsonSend.put("orderStatus",status.name)

        val jsonType = MediaType.parse("application/json; charset=utf-8")
        val reqBody = RequestBody.create(jsonType,jsonSend.toString())
        val request = Request.Builder().put(reqBody).url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("RestrictedApi")
            override fun onFailure(call: Call, e: IOException) {
                getActivity(context)?.runOnUiThread(){
                    Toast.makeText(
                        context,
                        "Lỗi kết nối internet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call, response: Response) {
                val resBody = JSONObject(response.body()?.string())
                if (resBody.has("success")) {
                    getActivity(context)?.runOnUiThread {
                        Toast.makeText(
                            context,
                            "Đã đổi trạng thái đơn hàng",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (nextFragment is ConfirmedFragment){
                            (nextFragment as ConfirmedFragment).addConfirmedItem(order)
                        }else if (nextFragment is DeliveredFragment){
                            (nextFragment as DeliveredFragment).addDeliveredItem(order)
                        }
                        deleteOrderItem(order)
                    }
                }
            }
        })
    }

    fun deleteOrder(order: Order){
        val url = "https://deskita-ecommerce.herokuapp.com/api/v1/order/"+order._id
        val prefs = context.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val userToken = prefs?.getString("TOKEN", null)!!
        val params = JSONObject().put("userToken",userToken)
        var jsonSend = JSONObject()
        jsonSend.put("params",params)

        val jsonType =  MediaType.parse("application/json; charset=utf-8")
        val reqBody = RequestBody.create(jsonType,jsonSend.toString())
        val request = Request.Builder().delete(reqBody).url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("RestrictedApi")
            override fun onFailure(call: Call, e: IOException) {
                getActivity(context)?.runOnUiThread(){
                    Toast.makeText(
                        context,
                        "Lỗi kết nối internet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call, response: Response) {
                val resBody = JSONObject(response.body()?.string())
                if (resBody.has("success")) {
                    getActivity(context)?.runOnUiThread {
                        Toast.makeText(
                            context,
                            "Đã hủy đơn hàng",
                            Toast.LENGTH_SHORT
                        ).show()
                        deleteOrderItem(order)
                    }
                }
            }
        })
    }

    fun justifyListViewHeightBasedOnChildren(listView: ListView) {
        val adapter: Adapter = listView.getAdapter() ?: return
        val vg: ViewGroup = listView
        var totalHeight = 0
        for (i in 0 until adapter.getCount()) {
            val listItem: View = adapter.getView(i, null, vg)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val par: ViewGroup.LayoutParams = listView.getLayoutParams()
        par.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1)
        listView.setLayoutParams(par)
        listView.requestLayout()
    }

    fun deleteOrderItem(order:Order){
        lstOrders.remove(order)
        notifyDataSetChanged()
    }

    fun addOrderItem(order:Order){
        lstOrders.add(order)
        notifyDataSetChanged()
    }
}
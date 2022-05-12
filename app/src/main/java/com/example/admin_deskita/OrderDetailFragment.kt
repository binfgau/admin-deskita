package com.example.admin_deskita

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.admin_deskita.adapter.OrderItemsAdapter
import com.example.admin_deskita.entity.OrderItem
import com.example.admin_deskita.request.DeskitaService
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val client=DeskitaService()
    var order: JSONObject? =null
    var customer:JSONObject?=null
    var shippingInfo:JSONObject?=null
    var orderItems:JSONArray?=null
    var total:JSONObject?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_order_detail,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get order
        val prefs=activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val token = prefs?.getString("TOKEN",null)!!
        try {
            val orderId =    prefs?.getString("order_id",null)

            order= orderId?.let { client.getOrderById(it,token) }
            customer= order?.getJSONObject("user");
            shippingInfo= order?.getJSONObject("order")?.getJSONObject("shippingInfo")
            orderItems=order?.getJSONObject("order")?.getJSONArray("orderItems")
            total=order?.getJSONObject("order")

        }catch (e:Exception){

        }

        //set avatar
        val imageUrl = URL(customer?.getJSONObject("avatar")?.getString("url"))
        val i: ImageView = view.findViewById(R.id.customer_avatar) as ImageView
        val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
        i.setImageBitmap(bitmap)
        i.requestLayout();
        i.layoutParams.height=400;

        //set customer
        val name=view.findViewById(R.id.customer_name) as TextView
        name.text="Họ và tên: "+customer?.getString("name")

        val email=view.findViewById(R.id.customer_email) as TextView
        email.text="Email: "+ customer?.getString("emailUser")

        //set shipping info
        val address=view.findViewById(R.id.shipping_info_address) as TextView
        address.text="Địa chỉ: "+shippingInfo?.getString("address")

        val city=view.findViewById(R.id.shipping_info_city) as TextView
        city.text="Thành phố: "+shippingInfo?.getString("city")

        val postalCode=view.findViewById(R.id.shipping_info_code) as TextView
        postalCode.text="Mã bưu điện: "+shippingInfo?.getString("postalCode")

        val country=view.findViewById(R.id.shipping_info_country) as TextView
        country.text="Quốc gia: "+shippingInfo?.getString("country")

        val phoneNo=view.findViewById(R.id.shipping_info_phone) as TextView
        phoneNo.text="Số điện thoại: "+shippingInfo?.getString("phoneNo")
        //set orderItems
        var orderItemsAl=ArrayList<OrderItem>()
        for(i in 0 until orderItems!!.length()){
            val orderItem = orderItems!!.getJSONObject(i)

            val orderItemTemp=OrderItem(orderItem.getString("image"),orderItem.getString("name"),orderItem.getDouble("price"),orderItem.getInt("quantity"))
            orderItemsAl.add(orderItemTemp)
        }

        val lvOrderItems=view.findViewById(R.id.order_items) as ListView
        val adapter= context?.let { OrderItemsAdapter(it,R.layout.row_order_items,orderItemsAl) }
        lvOrderItems.adapter=adapter

        //set total
        val paymentMethod=view.findViewById(R.id.total_payment_method) as TextView
        paymentMethod.text="Phương thức thanh toán: "+total!!.getString("paymentMethod")

        try{
                val paidAt=view.findViewById(R.id.total_paid_at) as TextView
                paidAt.text="Ngày thanh toán: "+total!!.getString("paidAt")

        }catch(e:Exception){

        }
        try{
                val discount=view.findViewById(R.id.total_discount_code) as TextView
                discount.text="Code Mã giảm giá: "+total!!.getJSONObject("discount").getString("name")
        }catch (e:Exception){

        }

        val itemsPrice=view.findViewById(R.id.total_items_price) as TextView
        itemsPrice.text="Tổng sản phẩm: "+total!!.getDouble("itemsPrice")

        val taxPrice=view.findViewById(R.id.total_tax_price) as TextView
        taxPrice.text="Giá thuế: "+total!!.getDouble("taxPrice")

        val shippingPrice=view.findViewById(R.id.total_shipping_price) as TextView
        shippingPrice.text="Giá shipping: "+total!!.getDouble("shippingPrice")

        val totalPrice=view.findViewById(R.id.total_total_price) as TextView
        totalPrice.text="Tổng đơn hàng: "+total!!.getDouble("totalPrice")

        val orderStatus=view.findViewById(R.id.total_order_status) as TextView
        orderStatus.text="Trạng thái đơn hàng: "+total!!.getString("orderStatus")

        val createAt=view.findViewById(R.id.total_create_at) as TextView
        createAt.text="Ngày tạo đơn: "+total!!.getString("createAt")



        //update status
        val btnUpdateStatus=view.findViewById(R.id.btn_update_status) as Button
        btnUpdateStatus.setOnClickListener{
            when(btnUpdateStatus.text){
                "Confirmed"->{client.updateStatusOrder( ""+prefs?.getString("order_id",null),"Confirmed",token)
                    btnUpdateStatus.text="Delivered"
                    orderStatus.text="Trạng thái đơn hàng: Confirmed"
                }
                "Delivered"->{client.updateStatusOrder(""+prefs?.getString("order_id",null),"Delivered",token)
                    btnUpdateStatus.visibility=+View.GONE
                    orderStatus.text="Trạng thái đơn hàng: Delivered"
                }
            }

        }
        when(total!!.getString("orderStatus")){
            "Processing"->btnUpdateStatus.text="Confirmed"
            "Confirmed"->btnUpdateStatus.text="Delivered"
            "Delivered"->btnUpdateStatus.visibility=+View.GONE
            "Complete"->btnUpdateStatus.visibility=+View.GONE
            "Cancel"->btnUpdateStatus.visibility=+View.GONE
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
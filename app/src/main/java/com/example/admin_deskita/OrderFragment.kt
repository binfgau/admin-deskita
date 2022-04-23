package com.example.admin_deskita

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.admin_deskita.request.DeskitaService
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
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val client = DeskitaService()
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



        return inflater.inflate(R.layout.fragment_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try{
            var orders: JSONArray?=client.getOrders();

            val array: ArrayList<String> = ArrayList()
            var ordersId:ArrayList<String> = ArrayList()
            if (orders != null) {
                for(i in 0 until orders.length()){
                    val order =orders.getJSONObject(i)

                    array.add( "Mã đơn hàng: "+order.getString("_id")+"\n"+
                            "Trạng thái: "+order.getString("orderStatus")+"\n"+
                            "Phương thức thanh toán: "+order.getString("paymentMethod")+"\n"+
                            "Ngày tạo: "+order.getString("createAt"))
                    ordersId.add(order.getString("_id"))
                }
            }
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    R.layout.list_order, array)
            }
            val listView:ListView= view.findViewById(R.id.orders)
            listView.adapter=adapter;


            listView.onItemClickListener = object : AdapterView.OnItemClickListener {

                override fun onItemClick(parent: AdapterView<*>, view: View,
                                         position: Int, id: Long) {

                    // value of item that is clicked
                    val preferences= activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                    preferences?.edit()?.putString("order_id",ordersId.get(position))?.apply()
                    findNavController().navigate(R.id.to_order_detail_fragment)
                }
            }
                }catch (e:Exception){
            Log.d("msg",e.stackTraceToString())
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
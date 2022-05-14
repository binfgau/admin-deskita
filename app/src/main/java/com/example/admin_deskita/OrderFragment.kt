package com.example.admin_deskita

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.admin_deskita.entity.MyOrder
import com.example.admin_deskita.entity.Order
import com.example.admin_deskita.entity.OrderStatus
import com.example.admin_deskita.orderstatusfrags.CompletedFragment
import com.example.admin_deskita.orderstatusfrags.ConfirmedFragment
import com.example.admin_deskita.orderstatusfrags.DeliveredFragment
import com.example.admin_deskita.orderstatusfrags.ProcessingFragment
import com.example.admin_deskita.request.DeskitaService
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_order.*
import okhttp3.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class OrderStatusPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    val lstOrderStatusFrags = ArrayList<Fragment>()

    override fun getCount(): Int {
        return lstOrderStatusFrags.size
    }

    override fun getItem(position: Int): Fragment {
        return  lstOrderStatusFrags.get(position)
    }

    fun addFragment(fragment: Fragment){
        lstOrderStatusFrags.add(fragment)
    }
}

class OrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val client = DeskitaService()
    lateinit var completedFrag: CompletedFragment
    lateinit var confirmedFragment: ConfirmedFragment
    lateinit var deliveredFragment: DeliveredFragment
    lateinit var processingFragment: ProcessingFragment
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
        callGetMyOrder()
    }

    fun callGetMyOrder(){
        var orders=client.getOrders();
        val gson = GsonBuilder().create()
        val myOrder = gson.fromJson(orders, MyOrder::class.java)
        classifyOrdersByStatus(myOrder.orders)

    }

    private fun classifyOrdersByStatus(orders: ArrayList<Order>) {
        val lstProcessOrders: ArrayList<Order> = arrayListOf()
        val lstConfirmedOrders: ArrayList<Order> = arrayListOf()
        val lstDeliveredOrders: ArrayList<Order> = arrayListOf()
        val lstCompletedOrders: ArrayList<Order> = arrayListOf()
        for (order in orders){
            for (item in order.orderItems){
                item.total = item.price*item.quantity
            }
            if (order.orderStatus== OrderStatus.Processing){
                lstProcessOrders.add(order)
                continue
            }else if (order.orderStatus==OrderStatus.Confirmed){
                lstConfirmedOrders.add(order)
                continue
            }else if (order.orderStatus==OrderStatus.Delivered){
                lstDeliveredOrders.add(order)
                continue
            }else if (order.orderStatus==OrderStatus.Complete){
                lstCompletedOrders.add(order)
                continue
            }
        }
        setTabPager(lstProcessOrders,lstConfirmedOrders,lstDeliveredOrders,lstCompletedOrders)
    }

    fun setTabPager(lstProcessOrders: ArrayList<Order>,
                    lstConfirmedOrders: ArrayList<Order>,
                    lstDeliveredOrders: ArrayList<Order>,
                    lstCompletedOrders: ArrayList<Order>,){
        val adapter = OrderStatusPagerAdapter(parentFragmentManager)

        completedFrag =CompletedFragment(lstCompletedOrders)
        deliveredFragment = DeliveredFragment(lstDeliveredOrders)
        confirmedFragment = ConfirmedFragment(lstConfirmedOrders,deliveredFragment)
        processingFragment = ProcessingFragment(lstProcessOrders,confirmedFragment)
        adapter.addFragment(processingFragment)
        adapter.addFragment(confirmedFragment)
        adapter.addFragment(deliveredFragment)
        adapter.addFragment(completedFrag)
        vpOrderStatus.adapter = adapter
        tabOrderStatus.setupWithViewPager(vpOrderStatus)

        tabOrderStatus.getTabAt(0)!!.setText("Chờ xử lý")
        tabOrderStatus.getTabAt(1)!!.setText("Xác nhận")
        tabOrderStatus.getTabAt(2)!!.setText("Đang giao")
        tabOrderStatus.getTabAt(3)!!.setText("Đã nhận")
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
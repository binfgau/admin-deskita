package com.example.admin_deskita.orderstatusfrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin_deskita.MainActivity
import com.example.admin_deskita.R
import com.example.admin_deskita.entity.Order
import com.example.admin_deskita.adapter.OrderAdapter
import com.example.admin_deskita.entity.OrderStatus
import kotlinx.android.synthetic.main.frag_confirmed.*
import kotlinx.android.synthetic.main.frag_delivered.*

class DeliveredFragment(var lstDeliveredOrders: ArrayList<Order>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_delivered, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lstvDeliverOrders.adapter = OrderAdapter(requireContext(),lstDeliveredOrders,null)
    }

    fun addDeliveredItem(order: Order){
        order.orderStatus = OrderStatus.Delivered
        (lstvDeliverOrders.adapter as OrderAdapter).addOrderItem(order)
    }
}
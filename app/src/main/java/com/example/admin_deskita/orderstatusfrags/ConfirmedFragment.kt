package com.example.admin_deskita.orderstatusfrags

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin_deskita.R
import com.example.admin_deskita.entity.Order
import com.example.admin_deskita.adapter.OrderAdapter
import com.example.admin_deskita.entity.OrderStatus
import kotlinx.android.synthetic.main.frag_confirmed.*
import kotlinx.android.synthetic.main.frag_delivered.*

class ConfirmedFragment(var lstConfirmedOrders: ArrayList<Order>,var nextFragment: DeliveredFragment) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_confirmed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lstvConfOrders.adapter = OrderAdapter(requireContext(),lstConfirmedOrders,nextFragment)
    }

    fun addConfirmedItem(order: Order){
        order.orderStatus = OrderStatus.Confirmed
        (lstvConfOrders.adapter as OrderAdapter).addOrderItem(order)
    }
}
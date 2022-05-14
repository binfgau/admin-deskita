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
import kotlinx.android.synthetic.main.frag_processing.*

class ProcessingFragment(var lstProcessOrders: ArrayList<Order>,var nextFragment: ConfirmedFragment) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_processing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lstvProcessOrders.adapter = OrderAdapter(requireContext(),lstProcessOrders,nextFragment)
    }
}
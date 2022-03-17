package com.example.admin_deskita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.admin_deskita.databinding.FragmentSecondBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val users = arrayOf(
            "Products", "Orders", "Customer",
            "Profile", "Discount","Analytics"
        )

        val arrayAdapter: ArrayAdapter<String>? = this.context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1, users)
        }
        binding.listFeatures.adapter = arrayAdapter
        binding.listFeatures.onItemClickListener =
            OnItemClickListener { arg0, arg1, position, arg3 ->

                if (position==0){
                    findNavController().navigate(R.id.action_SecondFragment_to_ProductFragment)
                }
                if (position==1){
                    findNavController().navigate(R.id.action_SecondFragment_to_OrderFragment)
                }
            }
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
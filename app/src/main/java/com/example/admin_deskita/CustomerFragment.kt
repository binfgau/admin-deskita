package com.example.admin_deskita

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.example.admin_deskita.adapter.CustomersAdapter
import com.example.admin_deskita.entity.Customer
import com.example.admin_deskita.request.DeskitaService
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val client =DeskitaService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //set list customer
    try {
        val prefs=activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val token = prefs?.getString("TOKEN",null)!!
        var res: JSONObject =client.getUsers("user",token);

        var users=res.getJSONArray("user");
        val array:ArrayList<Customer> = ArrayList()
        for(i in 0 until users.length()){
            val customer=users.getJSONObject(i)
            val customerModel=Customer(customer.getString("_id"),customer.getJSONObject("avatar").getString("url"),
                customer.getString("name"),customer.getString("emailUser"),customer.getString("phoneNumber")
            )
            array.add(customerModel)
        }
        val lvCustomers=view.findViewById(R.id.customers) as ListView
        val adapter= context?.let { CustomersAdapter(it,R.layout.list_customer,array) }
        lvCustomers.adapter=adapter

        lvCustomers.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {

                // value of item that is clicked
                val preferences= activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                val id=array.get(position).id
                preferences?.edit()?.putString("customer_id",id)?.apply()

                //findNavController().navigate(R.id.to_customer_detail_fragment)
            }
        }

    }catch (e:Exception){
        Log.d("error",e.printStackTrace().toString())
    }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
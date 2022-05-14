package com.example.admin_deskita

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.admin_deskita.request.DeskitaService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_customer_detail.*
import kotlinx.android.synthetic.main.list_customer.*
import org.json.JSONObject
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val client = DeskitaService()
    private var customer=JSONObject()
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
        return inflater.inflate(R.layout.fragment_customer_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val prefs=activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val customerId =    prefs?.getString("customer_id",null)

        val token = prefs?.getString("TOKEN",null)!!
        customer= customerId?.let { client.getUserById(it,token) }!!
        customer=customer.getJSONObject("user")
        //set image
        val imageUrl = customer.getJSONObject("avatar").getString("url")
        Picasso.get().load(imageUrl).placeholder(R.drawable.user_avatar).into(imgAvatar)

        //set fullname
        val fullName=view.findViewById(R.id.txtFullName) as TextView
        fullName.text=customer.getString("name")

        //set phoneNumber
        val phoneNumber=view.findViewById(R.id.txtPhoneNumber) as TextView
        phoneNumber.text=customer.getString("phoneNumber")

        //set place
        val place=view.findViewById(R.id.txtPlaceOfBirth) as TextView
        place.text=customer.getString("placeOfBirth")

        //set date of birth
        val date=view.findViewById(R.id.txtDateOfBirth) as TextView
        date.text=customer.getString("dateOfBirth")

        //set email
        val email=view.findViewById(R.id.txtEmail) as TextView
        email.text=customer.getString("emailUser")

        //set create at
        val createAt=view.findViewById(R.id.txtCreateAt) as TextView
        createAt.text=customer.getString("createAt")

        val btnUpdateRole=view.findViewById(R.id.bttChangeRole) as Button
        btnUpdateRole.setOnClickListener{
            try {
                client.updateRole(customerId,token)

            }catch (e:Exception){
                Log.d("errorr",e.printStackTrace().toString())
            }
        }

        bttBack.setOnClickListener{
            findNavController().navigate(R.id.action_back_customerFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
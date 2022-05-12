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
import com.example.admin_deskita.request.DeskitaService
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
        val imageUrl = URL(customer.getJSONObject("avatar").getString("url"))
        val image= view.findViewById(R.id.customer_cus_avatar) as ImageView
        val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
        image.setImageBitmap(bitmap)
        image.requestLayout();
        image.layoutParams.width=400;
        image.layoutParams.height=400;

        //set fullname
        val fullName=view.findViewById(R.id.customer_cus_full_name) as TextView
        fullName.text="Họ và tên: "+customer.getString("name")

        //set phoneNumber
        val phoneNumber=view.findViewById(R.id.customer_cus_phone) as TextView
        phoneNumber.text="Số điện thoại: "+customer.getString("phoneNumber")

        //set place
        val place=view.findViewById(R.id.customer_cus_place) as TextView
        place.text="Nơi sinh: "+customer.getString("placeOfBirth")

        //set date of birth
        val date=view.findViewById(R.id.customer_cus_date) as TextView
        date.text="Ngày sinh: "+customer.getString("dateOfBirth")

        //set email
        val email=view.findViewById(R.id.customer_cus_email) as TextView
        email.text="Email: "+customer.getString("emailUser")

        //set create at
        val createAt=view.findViewById(R.id.customer_cus_create_date) as TextView
        createAt.text="Ngày tạo: "+customer.getString("createAt")

        val btnUpdateRole=view.findViewById(R.id.btn_change_role) as Button
        btnUpdateRole.setOnClickListener{
            try {
                client.updateRole(customerId,token)

            }catch (e:Exception){
                Log.d("errorr",e.printStackTrace().toString())
            }
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
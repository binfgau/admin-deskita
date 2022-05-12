package com.example.admin_deskita

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.admin_deskita.databinding.FragmentFirstBinding
import com.example.admin_deskita.request.DeskitaService
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val client = DeskitaService()

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

        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSendEmail:Button=view.findViewById(R.id.btn_send_email) as Button
        btnSendEmail.setOnClickListener{
            try {
                val etEmail:EditText=view.findViewById(R.id.et_email)
                val res:JSONObject=client.sendEmail(etEmail.text.toString())
                val code=res.getInt("numberToken")

                val preferences= activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                preferences?.edit()?.putInt("code",code)?.apply()

                Toast.makeText(
                    activity, "Gửi email thành công",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_ForgotPasswordFragment_to_RecoveryPasswordFragment)

            }catch (e:Exception){
                Toast.makeText(
                    activity, "Không thể gửi email",
                    Toast.LENGTH_LONG
                ).show()
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
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
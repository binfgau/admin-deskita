package com.example.admin_deskita

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.admin_deskita.request.DeskitaService
import kotlinx.android.synthetic.main.act_forgot_password.*
import kotlinx.android.synthetic.main.act_login.*
import okhttp3.OkHttpClient
import org.json.JSONObject

class ForgotPasswordAct : AppCompatActivity() {
    private val client = DeskitaService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_forgot_password)
        btnSendEmail.setOnClickListener {
            try {
                val res: JSONObject = client.sendEmail(inputEmailFp.text.toString())
                val code = res.getInt("numberToken")

                val preferences = getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                preferences?.edit()?.putInt("code", code)?.apply()

                Toast.makeText(
                    this, "Gửi email thành công",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this,RecoveryPasswordAct::class.java)
                startActivity(intent)

            } catch (e: Exception) {
                Toast.makeText(
                    this, "Không thể gửi email",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}
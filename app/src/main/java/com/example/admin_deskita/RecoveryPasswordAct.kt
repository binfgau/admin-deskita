package com.example.admin_deskita

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.act_recovery_password.*

class RecoveryPasswordAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_recovery_password)
        val prefs= getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val code = prefs?.getInt("code",0)!!
        et_code.setText(code)
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}
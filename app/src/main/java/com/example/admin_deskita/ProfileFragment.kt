package com.example.admin_deskita

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import com.example.admin_deskita.databinding.FragmentProductBinding
import com.example.admin_deskita.databinding.FragmentProfileBinding
import com.example.admin_deskita.entity.Customer
import com.example.admin_deskita.entity.ListProductContainer
import com.example.admin_deskita.entity.Profile
import com.example.admin_deskita.request.DeskitaService
import com.google.android.material.R.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.act_add_update_product.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val client = DeskitaService()
    private var profile = JSONObject()

    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val prefs = activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val token = prefs?.getString("TOKEN", null)!!
        try {
            profile = client.getProfile(token);
            var user = profile.getJSONObject("user")
            //set image
            val imageUrl = URL(user.getJSONObject("avatar").getString("url"))
            println(imageUrl)
            val image = view.findViewById(R.id.imgProfile) as ImageView
            val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
            image.setImageBitmap(bitmap)
            image.requestLayout();
            image.layoutParams.width = 400;
            image.layoutParams.height = 400;

            // set name
            nameprofile.setText(user.getString("name"))
            emailprofile.setText(user.getString("emailUser"))
            birthdayprofile.text = user.getString("dateOfBirth")
            phoneprofile.setText(user.getString("phoneNumber"))
            fromprofile.setText(user.getString("placeOfBirth"))
            roleprofile.text = user.getString("role")

            //select image
            binding.selectImage1.setOnClickListener {
                selectImage()
            }
            //save
            binding.save1.setOnClickListener {
                save(imageUrl.openConnection().getInputStream())
            }

            bttPickDate.setOnClickListener{
                dialogPickDate()
            }
        } catch (e: Exception) {
            client.getProfile(token);
        }
    }

    private fun dialogPickDate() {
        val dialog: BottomSheetDialog = BottomSheetDialog(requireContext(), style.Theme_Design_BottomSheetDialog)
        val viewDatePicker: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_date_picker,null)

        val dpDateOfBirth = viewDatePicker.findViewById<DatePicker>(R.id.dpDateOfBirth)
        val bttConfirmDate = viewDatePicker.findViewById<Button>(R.id.bttConfirmDate)

        bttConfirmDate.setOnClickListener {
            var day = dpDateOfBirth.dayOfMonth.toString()
            if (dpDateOfBirth.dayOfMonth <10) day = "0"+day;
            var month = (dpDateOfBirth.month+1).toString()
            if (dpDateOfBirth.month+1 <10) month = "0"+month;
            val year = dpDateOfBirth.year.toString()
            val birthDay:String = year +"-"+ month+"-"+day

            birthdayprofile.setText(birthDay)
            dialog.dismiss()
        }
        dialog.setContentView(viewDatePicker)
        dialog.show()
    }

    fun save(imageUrl: InputStream) {

        val bitmap = (imgProfile.getDrawable() as BitmapDrawable).toBitmap()
        val encodedImage="data:image/jpeg;base64,"+BitMapToString(bitmap)
        val prefs = requireContext().getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
        val token = prefs?.getString("TOKEN", null)!!
        var name = binding.nameprofile.text
        var email = binding.emailprofile.text
        var birth = binding.birthdayprofile.text
        var phone = binding.phoneprofile.text
        var place = binding.fromprofile.text
        val resJson = client.updateProfile(
            token,
            encodedImage,
            name.toString(),
            email.toString(),
            birth.toString(),
            phone.toString(),
            place.toString()
        )
        if (resJson.has("success")){
            AlertDialog.Builder(requireContext())
                .setTitle("Thay đổi thông tin cá nhân")
                .setMessage("Cập nhật thành công")
                .setPositiveButton("OK",null)
                .show()
        }else{
            Toast.makeText(requireContext(),"Failure!",Toast.LENGTH_SHORT).show()
        }
    }

    fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try {
                imageUri = data!!.data!!
                binding.imgProfile.setImageURI(imageUri)

            } catch (e: Exception) {
                Log.d("error_error", e.stackTraceToString())
            }

        }
    }

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
            }
    }
}
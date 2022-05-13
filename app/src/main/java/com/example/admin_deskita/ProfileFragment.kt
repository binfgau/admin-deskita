package com.example.admin_deskita

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.widget.ImageView
import android.widget.TextView
import com.example.admin_deskita.databinding.FragmentProductBinding
import com.example.admin_deskita.databinding.FragmentProfileBinding
import com.example.admin_deskita.entity.Customer
import com.example.admin_deskita.entity.ListProductContainer
import com.example.admin_deskita.entity.Profile
import com.example.admin_deskita.request.DeskitaService
import com.google.gson.Gson
import com.google.gson.JsonObject
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

        //  val prefs=activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
//        val token = prefs?.getString("TOKEN",null)!!
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyMTdiNTYwNjA1YzAzMjg0YzRlZTc0NiIsImlhdCI6MTY1MjQyMzkwMywiZXhwIjoxNjUzMDI4NzAzfQ.Kl06ryLJNPfswHgzUBgNNbyzp4Ps3BaOz1R-eM7NV14"
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
            val name = user.getString("name");
            val nameprofile = view.findViewById(R.id.nameprofile) as TextView
            nameprofile.text = user.getString("name")
            val emailprofile = view.findViewById(R.id.emailprofile) as TextView
            emailprofile.text = user.getString("emailUser")
            val dateOfBirth = view.findViewById(R.id.birthdayprofile) as TextView
            dateOfBirth.text = user.getString("dateOfBirth")
            val phoneprofile = view.findViewById(R.id.phoneprofile) as TextView
            phoneprofile.text = user.getString("phoneNumber")
            val placeOfBirth = view.findViewById(R.id.fromprofile) as TextView
            placeOfBirth.text = user.getString("placeOfBirth")
            val roleprofile = view.findViewById(R.id.roleprofile) as TextView
            roleprofile.text = user.getString("role")

            //select image
            binding.selectImage1.setOnClickListener {
                selectImage()
            }
            //save
            binding.save1.setOnClickListener {
                save(imageUrl.openConnection().getInputStream())
            }
        } catch (e: Exception) {
            client.getProfile(token);
        }


    }

    fun save(imageUrl : InputStream) {
        val inputStream: InputStream? = context?.contentResolver?.openInputStream(imageUri)
        val selectedImage = BitmapFactory.decodeStream(inputStream)
        val encodedImage =
            "data:" + context?.contentResolver?.getType(imageUri) + ";base64," + BitMapToString(
                selectedImage
            )
        var token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyMTdiNTYwNjA1YzAzMjg0YzRlZTc0NiIsImlhdCI6MTY1MjQyMzkwMywiZXhwIjoxNjUzMDI4NzAzfQ.Kl06ryLJNPfswHgzUBgNNbyzp4Ps3BaOz1R-eM7NV14"

        var name = binding.nameprofile.text
        var email = binding.emailprofile.text
        var birth = binding.birthdayprofile.text
        var phone = binding.phoneprofile.text
        var place = binding.fromprofile.text
        client.updateProfile(
            token,
            encodedImage,
            name.toString(),
            email.toString(),
            birth.toString(),
            phone.toString(),
            place.toString()
        )
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
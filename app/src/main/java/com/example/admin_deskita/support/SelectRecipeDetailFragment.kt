package com.example.admin_deskita.support

import android.os.Bundle
import androidx.fragment.app.Fragment


class SelectRecipeDetailFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(key:String,jsonString: String?): SelectRecipeDetailFragment {
            val frag = SelectRecipeDetailFragment()
            val args = Bundle()
            args.putString(key, jsonString)
            frag.setArguments(args)
            return frag
        }
    }
}
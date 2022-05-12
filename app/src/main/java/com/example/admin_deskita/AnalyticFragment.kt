package com.example.admin_deskita

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.admin_deskita.request.DeskitaService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnalyticFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalyticFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val client = DeskitaService()
    private var type:String?=null
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
        return inflater.inflate(R.layout.fragment_analytic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val arraySpinner = arrayOf(
            "product", "order", "customer", "total"
        )
        val spinner = view.findViewById(R.id.spinner_type) as Spinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                type=arraySpinner.get(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return;
            }
        })

        val btnSelectDate=view.findViewById(R.id.btn_select_date) as Button

        btnSelectDate.setOnClickListener{
            val dateRangePicker=MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select date").build()
            fragmentManager?.let { it1 -> dateRangePicker.show(it1,"date_range_picker") }
            dateRangePicker.addOnPositiveButtonClickListener { datePicked->
                 var dateStart=datePicked.first
                 var dateEnd=datePicked.second
                val prefs=activity?.getSharedPreferences("admin_deskita", Context.MODE_PRIVATE)
                val token = prefs?.getString("TOKEN",null)!!
                val res=client.getAnalyticsByDate(type, convertLongToDate(dateStart),convertLongToDate(dateEnd),token)
                val seriesData=res.getJSONArray("resultArray")

                var lineList=ArrayList<Entry>()

                if(seriesData!=null){
                    for (i in 0 until seriesData.length()) {

                        //Adding each element of JSON array into ArrayList
                        val value=seriesData.get(i).toString().toFloat()

                        val xy=((i+1)*10).toFloat()

                        lineList.add(Entry(xy,value))


                    }
                }
                val lineDataSet:LineDataSet= LineDataSet(lineList,"Count")
                val lineData:LineData= LineData(lineDataSet)
                val chart=view.findViewById(R.id.lineChart) as LineChart
                chart.data=lineData
                chart.invalidate()

                lineDataSet.color=Color.BLACK
                lineDataSet!!.valueTextColor=Color.BLUE
                lineDataSet.valueTextSize=13f
                lineDataSet.setDrawFilled(true)
            }

        }
    }
    private fun convertLongToDate(time:Long):String{
        val date=Date(time)
        val format=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return format.format(date)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnalyticFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnalyticFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


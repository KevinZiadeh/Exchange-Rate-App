package com.kmz07.currencyexchange.ui.statistics

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.kmz07.currencyexchange.R
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Graph
import com.kmz07.currencyexchange.databinding.FragmentStatisticsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private var dates: ArrayList<String>? = null
    private var mean_rate_lbp_to_usd: ArrayList<Float>? = null
    private var mean_rate_usd_to_lbp: ArrayList<Float>? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getGraph(){
        Log.d("mytag", "get")
        ExchangeService.exchangeApi()
            .getGraphs()
            .enqueue(object : Callback<Graph> {
                override fun onFailure(
                    call: Call<Graph>,
                    t: Throwable
                ) {
                    Log.d("mytag", t.stackTraceToString())
                    Snackbar.make(
                        view as View,
                        "Fetching Statistics Failed.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }

                override fun onResponse(
                    call: Call<Graph>,
                    response: Response<Graph>
                ) {
                    val responseBody: Graph? = response.body()
                    dates = responseBody?.graph_dates
                    mean_rate_lbp_to_usd = responseBody?.mean_rate_lbp_to_usd
                    mean_rate_usd_to_lbp = responseBody?.mean_rate_usd_to_lbp
                    setGraph(usdToLbp = false)
                    setGraph(usdToLbp = true)
                }
            })
    }

    private fun setGraph(usdToLbp: Boolean){
        var rateChart: LineChart? = null
        val entries: MutableList<Entry> = ArrayList()

        if (usdToLbp){
            rateChart = binding.usdSellRateEvolChart
            rateChart.description.text = "Evolution of the rate of selling 1$"
            for (i in 0 until mean_rate_usd_to_lbp!!.size){
                entries.add(Entry(i.toFloat(), mean_rate_usd_to_lbp!![i]))
            }
        } else{
            rateChart = binding.usdBuyRateEvolChart
            rateChart.description.text = "Evolution of the rate of buying 1$"
            for (i in 0 until mean_rate_lbp_to_usd!!.size){
                entries.add(Entry(i.toFloat(), mean_rate_lbp_to_usd!![i]))
            }
        }

        Log.d("mytag", entries.joinToString())

        rateChart.setTouchEnabled(true)
        rateChart.setPinchZoom(true)
        rateChart.setDrawBorders(false)
        rateChart.animateXY(1500, 1500, Easing.Linear)
        rateChart.legend.formSize = 0F
        rateChart.description.textSize = 12F
        rateChart.description.textColor = 4278290310.toInt()
        rateChart.description.xOffset = 200F
        rateChart.description.yOffset = -25F
        rateChart.description.textAlign = Paint.Align.CENTER

//        entries.add(Entry(0f, 4f))
//        entries.add(Entry(1f, 3f))
//        entries.add(Entry(2f, 2f))
//        entries.add(Entry(3f, 1f))
//        entries.add(Entry(4f, 5f))
//        entries.add(Entry(5f, 4f))
//        entries.add(Entry(6f, 6f))

        val dataSet = LineDataSet(entries, "")
        dataSet.setDrawHighlightIndicators(false)
        dataSet.setDrawHorizontalHighlightIndicator(false)
        dataSet.setDrawVerticalHighlightIndicator(false)
        dataSet.disableDashedLine()
        dataSet.disableDashedHighlightLine()
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = this.context?.let { ContextCompat.getDrawable(it, R.drawable.gradient) }


        rateChart.axisRight.isEnabled = false
        rateChart.axisLeft.isEnabled = false
        rateChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Customizing x axis value
        val xFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return dates!![value.toInt()]
            }
        }
        rateChart.xAxis.valueFormatter = xFormatter

        // Setting Data
        val data = LineData(dataSet)
        rateChart.data = data
        rateChart.invalidate()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        getGraph()
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

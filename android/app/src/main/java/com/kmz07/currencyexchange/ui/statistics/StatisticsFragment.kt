package com.kmz07.currencyexchange.ui.statistics

import android.annotation.SuppressLint
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
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Graph
import com.kmz07.currencyexchange.api.model.Statistics
import com.kmz07.currencyexchange.databinding.FragmentStatisticsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private var dates: ArrayList<String>? = null
    private var meanRateLbpToUsd: ArrayList<Float>? = null
    private var meanRateUsdToLbp: ArrayList<Float>? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getStats(){
        ExchangeService.exchangeApi()
            .getStatistics()
            .enqueue(object : Callback<Statistics> {
                override fun onFailure(
                    call: Call<Statistics>,
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

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<Statistics>,
                    response: Response<Statistics>
                ) {
                    val responseBody: Statistics? = response.body()

                    binding.maxLbpToUsdValue.text = responseBody?.max_lbp_to_usd.toString()+" LBP"
                    binding.maxUsdToLbpValue.text = responseBody?.max_usd_to_lbp.toString()+" LBP"
                    binding.medianLbpToUsdValue.text = responseBody?.median_lbp_to_usd.toString()+" LBP"
                    binding.medianUsdToLbpValue.text = responseBody?.median_usd_to_lbp.toString()+" LBP"
                    binding.minLbpToUsdValue.text = responseBody?.min_lbp_to_usd.toString()+" LBP"
                    binding.minUsdToLbpValue.text = responseBody?.min_usd_to_lbp.toString()+" LBP"
                    binding.modeLbpToUsdValue.text = responseBody?.mode_lbp_to_usd.toString()+" LBP"
                    binding.modeUsdToLbpValue.text = responseBody?.mode_usd_to_lbp.toString()+" LBP"
                    binding.stdLbpToUsdValue.text = responseBody?.std_lbp_to_usd.toString()+" LBP"
                    binding.stdUsdToLbpValue.text = responseBody?.std_usd_to_lbp.toString()+" LBP"
                    binding.predictedLbpToUsdValue.text = responseBody?.predicted_lbp_to_usd.toString()+" LBP"
                    binding.predictedUsdToLbpValue.text = responseBody?.predicted_usd_to_lbp.toString()+" LBP"

                }
            })
    }

    private fun getGraph(){
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
                    meanRateLbpToUsd = responseBody?.mean_rate_lbp_to_usd
                    meanRateUsdToLbp = responseBody?.mean_rate_usd_to_lbp
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
            rateChart.description.text = "Evolution of the rate of selling 1$ in LBP"
            for (i in 0 until meanRateUsdToLbp!!.size){
                entries.add(Entry(i.toFloat(), meanRateUsdToLbp!![i]))
            }
        } else{
            rateChart = binding.usdBuyRateEvolChart
            rateChart.description.text = "Evolution of the rate of buying 1$ in LBP"
            for (i in 0 until meanRateLbpToUsd!!.size){
                entries.add(Entry(i.toFloat(), meanRateLbpToUsd!![i]))
            }
        }

        rateChart.setTouchEnabled(true)
        rateChart.setPinchZoom(true)
        rateChart.setDrawBorders(false)
        rateChart.animateXY(1500, 1500, Easing.Linear)
        rateChart.legend.formSize = 0F
        rateChart.description.textSize = 12F
        rateChart.description.textColor = 4278290310.toInt()
        rateChart.description.xOffset = 160F
        rateChart.description.yOffset = -25F
        rateChart.description.textAlign = Paint.Align.CENTER


        val dataSet = LineDataSet(entries, "")
        dataSet.setDrawHighlightIndicators(false)
        dataSet.setDrawHorizontalHighlightIndicator(false)
        dataSet.setDrawVerticalHighlightIndicator(false)
        dataSet.disableDashedLine()
        dataSet.disableDashedHighlightLine()
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = this.context?.let { ContextCompat.getDrawable(it, R.drawable.gradient) }
        dataSet.valueTextSize = 10f

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
        getStats()
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

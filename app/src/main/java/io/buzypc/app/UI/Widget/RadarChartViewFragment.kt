// RadarChartViewFragment.kt
package io.buzypc.app.UI.Widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.color.MaterialColors
import io.buzypc.app.R
import io.buzypc.app.Data.AppSession.BuzyUserAppSession

class RadarChartViewFragment : Fragment() {

    private var chart: RadarChart? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("RadarChartFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_radar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("RadarChartFragment", "onViewCreated")
        chart = view.findViewById(R.id.radarChart)
        Log.d("RadarChartFragment", "RadarChart view found: ${chart != null}")
        setupRadarChart()
    }

    private fun setupRadarChart() {
        chart?.let { radarChart ->
            // 1) Basic styling
            radarChart.description.isEnabled = false
            radarChart.isRotationEnabled = false
            radarChart.legend.isEnabled = false

            // the background of the chart;
            radarChart.webLineWidth = 2f
            radarChart.webColor = MaterialColors.getColor(radarChart, com.google.android.material.R.attr.colorSecondary)
            radarChart.webLineWidthInner = 0f
            radarChart.webColorInner = MaterialColors.getColor(radarChart, com.google.android.material.R.attr.colorSecondary)
            radarChart.webAlpha = 200
            radarChart.setBackgroundColor(Color.TRANSPARENT)
            radarChart.animateXY(1500, 1500)

            // 3) Prepare your entries (Retrieve actual data)
            val app = requireActivity().application as BuzyUserAppSession
            val focusedPC = app.selectedBuildToSummarize.pc

            Log.d("RadarChartFragment", "App Session: $app")
            val computingPower = focusedPC.cpu.performanceScore
            val graphicsRendering = focusedPC.gpu.performanceScore
            val dataStorage = focusedPC.storageDevice.performanceScore
            val dataTransferSpeed = focusedPC.ram.performanceScore
            val powerCapacity = focusedPC.psu.performanceScore

            Log.d("RadarChartFragment", "Data: CPU=$computingPower, GPU=$graphicsRendering, Storage=$dataStorage, Network=$dataTransferSpeed, Battery=$powerCapacity")

            val entries = listOf(
                RadarEntry(clamp(computingPower)),
                RadarEntry(clamp(graphicsRendering)),
                RadarEntry(clamp(dataStorage)),
                RadarEntry(clamp(dataTransferSpeed)),
                RadarEntry(clamp(powerCapacity))
            )

            // 4) Build and style the dataset (the translucent part of the chart; the foreground)
            val set = RadarDataSet(entries,"").apply {
                color = MaterialColors.getColor(radarChart, com.google.android.material.R.attr.colorControlNormal)
                fillColor = MaterialColors.getColor(radarChart, com.google.android.material.R.attr.colorControlNormal)
                setDrawFilled(true)
                fillAlpha = 150
                lineWidth = 2f
                isDrawHighlightCircleEnabled = true
                highlightCircleInnerRadius = 1f
                highlightCircleOuterRadius = 3f
                highlightCircleFillColor = MaterialColors.getColor(radarChart, com.google.android.material.R.attr.colorSecondary)
                setDrawHighlightIndicators(false)
            }

            // 5) Wrap in RadarData and style values
            val data = RadarData(set).apply {
                setDrawValues(false)
            }

            radarChart.data = data

            // 6) X‑axis labels
            val labels = listOf("Processor", "Graphics", "Storage", "Memory", "Power")
            radarChart.xAxis.apply {
                isEnabled = true
                textSize = 12f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt() % labels.size]
                    }
                }
            }

            // we don't need Y‑axis labels
            radarChart.yAxis.isEnabled = true
            radarChart.xAxis.textColor = MaterialColors.getColor(radarChart, com.google.android.material.R.attr.colorSecondary)
            radarChart.xAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.ubuntu_bold_italic)

            radarChart.xAxis.apply {
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
            radarChart.yAxis.apply{

                granularity = 2f
                axisMaximum = 10f
                axisMinimum = 0f
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
            radarChart.yAxis.setLabelCount(6,true)

            // 7) Force redraw
            radarChart.data?.notifyDataChanged()
            radarChart.notifyDataSetChanged()
            radarChart.invalidate()
            Log.d("RadarChartFragment", "Chart data set and invalidated")

            // 8) Reveal animation
//            radarChart.isVisible = true
            radarChart.data?.setDrawValues(false)
            revealChart(radarChart)
        } ?: run {
            Log.e("RadarChartFragment", "RadarChart view is null in setupRadarChart")
        }
    }

    private fun revealChart(chartView: RadarChart) {
        chartView.post {
            chartView.isVisible = true
            val fadeAnimator = ObjectAnimator.ofFloat(chartView, "alpha", 0f, 1f).apply { duration = 1000L }
            val scaleXAnimator = ObjectAnimator.ofFloat(chartView, "scaleX", 0.8f, 1f).apply { duration = 1000L }
            val scaleYAnimator = ObjectAnimator.ofFloat(chartView, "scaleY", 0.8f, 1f).apply { duration = 1000L }
            AnimatorSet().apply {
                playTogether(fadeAnimator, scaleXAnimator, scaleYAnimator)
                start()
            }
            Log.d("RadarChartFragment", "Chart reveal animation started")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("RadarChartFragment", "onDestroyView")
        chart = null
    }

    private fun clamp(value: Float): Float = value.coerceIn(0f, 10f)
}
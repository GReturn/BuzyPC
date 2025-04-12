// RadarChartViewFragment.kt
package io.buzypc.app.ui.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewAnimationUtils
import androidx.core.content.ContextCompat
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
import io.buzypc.app.data.appsession.BuzyUserAppSession

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
            radarChart.webLineWidth = 1f
            radarChart.webColor = MaterialColors.getColor(radarChart, androidx.appcompat.R.attr.colorPrimary)
            radarChart.webLineWidthInner = 1f
            radarChart.webColorInner = MaterialColors.getColor(radarChart, androidx.appcompat.R.attr.colorPrimary)
            radarChart.webAlpha = 100
            radarChart.isRotationEnabled = false
            radarChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bz_deepCharcoal))
            radarChart.animateXY(1500, 1500)
            radarChart.legend.isEnabled = false
            radarChart.xAxis.apply{
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
            radarChart.yAxis.apply{
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            // 2) Y‑axis bounds
            radarChart.yAxis.axisMinimum = 0f
            radarChart.yAxis.axisMaximum = 5f

            // 3) Prepare your entries (Retrieve actual data)
            val app = requireActivity().application as BuzyUserAppSession
            Log.d("RadarChartFragment", "App Session: $app")
            val computingPower = 5f
            val graphicsRendering = 4f
            val dataStorage = 3f
            val dataTransferSpeed = 4.5f
            val powerCapacity = 3f

            Log.d("RadarChartFragment", "Data: CPU=$computingPower, GPU=$graphicsRendering, Storage=$dataStorage, Network=$dataTransferSpeed, Battery=$powerCapacity")

            val entries = listOf(
                RadarEntry(computingPower),
                RadarEntry(graphicsRendering),
                RadarEntry(dataStorage),
                RadarEntry(dataTransferSpeed),
                RadarEntry(powerCapacity)
            )

            // 4) Build and style the dataset
            val set = RadarDataSet(entries,"").apply {
                color = MaterialColors.getColor(radarChart, androidx.appcompat.R.attr.colorPrimary)
                fillColor = MaterialColors.getColor(radarChart, androidx.appcompat.R.attr.colorPrimary)
                setDrawFilled(false)
                lineWidth = 2f
                isDrawHighlightCircleEnabled = true
                highlightCircleInnerRadius = 1f
                highlightCircleOuterRadius = 3f
                highlightCircleFillColor = MaterialColors.getColor(radarChart, androidx.appcompat.R.attr.colorPrimary)
                setDrawHighlightIndicators(false)
            }

            // 5) Wrap in RadarData and style values
            val data = RadarData(set).apply {
                setValueTypeface(Typeface.SANS_SERIF)
                setValueTextSize(10f)
                setDrawValues(false)
                setValueTextColor(Color.BLACK)
            }

            radarChart.data = data

            // 6) X‑axis labels
            val labels = listOf("Computing Power", "Graphics Rendering", "Data Storage", "Data Transfer Speed", "Power Capacity")
            radarChart.xAxis.apply {
                isEnabled = true

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt() % labels.size]
                    }
                }
            }

            // we don't need Y‑axis labels
            radarChart.yAxis.isEnabled = false
            radarChart.xAxis.textColor = MaterialColors.getColor(radarChart, androidx.appcompat.R.attr.colorPrimary)
            radarChart.xAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.ubuntu_bold)

            // 7) Force redraw
            radarChart.data?.notifyDataChanged()
            radarChart.notifyDataSetChanged()
            radarChart.invalidate()
            Log.d("RadarChartFragment", "Chart data set and invalidated")

            // 8) Reveal animation
//            radarChart.isVisible = true
            revealChart(radarChart)
        } ?: run {
            Log.e("RadarChartFragment", "RadarChart view is null in setupRadarChart")
        }
    }

    private fun revealChart(chartView: RadarChart) {
        chartView.post {
            val radius = chartView.radius
            val centerX = chartView.width / 2
            val centerY = chartView.height / 2

            chartView.isVisible = true

            val revealAnimator = ViewAnimationUtils.createCircularReveal(chartView, centerX, centerY, 0f, radius)
            val fadeAnimator = ObjectAnimator.ofFloat(chartView, "alpha", 0f, 1f).apply { duration = 1000L }
            val scaleXAnimator = ObjectAnimator.ofFloat(chartView, "scaleX", 0.8f, 1f).apply { duration = 1000L }
            val scaleYAnimator = ObjectAnimator.ofFloat(chartView, "scaleY", 0.8f, 1f).apply { duration = 1000L }

            AnimatorSet().apply {
                playTogether(revealAnimator, fadeAnimator, scaleXAnimator, scaleYAnimator)
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
}
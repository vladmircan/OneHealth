package com.example.onehealth.app.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.onehealth.R
import com.example.onehealth.app.utils.getDimensionInPixels
import com.example.onehealth.app.utils.getResolvedColor
import com.example.onehealth.app.utils.unitCodeStringId
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.utils.Constants
import com.example.onehealth.domain.utils.DateFormats
import com.example.onehealth.domain.utils.format
import com.example.onehealth.domain.utils.formatTimeStamp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@Suppress("ViewConstructor")
class ChartView constructor(
    context: Context,
    private val chartData: ChartDataModel
): LineChart(context) {

    private val xAxisFormatter = IAxisValueFormatter { value, _ ->
        val label = xAxisLabels.getOrNull(value.toInt()) ?: ""
        if (xAxis.axisMaximum <= Constants.NUMBER_OF_MEASUREMENTS_IN_CHART_OVERVIEW || value.toInt() % 5 == 0)
            label
        else
            ""
    }

    private val dataPointsValueFormatter = IValueFormatter { value, _, _, _ ->
        value.format(decimalPlaces = 2)
    }

    var defaultDateFormat = DateFormats.DATE_FORMAT_DD

    var areMarkersEnabled: Boolean = true
        set(value) {
            field = value
            this.setDrawMarkers(value)
        }

    var xAxisRange: Pair<Int, Int> = DEFAULT_X_AXIS_RANGE
        set(value) {
            field = value
            setXAxisRange()
        }

    var xAxisLabels: List<String> = emptyList()

    private val emptyView = TextView(context).apply {
        this.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.font_size_2)
        )
        setText(R.string.no_recent_measurements)

        this@ChartView.addView(this)
        gravity = Gravity.CENTER
    }

    private var maxValueY = 0f

    init {
        configureGraph()
        renderData()
    }

    override fun init() {
        super.init()
        mXAxisRenderer = CustomXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer)
        setYAxisRange(DEFAULT_Y_AXIS_RANGE)
    }

    private fun renderData() {
        xAxis.resetAxisMaximum()
        xAxisLabels = buildNormalModeLabels()

        emptyView.isVisible = chartData.values.isEmpty()

        data = this.computeLineData()
        if (maxValueY > 0.1)
            axisLeft.axisMaximum = maxValueY * 1.3f
        xAxis.setLabelCount(xAxis.axisMaximum.toInt(), false)

        requestLayout()
        fitScreen()
        highlightValues(emptyArray())
    }

    private fun computeLineData(): LineData {
        return LineData(
            createLineDataSet(
                chartData.values,
                context.getString(chartData.chartLabelId),
                resources.getResolvedColor(R.color.teal_700)
            )
        ).apply {
            this.isHighlightEnabled = true
            this.setValueFormatter(dataPointsValueFormatter)
        }
    }

    private fun createLineDataSet(
        observationValues: List<MeasurementModel>,
        dataSetLabel: String,
        lineColor: Int
    ): LineDataSet {
        val entries =
            observationValues.mapIndexed { indexOfObservationValue, measurementValue ->
                val floatValue = measurementValue.value.toFloat()

                if (floatValue > maxValueY)
                    maxValueY = floatValue

                val xAxisIndex = indexOfObservationValue.toFloat()

                Entry(xAxisIndex, floatValue)
            }
        return createNewLineDataSet(entries, dataSetLabel, lineColor)
    }

    private fun createNewLineDataSet(
        entries: List<Entry>,
        dataSetLabel: String,
        lineColor: Int
    ): LineDataSet {
        return LineDataSet(
            entries,
            dataSetLabel
        ).apply {
            this.color = lineColor
            this.circleColors = listOf(lineColor)

            highLightColor = Color.BLUE
            setDrawHorizontalHighlightIndicator(false)
        }
    }

    private fun buildNormalModeLabels(): List<String> {
        return chartData.values.map { measurement ->
            measurement.timeStamp.formatTimeStamp(defaultDateFormat)
        }
    }

    private fun configureGraph() {
        configureYAxis()
        configureXAxis()

        isDoubleTapToZoomEnabled = false
        setPinchZoom(false)
        setScaleEnabled(false)
        isAutoScaleMinMaxEnabled = true
        description.isEnabled = false
        legend.isEnabled = false

        marker = CustomMarkerView()
    }

    private fun configureYAxis() {
        axisLeft.apply {
            setDrawAxisLine(false)
            axisMinimum = 0f
            xOffset = resources.getDimensionInPixels(R.dimen.dimen_1).toFloat()
            textColor = resources.getResolvedColor(R.color.medium_gray_darker)
            gridColor = resources.getResolvedColor(R.color.medium_gray_darker)
            textSize =
                resources.getDimension(R.dimen.font_size_1) / resources.displayMetrics.density
        }
        axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            xOffset = resources.getDimensionInPixels(R.dimen.dimen_1).toFloat()
        }
    }

    private fun configureXAxis() {
        xAxis.apply {
            valueFormatter = xAxisFormatter
            setXAxisRange()
            yOffset = 0f
            granularity = 1f
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            setAvoidFirstLastClipping(false)
            textColor = resources.getResolvedColor(R.color.medium_gray_darker)
            gridColor = resources.getResolvedColor(R.color.medium_gray_darker)
            textSize =
                resources.getDimension(R.dimen.font_size_1) / resources.displayMetrics.density
        }
    }

    private fun setXAxisRange() {
        xAxis.axisMinimum = xAxisRange.first.toFloat()
        xAxis.axisMaximum = xAxisRange.second.toFloat()
    }

    private fun setYAxisRange(yAxisRange: Pair<Long, Long>) {
        axisLeft.axisMinimum = yAxisRange.first.toFloat()
        axisLeft.axisMaximum = yAxisRange.second.toFloat()
    }

    private inner class CustomMarkerView: LinearLayout(context), IMarker {
        private val dateView: TextView

        private val dialogTip = ImageView(context).apply {
            setImageResource(R.drawable.ic_tip)
            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            layout(0, 0, measuredWidth, measuredHeight)
        }

        init {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            orientation = VERTICAL
            clipChildren = false

            val regularPadding = resources.getDimensionInPixels(R.dimen.dimen_2_5)
            val endPadding = resources.getDimensionInPixels(R.dimen.dimen_5)
            setPadding(regularPadding, regularPadding, endPadding, regularPadding)
            setBackgroundResource(R.drawable.background_chart_marker)

            dateView = TextView(context)
            this.addView(
                dateView,
                LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).also { layoutParams ->
                    layoutParams.bottomMargin = resources.getDimensionInPixels(R.dimen.dimen_2)
                })
        }

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            val xIndex = e?.x ?: return
            val measurement = chartData.values[xIndex.toInt()]

            dateView.text =
                measurement.timeStamp.formatTimeStamp(DateFormats.DATE_FORMAT_D_MMMM_YYYY)

            renderValue(measurement)

            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            layout(0, 0, measuredWidth, measuredHeight)
        }

        private fun renderValue(measurement: MeasurementModel) {
            if (childCount > 1)
                removeViewsInLayout(1, childCount - 1)
            val measurementValueLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                clipChildren = false
            }
            val definitionNameView = TextView(context).apply {
                text = resources.getString(chartData.chartLabelId)
            }
            val valueView = TextView(context).apply {
                text = measurement.value.toString()
            }
            val unitCodeView = TextView(context).apply {
                text = resources.getString(measurement.measurementType.unitCodeStringId)
            }
            measurementValueLayout.addView(
                definitionNameView,
                LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).also { layoutParams ->
                    layoutParams.marginEnd = resources.getDimensionInPixels(R.dimen.dimen_2)
                }
            )
            measurementValueLayout.addView(
                valueView,
                LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).also { layoutParams ->
                    layoutParams.marginEnd = resources.getDimensionInPixels(R.dimen.dimen_2)
                }
            )
            measurementValueLayout.addView(
                unitCodeView,
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            )

            this.addView(
                measurementValueLayout,
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            )
        }

        override fun draw(canvas: Canvas, posX: Float, posY: Float) {
            val saveId = canvas.save()

            canvas.translate(
                (posX + -(width.toFloat() - paddingEnd)).coerceAtLeast(0f),
                -height.toFloat()
            )
            draw(canvas)

            canvas.restoreToCount(saveId)

            canvas.translate(posX - dialogTip.measuredWidth / 2, 0f)
            dialogTip.draw(canvas)

            canvas.restoreToCount(saveId)
        }

        override fun getOffset(): MPPointF? = null

        override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF? = null
    }

    companion object {
        val DEFAULT_X_AXIS_RANGE = 0 to Constants.NUMBER_OF_MEASUREMENTS_IN_CHART_OVERVIEW - 1
        val DEFAULT_Y_AXIS_RANGE = 1L to 100L
    }
}
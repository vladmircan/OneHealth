package com.example.onehealth.app.chart

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setPadding
import com.example.onehealth.R
import com.example.onehealth.app.utils.getDimensionInPixels
import com.example.onehealth.app.utils.getResolvedColor
import com.example.onehealth.domain.model.local.ChartDataModel

@Suppress("ViewConstructor")
class ChartCard constructor(
    context: Context,
    chartData: ChartDataModel,
    onAction: () -> Unit
): FrameLayout(context) {

    init {
        val cardView = CardView(context).apply {
            radius = resources.getDimension(R.dimen.dimen_1)
            preventCornerOverlap = true
            useCompatPadding = true
        }

        val rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getResolvedColor(android.R.color.white))
        }

        rootLayout.addView(
            TextView(context).also { cardTitle ->
                val padding = resources.getDimensionInPixels(R.dimen.dimen_3)
                cardTitle.setPadding(padding)
                cardTitle.setTextColor(resources.getResolvedColor(android.R.color.white))
                cardTitle.setBackgroundColor(
                    resources.getResolvedColor(R.color.teal_700)
                )
                cardTitle.text = resources.getString(chartData.chartLabelId)
                cardTitle.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.font_size_3)
                )
                cardTitle.setTypeface(cardTitle.typeface, Typeface.BOLD)
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        rootLayout.addView(
            ChartView(context, chartData).also { chart ->
                chart.areMarkersEnabled = false
                chart.setOnClickListener {
                    onAction()
                }
            },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.chart_height)
            )
        )

        rootLayout.addHorizontalSeparator()

        rootLayout.addView(
            Button(context).apply {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.font_size_3)
                )
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                background = null
                text = resources.getString(R.string.all_measurements)
                isAllCaps = false
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right, 0)
                setOnClickListener {
                    onAction()
                }
            },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { layoutParams ->
                val marginHorizontal = resources.getDimensionInPixels(R.dimen.dimen_2)
                val marginVertical = resources.getDimensionInPixels(R.dimen.dimen_4)
                layoutParams.setMargins(
                    marginHorizontal,
                    marginVertical,
                    marginHorizontal,
                    marginVertical
                )
            }
        )

        cardView.addView(
            rootLayout,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        )
        addView(cardView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    private fun ViewGroup.addHorizontalSeparator() {
        this.addView(View(context).also { separator ->
            separator.setBackgroundColor(resources.getResolvedColor(R.color.separator_color))
        },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also {
                it.height = resources.getDimensionInPixels(R.dimen.separator_thickness)
                val verticalMargin = resources.getDimensionInPixels(R.dimen.dimen_2)
                it.setMargins(0, verticalMargin, 0, verticalMargin)
            }
        )
    }
}
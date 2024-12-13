package com.example.nutrimamadaily

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circleColor: Int = Color.GRAY
    private var progressColor: Int = Color.GREEN
    private var textColor: Int = Color.BLACK
    private var strokeWidth: Float = 20f
    private var max: Int = 100
    private var progress: Int = 0

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 50f
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0).apply {
            try {
                circleColor = getColor(R.styleable.CircularProgressBar_circleColor, circleColor)
                progressColor = getColor(R.styleable.CircularProgressBar_progressColor, progressColor)
                textColor = getColor(R.styleable.CircularProgressBar_textColor, textColor)
                strokeWidth = getDimension(R.styleable.CircularProgressBar_strokeWidth, strokeWidth)
                max = getInteger(R.styleable.CircularProgressBar_max, max)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width / 2f
        val height = height / 2f
        val radius = width - strokeWidth / 2

        // Draw circle
        circlePaint.color = circleColor
        circlePaint.strokeWidth = strokeWidth
        canvas?.drawCircle(width, height, radius, circlePaint)

        // Draw progress arc
        circlePaint.color = progressColor
        val sweepAngle = (progress / max.toFloat()) * 360
        canvas?.drawArc(
            width - radius,
            height - radius,
            width + radius,
            height + radius,
            -90f,
            sweepAngle,
            false,
            circlePaint
        )

        // Draw progress text
        textPaint.color = textColor
        canvas?.drawText("$progress%", width, height + textPaint.textSize / 4, textPaint)
    }

    fun setProgress(value: Int) {
        progress = value.coerceIn(0, max)
        invalidate()
    }
}

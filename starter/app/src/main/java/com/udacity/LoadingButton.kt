package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var loadingRectWidth = 0f
    private var loadingCircDegrees = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorPrimary)
    }

    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG).apply {
        color = context.getColor(R.color.white)
        textAlign = Paint.Align.CENTER
        textSize = 80f
        typeface = Typeface.create( "", Typeface.NORMAL)
    }

    private val loadingRectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorPrimaryDark)
    }

    private val loadingCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorAccent)
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> drawLoadingButton()
            else -> resetLoadingButton()
        }
    }

    private var valueAnimator: ValueAnimator? = null


    init {

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f,0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawRect(0f, 0f, loadingRectWidth, height.toFloat(), loadingRectanglePaint)
        val completedLabel = resources.getString(R.string.button_name)
        val loadingLabel = resources.getString(R.string.button_loading)
        val textX = width.toFloat() / 2f
        val textY = height.toFloat() / 2f + 25f
        when (buttonState) {
            ButtonState.Completed -> canvas.drawText(completedLabel, textX, textY, textPaint)
            else -> canvas.drawText(loadingLabel, textX, textY, textPaint)
        }
        val circleDiameter = 100f
        val circleOffset = 400f
        val circleY = 50f
        canvas.drawArc(
            textX + circleOffset,
            circleY,
            textX+circleOffset + circleDiameter,
            circleY+circleDiameter,
            0f,
            loadingCircDegrees,
            true,
            loadingCirclePaint)
    }


    private fun drawLoadingButton() {
        val propertyDegrees = PropertyValuesHolder.ofFloat("PROPERTY_DEGREES", 0f, 360f);
        val propertyWidth = PropertyValuesHolder.ofFloat("PROPERTY_WIDTH", 0f, width.toFloat())

        valueAnimator = ValueAnimator.ofInt(0, 10).apply {
            duration = 2000
            setValues(propertyDegrees, propertyWidth)
            addUpdateListener { animation ->
                loadingCircDegrees = animation.getAnimatedValue("PROPERTY_DEGREES") as Float
                loadingRectWidth = animation.getAnimatedValue("PROPERTY_WIDTH") as Float
                invalidate()
            }
            start()
        }
    }

    private fun resetLoadingButton() {
        valueAnimator?.end()
        loadingRectWidth = 0f
        loadingCircDegrees = 0f
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}
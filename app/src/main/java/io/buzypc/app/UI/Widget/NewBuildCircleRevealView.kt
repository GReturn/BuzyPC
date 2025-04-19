package io.buzypc.app.UI.Widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd

// this is referenced in the activity_navigation.xml file;
class NewBuildCircleRevealView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // get the color from the theme; similar to "?attr/colorPrimary" in our XML code
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.colorPrimary))
        color = typedArray.getColor(0, 0xE4B363)
    }

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    fun startAnimation(fromX: Float, fromY: Float, maxRadius: Float, onAnimationEnd: () -> Unit) {
        centerX = fromX
        centerY = fromY
        val animator = android.animation.ValueAnimator.ofFloat(0f, maxRadius).apply {
            // TODO (adjust this dude if to change the speed/duration of the circle)
            duration = 600
            addUpdateListener {
                radius = it.animatedValue as Float
                invalidate()
            }
            doOnEnd { onAnimationEnd() }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}

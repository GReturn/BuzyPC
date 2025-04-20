package io.buzypc.app.UI.Utils.LayoutManagers

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.lerp
import io.buzypc.app.UI.Utils.withEndActionOnce
import kotlin.math.abs

abstract class AnimatedGridLayoutManager(
    private val context: Context,
    spanCount: Int = 1
) : GridLayoutManager(context, spanCount) {

    companion object {
        const val DY_COEFFICIENT = 2
        const val ANIMATION_OFFSET = 50L
    }

    private var animator: ValueAnimator? = null
    private var previousDy = 0
    private var currentDy = 0
    private var animatedDy = 0
    private var isAnimated = false

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if(childCount == 0) return 0

        val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
        calculateDy(dy, scrollRange)
        updateViews()
        return scrollRange
    }

    private fun calculateDy(dy: Int, scrollRange: Int) {
        if(isAnimated) return
        val overScroll = dy - scrollRange // no use yet.........

        val topView = getChildAt(0) as View
        val bottomView = getChildAt(childCount - 1) as View

        val isAtTop = getPosition(topView) == 0
                && topView.top >= topView.marginTop + paddingTop

        val isAtBottom = getPosition(bottomView) == itemCount - 1
                && bottomView.bottom <= height - bottomView.marginBottom - paddingBottom

        if(findLastCompletelyVisibleItemPosition() == itemCount - 1 && dy < 0) {
            if(currentDy > 0)
                currentDy += dy
            else
                currentDy = 0
        }
        if(findFirstVisibleItemPosition() == 0 && dy > 0) {
            if(currentDy > 0)
                currentDy -= dy
            else
                currentDy = 0
        }

        val adjustedDelta = dy / DY_COEFFICIENT
        if(isAtTop && dy <= 0) {
            currentDy += abs(adjustedDelta)
            offsetChildrenVertical(-adjustedDelta)
        }
        else if(isAtBottom && dy >= 0) {
            offsetChildrenVertical(-adjustedDelta)
            currentDy += abs(adjustedDelta)
        }
    }

    fun updateViews() {
        val alphaMultiplier = 2
        val scaleMultiplier = 4

        for(i in 0 until childCount) {
            val view = getChildAt(i)
            view?.let {
                // let the `off-screen factor` be a basis as to how
                // a list item varies in appearance is it undergoes changes
                // as the user scrolls it on- and off-screen
                val offScreenFactor = ( paddingTop - it.top ) / it.height.toFloat()
                it.alpha = 1f - offScreenFactor * alphaMultiplier

                var scale = 1f - offScreenFactor / 20f * scaleMultiplier

                if(scale > 1) scale = 1f
                it.scaleX = scale
                it.scaleY = scale
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if(state == RecyclerView.SCROLL_STATE_IDLE) {
            if(getPosition(getChildAt(childCount - 1) as View) == itemCount - 1 && currentDy > 0) {
                animationOffset(false)
            }
            else {
                if(getPosition(getChildAt(0) as View) == 0 && currentDy > 0) {
                    animationOffset(true)
                }
            }
        }
    }

    /**
     * Animates the vertical offset of child views. Think of a rubber-band or snap-back animation.
     *
     * This function creates and starts a `ValueAnimator` to smoothly adjust the vertical
     * position of child views based on the `animatedDy` value. It handles the animation
     * direction based on the `top` parameter and manages the animator's lifecycle, including
     * canceling existing animations.
     *
     * @param top A boolean flag indicating the direction of the offset.
     *            - `true`: Offset is applied upwards (negative direction).
     *            - `false`: Offset is applied downwards (positive direction).
     *
     * The function performs the following actions:
     * 1. **Calculates the coefficient:**
     *    - If `top` is `true`, the `coefficient` is set to -1, indicating an upward offset.
     *    - If `top` is `false`, the `coefficient` is set to 1, indicating a downward offset.
     * 2. **Handles existing animations:**
     *    - If an animation (`animator`) is currently running, it calculates the accumulated
     *      offset (`animatedDy`) by adding the difference between the current and previous
     *      vertical displacement (`currentDy - previousDy`).
     *    - The running animation is then canceled.
     * 3. **Initializes `animatedDy`:**
     *    - If no animation is running, `animatedDy` is set to the current vertical displacement (`currentDy`).
     * 4. **Creates and configures the `ValueAnimator`:**
     *    - A `ValueAnimator` is created to animate from 0 to `animatedDy`.
     *    - An `addUpdateListener` is added to update the vertical offset of the children
     *      during each animation frame.
     *      - It calculates the `offset` based on the current animated value and the
     *        `previousDy`.
     *      - It updates `previousDy` to the current animated value.
     *      - It calls `offsetChildrenVertical` to apply the calculated vertical offset,
     *        multiplied by the `coefficient`.
     *      - It calls `updateViews` (to refresh the UI).
     *    - `doOnStart` listener */
    private fun animationOffset(top: Boolean) {
        val coefficient = if (top) -1 else 1
        if(animator?.isRunning == true) {
            animatedDy += currentDy - previousDy
            animator?.cancel()
        }
        else {
            animatedDy = currentDy
        }

        animator = ValueAnimator.ofInt( 0, animatedDy).apply {
            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                val offset = animatedValue - previousDy
                previousDy = animatedValue
                offsetChildrenVertical(offset * coefficient)
                updateViews()
            }
            doOnStart {
                isAnimated = true
                currentDy = 0
            }
            doOnEnd {
                isAnimated = false
                currentDy = 0
            }
            // adjust duration based on displacement (between 400-800ms)
            duration = lerp(800f, 400f, currentDy.toFloat() / 1000).toLong()
            start()
        }
    }

    fun animateItemsIn() = animate(R.anim.list_item_animation_fade_in)

    fun animateItemsOut() = animate(R.anim.list_item_animation_fade_out)

    /**
     * Initiates an animation specified by the given animation resource ID.
     *
     * This function triggers an animation defined in the application's animation resources.
     * The animation can be used for various visual effects, such as transitions, fade-ins,
     * fade-outs, rotations, and other transformations.
     *
     * @param animationId The resource ID of the animation to play. This should be a valid
     *                    animation resource defined in the `res/anim` directory.
     *                    The ID is annotated with `@AnimRes` to indicate that it must
     *                    refer to an animation resource.
     *
     * @throws Resources.NotFoundException If the resource specified by `animationId`
     *                                       cannot be found.
     *
     * @see androidx.annotation.AnimRes
     * @see android.view.animation.Animation
     */
    abstract fun animate(@AnimRes animationId: Int)
}
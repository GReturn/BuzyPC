package io.buzypc.app.ui.utils

import android.view.animation.Animation

/**
 * Linearly interpolates between two values.
 *
 * This function calculates a value that is a fraction `t` of the way from `a` to `b`.
 * When `t` is 0, the function returns `a`. When `t` is 1, the function returns `b`.
 * When `t` is between 0 and 1, the function returns a value between `a` and `b`.
 * When `t` is outside the range [0, 1], the function extrapolates beyond `a` and `b`.
 *
 * @param a The starting value.
 * @param b The ending value.
 * @param t The interpolation factor, typically between 0 and 1.
 * @return The linearly interpolated value.
 *
 */
fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t

fun Animation.withEndActionOnce(action: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {

        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            setAnimationListener(null)
            action()
        }

        override fun onAnimationStart(animation: Animation?) {
        }
    })
}

fun Animation.withStartActionOnce(action: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {

        override fun onAnimationEnd(animation: Animation?) {
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationStart(animation: Animation?) {
            setAnimationListener(null)
            action()
        }
    })
}
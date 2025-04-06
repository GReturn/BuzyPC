package io.buzypc.app.ui.utils

import android.view.animation.Animation

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
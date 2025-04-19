package io.buzypc.app.UI.Utils.LayoutManagers

import android.content.Context
import android.view.animation.AnimationUtils
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.withEndActionOnce

class BuildListLayoutManager(
    private val context: Context,
    spanCount: Int = 1
) : AnimatedGridLayoutManager(context, spanCount) {
    override fun animate(animationId: Int) {
        var startOffset = 0L

        // for the plus symbol
        var set = AnimationUtils.loadAnimation(context, R.anim.nav_fade_in)
        set.startOffset = 0
        startOffset += ANIMATION_OFFSET
        getChildAt(0)?.startAnimation(set)

        // for the rest
        for(i in 1 until childCount) {
            set = AnimationUtils.loadAnimation(context, animationId)
            set.startOffset = startOffset
            startOffset += ANIMATION_OFFSET
            getChildAt(i)?.startAnimation(set)
            startOffset += ANIMATION_OFFSET
            set.withEndActionOnce { super.updateViews() }
        }
    }
}
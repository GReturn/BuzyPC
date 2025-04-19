package io.buzypc.app.UI.Utils.LayoutManagers

import android.content.Context
import android.view.animation.AnimationUtils
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager.Companion.ANIMATION_OFFSET
import io.buzypc.app.UI.Utils.withEndActionOnce

class BuildTrackerListLayoutManager(
    private val context: Context,
    spanCount: Int = 1
) : AnimatedGridLayoutManager(context, spanCount) {
    override fun animate(animationId: Int) {
        var startOffset = 0L

        // for the plus symbol
        val set = AnimationUtils.loadAnimation(context, animationId)

        // for the rest
        for (i in 0 until childCount) {
            set.startOffset = startOffset
            startOffset += ANIMATION_OFFSET
            getChildAt(i)?.startAnimation(set)
            startOffset += ANIMATION_OFFSET
            set.withEndActionOnce { super.updateViews() }
        }
    }
}
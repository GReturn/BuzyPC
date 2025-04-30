package io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class HorizontalSpaceItemDecoration(private val horizontalSpace: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalSpace

        // Optional: add left margin only for the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = horizontalSpace
        }
    }
}
package com.example.shoppingapplication.ui.modifiedRecyclerView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


/**
 * Created a modified recycler view.
 * The benefit of this recycler view is it will not inherit the action of parent.
 * I have used it in the case when I have to show the recycler view in the navigation view.
 * While showing the recycler view in navigation view, whenever I was swiping from right to left the nav bar was getting closed.
 * To solve this issue ,I have created this custom recycler view.
 * @author Neeraj Mahapatra
 */
class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        // Prevent touch events from being passed to the parent
        parent.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(e)
    }
}
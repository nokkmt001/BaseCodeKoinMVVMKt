package net.hms.imagepicker.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * An image view which always remains square with respect to its width.
 */
internal class SquaredImageView : ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}

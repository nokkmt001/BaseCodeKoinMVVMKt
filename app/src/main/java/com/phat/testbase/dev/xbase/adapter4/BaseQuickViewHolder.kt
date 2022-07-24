package com.phat.testbase.dev.xbase.adapter4

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.util.Linkify
import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.animation.AlphaAnimation
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

class BaseQuickViewHolder(view: View) : RecyclerView.ViewHolder(view){

    /*

    private var views: SparseArray<View?>? = null

    fun getNestViews(): Set<Int?>? {
        return nestViews
    }

    private var nestViews: HashSet<Int?>? = null

    private var childClickViewIds: LinkedHashSet<Int>? = null

    private var itemChildLongClickViewIds: LinkedHashSet<Int>? = null
    private var adapter: BaseQuickAdapter? = null

    /**
     * use itemView instead
     */
    @Deprecated("")
    var convertView: View? = null

    /**
     * Package private field to retain the associated user object and detect a change
     */
    var associatedObject: Any? = null


    fun BaseViewHolder(view: View?) {
        super(view)
        views = SparseArray()
        childClickViewIds = LinkedHashSet()
        itemChildLongClickViewIds = LinkedHashSet()
        nestViews = HashSet()
        convertView = view
    }

    private fun getClickPosition(): Int {
        return if (layoutPosition >= adapter.getHeaderLayoutCount()) {
            layoutPosition - adapter.getHeaderLayoutCount()
        } else 0
    }

    fun getItemChildLongClickViewIds(): HashSet<Int>? {
        return itemChildLongClickViewIds
    }

    fun getChildClickViewIds(): HashSet<Int>? {
        return childClickViewIds
    }

    /**
     * use itemView instead
     *
     * @return the ViewHolder root view
     */
    @Deprecated("")
    fun getConvertView(): View? {
        return convertView
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    fun setText(
        @IdRes viewId: Int,
        value: CharSequence?
    ): BaseQuickViewHolder? {
        val view = getView<TextView>(viewId)!!
        view.text = value
        return this
    }

    fun setText(
        @IdRes viewId: Int,
        @StringRes strId: Int
    ): BaseQuickViewHolder? {
        val view = getView<TextView>(viewId)!!
        view.setText(strId)
        return this
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    fun setImageResource(
        @IdRes viewId: Int,
        @DrawableRes imageResId: Int
    ): BaseQuickViewHolder? {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(imageResId)
        return this
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    fun setBackgroundColor(
        @IdRes viewId: Int,
        @ColorInt color: Int
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    fun setBackgroundRes(
        @IdRes viewId: Int,
        @DrawableRes backgroundRes: Int
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    fun setTextColor(
        @IdRes viewId: Int,
        @ColorInt textColor: Int
    ): BaseQuickViewHolder? {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }


    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    fun setImageDrawable(
        @IdRes viewId: Int,
        drawable: Drawable?
    ): BaseQuickViewHolder? {
        val view = getView<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    fun setImageBitmap(
        @IdRes viewId: Int,
        bitmap: Bitmap?
    ): BaseQuickViewHolder? {
        val view = getView<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    fun setAlpha(@IdRes viewId: Int, value: Float): BaseQuickViewHolder? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)!!.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)!!.startAnimation(alpha)
        }
        return this
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    fun setGone(
        @IdRes viewId: Int,
        visible: Boolean
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    fun setVisible(
        @IdRes viewId: Int,
        visible: Boolean
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The BaseViewHolder for chaining.
     */
    fun linkify(@IdRes viewId: Int): BaseQuickViewHolder? {
        val view = getView<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    fun setTypeface(
        @IdRes viewId: Int,
        typeface: Typeface?
    ): BaseQuickViewHolder? {
        val view = getView<TextView>(viewId)!!
        view.setTypeface(typeface)
        view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        return this
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    fun setTypeface(
        typeface: Typeface?,
        vararg viewIds: Int
    ): BaseQuickViewHolder? {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)!!
            view.setTypeface(typeface)
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The BaseViewHolder for chaining.
     */
    fun setProgress(
        @IdRes viewId: Int,
        progress: Int
    ): BaseQuickViewHolder? {
        val view = getView<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The BaseViewHolder for chaining.
     */
    fun setProgress(
        @IdRes viewId: Int,
        progress: Int,
        max: Int
    ): BaseQuickViewHolder? {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The BaseViewHolder for chaining.
     */
    fun setMax(@IdRes viewId: Int, max: Int): BaseQuickViewHolder? {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The BaseViewHolder for chaining.
     */
    fun setRating(
        @IdRes viewId: Int,
        rating: Float
    ): BaseQuickViewHolder? {
        val view = getView<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The BaseViewHolder for chaining.
     */
    fun setRating(
        @IdRes viewId: Int,
        rating: Float,
        max: Int
    ): BaseQuickViewHolder? {
        val view = getView<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseViewHolder for chaining.
     */
    @Deprecated("")
    fun setOnClickListener(
        @IdRes viewId: Int,
        listener: View.OnClickListener?
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.setOnClickListener(listener)
        return this
    }

    /**
     * add childView id
     *
     * @param viewIds add the child views id can support childview click
     * @return if you use adapter bind listener
     * @link {(adapter.setOnItemChildClickListener(listener))}
     *
     *
     * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
     */
    fun addOnClickListener(@IdRes vararg viewIds: Int): BaseQuickViewHolder? {
        for (viewId in viewIds) {
            childClickViewIds!!.add(viewId)
            val view = getView<View>(viewId)
            if (view != null) {
                if (!view.isClickable) {
                    view.isClickable = true
                }
                view.setOnClickListener(View.OnClickListener { v ->
                    if (adapter.getOnItemChildClickListener() != null) {
                        adapter.getOnItemChildClickListener()
                            .onItemChildClick(adapter, v, getClickPosition())
                    }
                })
            }
        }
        return this
    }


    /**
     * set nestview id
     *
     * @param viewIds add the child views id   can support childview click
     * @return
     */
    fun setNestView(@IdRes vararg viewIds: Int): BaseQuickViewHolder? {
        for (viewId in viewIds) {
            nestViews!!.add(viewId)
        }
        addOnClickListener(*viewIds)
        addOnLongClickListener(*viewIds)
        return this
    }

    /**
     * add long click view id
     *
     * @param viewIds
     * @return if you use adapter bind listener
     * @link {(adapter.setOnItemChildLongClickListener(listener))}
     *
     *
     * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
     */
    fun addOnLongClickListener(@IdRes vararg viewIds: Int): BaseQuickViewHolder? {
        for (viewId in viewIds) {
            itemChildLongClickViewIds!!.add(viewId)
            val view = getView<View>(viewId)
            if (view != null) {
                if (!view.isLongClickable) {
                    view.isLongClickable = true
                }
                view.setOnLongClickListener(OnLongClickListener { v ->
                    adapter.getOnItemChildLongClickListener() != null &&
                            adapter.getOnItemChildLongClickListener()
                                .onItemChildLongClick(adapter, v, getClickPosition())
                })
            }
        }
        return this
    }


    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The BaseViewHolder for chaining.
     */
    @Deprecated("")
    fun setOnTouchListener(
        @IdRes viewId: Int,
        listener: OnTouchListener?
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.setOnTouchListener(listener)
        return this
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The BaseViewHolder for chaining.
     * Please use [.addOnLongClickListener] (adapter.setOnItemChildLongClickListener(listener))}
     */
    @Deprecated("")
    fun setOnLongClickListener(
        @IdRes viewId: Int,
        listener: OnLongClickListener?
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.setOnLongClickListener(listener)
        return this
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item on click listener;
     * @return The BaseViewHolder for chaining.
     * Please use [.addOnClickListener] (int)} (adapter.setOnItemChildClickListener(listener))}
     */
    @Deprecated("")
    fun setOnItemClickListener(
        @IdRes viewId: Int,
        listener: OnItemClickListener?
    ): BaseQuickViewHolder? {
        val view = getView<AdapterView<*>>(viewId)!!
        view.onItemClickListener = listener
        return this
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item long click listener;
     * @return The BaseViewHolder for chaining.
     */
    fun setOnItemLongClickListener(
        @IdRes viewId: Int,
        listener: OnItemLongClickListener?
    ): BaseQuickViewHolder? {
        val view = getView<AdapterView<*>>(viewId)!!
        view.onItemLongClickListener = listener
        return this
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item selected click listener;
     * @return The BaseViewHolder for chaining.
     */
    fun setOnItemSelectedClickListener(
        @IdRes viewId: Int,
        listener: AdapterView.OnItemSelectedListener?
    ): BaseQuickViewHolder? {
        val view = getView<AdapterView<*>>(viewId)!!
        view.onItemSelectedListener = listener
        return this
    }

    /**
     * Sets the on checked change listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The checked change listener of compound button.
     * @return The BaseViewHolder for chaining.
     */
    fun setOnCheckedChangeListener(
        @IdRes viewId: Int,
        listener: CompoundButton.OnCheckedChangeListener?
    ): BaseQuickViewHolder? {
        val view = getView<CompoundButton>(viewId)!!
        view.setOnCheckedChangeListener(listener)
        return this
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    fun setTag(@IdRes viewId: Int, tag: Any?): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.tag = tag
        return this
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    fun setTag(
        @IdRes viewId: Int,
        key: Int,
        tag: Any?
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The BaseViewHolder for chaining.
     */
    fun setChecked(
        @IdRes viewId: Int,
        checked: Boolean
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        // View unable cast to Checkable
        if (view is Checkable) {
            (view as Checkable).isChecked = checked
        }
        return this
    }

    /**
     * Set the enabled state of this view.
     *
     * @param viewId  The view id.
     * @param enable The checked status;
     * @return The BaseViewHolder for chaining.
     */
    fun setEnabled(
        @IdRes viewId: Int,
        enable: Boolean
    ): BaseQuickViewHolder? {
        val view = getView<View>(viewId)!!
        view.isEnabled = enable
        return this
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseViewHolder for chaining.
     */
    fun setAdapter(
        @IdRes viewId: Int,
        adapter: Adapter?
    ): BaseQuickViewHolder? {
        val view = getView<AdapterView<*>>(viewId)!!
        view.setAdapter(adapter)
        return this
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param adapter The adapter;
     * @return The BaseViewHolder for chaining.
     */
    fun setAdapter(adapter: BaseQuickAdapter?): BaseQuickViewHolder? {
        this.adapter = adapter
        return this
    }

    fun <T : View?> getView(@IdRes viewId: Int): T? {
        var view = views!![viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T?
    }

    /**
     * Retrieves the last converted object on this view.
     */
    fun getAssociatedObject(): Any? {
        return associatedObject
    }

    /**
     * Should be called during convert
     */
    fun setAssociatedObject(associatedObject: Any?) {
        this.associatedObject = associatedObject
    }*/

}
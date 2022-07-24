package com.phat.testbase.dev.xbase.adapter4

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

abstract class BaseQuickAdapter<T, K : BaseQuickViewHolder> : RecyclerView.Adapter<K>() {
 /*   private var mNextLoadEnable = false
    private var mLoadMoreEnable = false
    private var mLoading = false
    private var mLoadMoreView: LoadMoreView = SimpleLoadMoreView()
    private var mRequestLoadMoreListener: RequestLoadMoreListener? = null
    private var mEnableLoadMoreEndClick = false

    //Animation
    //Animation
    /**
     * Use with [.openLoadAnimation]
     */
    val ALPHAIN = 0x00000001

    /**
     * Use with [.openLoadAnimation]
     */
    val SCALEIN = 0x00000002

    /**
     * Use with [.openLoadAnimation]
     */
    val SLIDEIN_BOTTOM = 0x00000003

    /**
     * Use with [.openLoadAnimation]
     */
    val SLIDEIN_LEFT = 0x00000004

    /**
     * Use with [.openLoadAnimation]
     */
    val SLIDEIN_RIGHT = 0x00000005
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null
    private var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null

    @IntDef([ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT])
    @Retention(
        RetentionPolicy.SOURCE
    )
    annotation class AnimationType

    private var mFirstOnlyEnable = true
    private var mOpenAnimationEnable = false
    private val mInterpolator: Interpolator = LinearInterpolator()
    private var mDuration = 300
    private var mLastPosition = -1

    private var mCustomAnimation: BaseAnimation? = null
    private var mSelectAnimation: BaseAnimation = AlphaInAnimation()

    //header footer
    private var mHeaderLayout: LinearLayout? = null
    private var mFooterLayout: LinearLayout? = null

    //empty
    private var mEmptyLayout: FrameLayout? = null
    private var mIsUseEmpty = true
    private var mHeadAndEmptyEnable = false
    private var mFootAndEmptyEnable = false

    protected val TAG = BaseQuickAdapter::class.java.simpleName
    protected var mContext: Context? = null
    protected var mLayoutResId = 0
    protected var mLayoutInflater: LayoutInflater? = null
    protected var mData: MutableList<T>? = null
    val HEADER_VIEW = 0x00000111
    val LOADING_VIEW = 0x00000222
    val FOOTER_VIEW = 0x00000333
    val EMPTY_VIEW = 0x00000555

    private var mRecyclerView: RecyclerView? = null

    protected open fun getRecyclerView(): RecyclerView? {
        return mRecyclerView
    }

    private open fun setRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
    }

    private open fun checkNotNull() {
        if (getRecyclerView() == null) {
            throw RuntimeException("please bind recyclerView first!")
        }
    }

    /**
     * same as recyclerView.setAdapter(), and save the instance of recyclerView
     */
    open fun bindToRecyclerView(recyclerView: RecyclerView) {
        if (getRecyclerView() != null) {
            throw RuntimeException("Don't bind twice")
        }
        setRecyclerView(recyclerView)
        getRecyclerView()!!.adapter = this
    }

    /**
     * @see .setOnLoadMoreListener
     */
    @Deprecated(
        """This method is because it can lead to crash: always call this method while RecyclerView is computing a layout or scrolling.
      Please use {@link #setOnLoadMoreListener(RequestLoadMoreListener, RecyclerView)}"""
    )
    open fun setOnLoadMoreListener(requestLoadMoreListener: RequestLoadMoreListener) {
        openLoadMore(requestLoadMoreListener)
    }

    private open fun openLoadMore(requestLoadMoreListener: RequestLoadMoreListener) {
        mRequestLoadMoreListener = requestLoadMoreListener
        mNextLoadEnable = true
        mLoadMoreEnable = true
        mLoading = false
    }

    open fun setOnLoadMoreListener(
        requestLoadMoreListener: RequestLoadMoreListener,
        recyclerView: RecyclerView
    ) {
        openLoadMore(requestLoadMoreListener)
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView)
        }
    }

    /**
     * bind recyclerView [.bindToRecyclerView] before use!
     *
     * @see .disableLoadMoreIfNotFullPage
     */
    open fun disableLoadMoreIfNotFullPage() {
        checkNotNull()
        disableLoadMoreIfNotFullPage(getRecyclerView())
    }

    /**
     * check if full page after [.setNewData], if full, it will enable load more again.
     *
     *
     * 不是配置项！！
     *
     *
     * 这个方法是用来检查是否满一屏的，所以只推荐在 [.setNewData] 之后使用
     * 原理很简单，先关闭 load more，检查完了再决定是否开启
     *
     *
     * 不是配置项！！
     *
     * @param recyclerView your recyclerView
     * @see .setNewData
     */
    open fun disableLoadMoreIfNotFullPage(recyclerView: RecyclerView?) {
        setEnableLoadMore(false)
        if (recyclerView == null) return
        val manager = recyclerView.layoutManager ?: return
        if (manager is LinearLayoutManager) {
            val linearLayoutManager: LinearLayoutManager = manager as LinearLayoutManager
            recyclerView.postDelayed(Runnable {
                if (isFullScreen(linearLayoutManager)) {
                    setEnableLoadMore(true)
                }
            }, 50)
        } else if (manager is StaggeredGridLayoutManager) {
            val staggeredGridLayoutManager: StaggeredGridLayoutManager =
                manager as StaggeredGridLayoutManager
            recyclerView.postDelayed(Runnable {
                val positions = IntArray(staggeredGridLayoutManager.getSpanCount())
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions)
                val pos = getTheBiggestNumber(positions) + 1
                if (pos != itemCount) {
                    setEnableLoadMore(true)
                }
            }, 50)
        }
    }

    private open fun isFullScreen(llm: LinearLayoutManager): Boolean {
        return llm.findLastCompletelyVisibleItemPosition() + 1 !== itemCount ||
                llm.findFirstCompletelyVisibleItemPosition() !== 0
    }

    private open fun getTheBiggestNumber(numbers: IntArray?): Int {
        var tmp = -1
        if (numbers == null || numbers.size == 0) {
            return tmp
        }
        for (num in numbers) {
            if (num > tmp) {
                tmp = num
            }
        }
        return tmp
    }

    /**
     * up fetch start
     */
    private var mUpFetchEnable = false
    private var mUpFetching = false
    private var mUpFetchListener: UpFetchListener? = null

    open fun setUpFetchEnable(upFetch: Boolean) {
        mUpFetchEnable = upFetch
    }

    open fun isUpFetchEnable(): Boolean {
        return mUpFetchEnable
    }

    /**
     * start up fetch position, default is 1.
     */
    private var mStartUpFetchPosition = 1

    open fun setStartUpFetchPosition(startUpFetchPosition: Int) {
        mStartUpFetchPosition = startUpFetchPosition
    }

    private open fun autoUpFetch(positions: Int) {
        if (!isUpFetchEnable() || isUpFetching()) {
            return
        }
        if (positions <= mStartUpFetchPosition && mUpFetchListener != null) {
            mUpFetchListener!!.onUpFetch()
        }
    }

    open fun isUpFetching(): Boolean {
        return mUpFetching
    }

    open fun setUpFetching(upFetching: Boolean) {
        mUpFetching = upFetching
    }

    open fun setUpFetchListener(upFetchListener: UpFetchListener?) {
        mUpFetchListener = upFetchListener
    }

    interface UpFetchListener {
        fun onUpFetch()
    }

    /**
     * up fetch end
     */
    open fun setNotDoAnimationCount(count: Int) {
        mLastPosition = count
    }

    /**
     * Set custom load more
     *
     * @param loadingView 加载视图
     */
    open fun setLoadMoreView(loadingView: LoadMoreView) {
        mLoadMoreView = loadingView
    }

    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    open fun getLoadMoreViewCount(): Int {
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0
        }
        return if (mData!!.size == 0) {
            0
        } else 1
    }

    /**
     * Gets to load more locations
     *
     * @return
     */
    open fun getLoadMoreViewPosition(): Int {
        return getHeaderLayoutCount() + mData!!.size + getFooterLayoutCount()
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    open fun isLoading(): Boolean {
        return mLoading
    }


    /**
     * Refresh end, no more data
     */
    open fun loadMoreEnd() {
        loadMoreEnd(false)
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    open fun loadMoreEnd(gone: Boolean) {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        mLoading = false
        mNextLoadEnable = false
        mLoadMoreView.setLoadMoreEndGone(gone)
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition())
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END)
            notifyItemChanged(getLoadMoreViewPosition())
        }
    }

    /**
     * Refresh complete
     */
    open fun loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        mLoading = false
        mNextLoadEnable = true
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT)
        notifyItemChanged(getLoadMoreViewPosition())
    }

    /**
     * Refresh failed
     */
    open fun loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        mLoading = false
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL)
        notifyItemChanged(getLoadMoreViewPosition())
    }

    /**
     * Set the enabled state of load more.
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    open fun setEnableLoadMore(enable: Boolean) {
        val oldLoadMoreCount = getLoadMoreViewCount()
        mLoadMoreEnable = enable
        val newLoadMoreCount = getLoadMoreViewCount()
        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition())
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT)
                notifyItemInserted(getLoadMoreViewPosition())
            }
        }
    }

    /**
     * Returns the enabled status for load more.
     *
     * @return True if load more is enabled, false otherwise.
     */
    open fun isLoadMoreEnable(): Boolean {
        return mLoadMoreEnable
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration The length of the animation, in milliseconds.
     */
    open fun setDuration(duration: Int) {
        mDuration = duration
    }

    /**
     * If you have added headeview, the notification view refreshes.
     * Do not need to care about the number of headview, only need to pass in the position of the final view
     * @param position
     */
    fun refreshNotifyItemChanged(position: Int) {
        notifyItemChanged(position + getHeaderLayoutCount())
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    open fun BaseQuickAdapter(@LayoutRes layoutResId: Int, @Nullable data: List<T>?) {
        mData = data ?: ArrayList()
        if (layoutResId != 0) {
            mLayoutResId = layoutResId
        }
    }

    open fun BaseQuickAdapter(@Nullable data: List<T>?) {
        this(0, data)
    }

    open fun BaseQuickAdapter(@LayoutRes layoutResId: Int) {
        this(layoutResId, null)
    }

    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    open fun setNewData(@Nullable data: List<T>?) {
        mData = data ?: ArrayList()
        if (mRequestLoadMoreListener != null) {
            mNextLoadEnable = true
            mLoadMoreEnable = true
            mLoading = false
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT)
        }
        mLastPosition = -1
        notifyDataSetChanged()
    }


    /**
     * insert  a item associated with the specified position of adapter
     *
     * @param position
     * @param item
     */
    @Deprecated("use {@link #addData(int, Object)} instead")
    open fun add(@IntRange(from = 0) position: Int, @NonNull item: T) {
        addData(position, item)
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    open fun addData(@IntRange(from = 0) position: Int, @NonNull data: T) {
        mData!!.add(position, data)
        notifyItemInserted(position + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     */
    open fun addData(@NonNull data: T) {
        mData!!.add(data)
        notifyItemInserted(mData!!.size + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    open fun remove(@IntRange(from = 0) position: Int) {
        mData!!.removeAt(position)
        val internalPosition = position + getHeaderLayoutCount()
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, mData!!.size - internalPosition)
    }

    /**
     * change data
     */
    open fun setData(@IntRange(from = 0) index: Int, @NonNull data: T) {
        mData!![index] = data
        notifyItemChanged(index + getHeaderLayoutCount())
    }

    /**
     * add new data in to certain location
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addData(@IntRange(from = 0) position: Int, @NonNull newData: Collection<T>) {
        mData!!.addAll(position, newData)
        notifyItemRangeInserted(position + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * add new data to the end of mData
     *
     * @param newData the new data collection
     */
    open fun addData(@NonNull newData: Collection<T>) {
        mData!!.addAll(newData)
        notifyItemRangeInserted(mData!!.size - newData.size + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * use data to replace all item in mData. this method is different [.setNewData],
     * it doesn't change the mData reference
     *
     * @param data data collection
     */
    open fun replaceData(@NonNull data: Collection<T>) {
        // 不是同一个引用才清空列表
        if (data !== mData) {
            mData!!.clear()
            mData!!.addAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private open fun compatibilityDataSizeChanged(size: Int) {
        val dataSize = if (mData == null) 0 else mData!!.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * Get the data of list
     *
     * @return 列表数据
     */
    @NonNull
    open fun getData(): List<T>? {
        return mData
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    @Nullable
    open fun getItem(@IntRange(from = 0) position: Int): T? {
        return if (position >= 0 && position < mData!!.size) mData!![position] else null
    }

    /**
     * if setHeadView will be return 1 if not will be return 0.
     * notice: Deprecated! Use [ViewGroup.getChildCount] of [.getHeaderLayout] to replace.
     *
     * @return
     */
    @Deprecated("")
    open fun getHeaderViewsCount(): Int {
        return getHeaderLayoutCount()
    }

    /**
     * if mFooterLayout will be return 1 or not will be return 0.
     * notice: Deprecated! Use [ViewGroup.getChildCount] of [.getFooterLayout] to replace.
     *
     * @return
     */
    @Deprecated("")
    open fun getFooterViewsCount(): Int {
        return getFooterLayoutCount()
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    open fun getHeaderLayoutCount(): Int {
        return if (mHeaderLayout == null || mHeaderLayout!!.childCount == 0) {
            0
        } else 1
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    open fun getFooterLayoutCount(): Int {
        return if (mFooterLayout == null || mFooterLayout!!.childCount == 0) {
            0
        } else 1
    }

    /**
     * if show empty view will be return 1 or not will be return 0
     *
     * @return
     */
    open fun getEmptyViewCount(): Int {
        if (mEmptyLayout == null || mEmptyLayout!!.childCount == 0) {
            return 0
        }
        if (!mIsUseEmpty) {
            return 0
        }
        return if (mData!!.size != 0) {
            0
        } else 1
    }

    override fun getItemCount(): Int {
        var count: Int
        if (getEmptyViewCount() == 1) {
            count = 1
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++
            }
        } else {
            count =
                getHeaderLayoutCount() + mData!!.size + getFooterLayoutCount() + getLoadMoreViewCount()
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        if (getEmptyViewCount() == 1) {
            val header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0
            return when (position) {
                0 -> if (header) {
                    HEADER_VIEW
                } else {
                    EMPTY_VIEW
                }
                1 -> if (header) {
                    EMPTY_VIEW
                } else {
                    FOOTER_VIEW
                }
                2 -> FOOTER_VIEW
                else -> EMPTY_VIEW
            }
        }
        val numHeaders = getHeaderLayoutCount()
        return if (position < numHeaders) {
            HEADER_VIEW
        } else {
            var adjPosition = position - numHeaders
            val adapterCount = mData!!.size
            if (adjPosition < adapterCount) {
                getDefItemViewType(adjPosition)
            } else {
                adjPosition = adjPosition - adapterCount
                val numFooters = getFooterLayoutCount()
                if (adjPosition < numFooters) {
                    FOOTER_VIEW
                } else {
                    LOADING_VIEW
                }
            }
        }
    }

    protected open fun getDefItemViewType(position: Int): Int {
        return if (mMultiTypeDelegate != null) {
            mMultiTypeDelegate.getDefItemViewType(mData, position)
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K? {
        var baseViewHolder: K? = null
        mContext = parent.context
        mLayoutInflater = LayoutInflater.from(mContext)
        when (viewType) {
            LOADING_VIEW -> baseViewHolder = getLoadingView(parent)
            HEADER_VIEW -> baseViewHolder = createBaseViewHolder(mHeaderLayout)
            EMPTY_VIEW -> baseViewHolder = createBaseViewHolder(mEmptyLayout)
            FOOTER_VIEW -> baseViewHolder = createBaseViewHolder(mFooterLayout)
            else -> {
                baseViewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(baseViewHolder)
            }
        }
        baseViewHolder.setAdapter(this)
        return baseViewHolder
    }

    private open fun getLoadingView(parent: ViewGroup): K {
        val view = getItemView(mLoadMoreView.getLayoutId(), parent)
        val holder = createBaseViewHolder(view)
        holder.itemView.setOnClickListener {
            if (mLoadMoreView.getLoadMoreStatus() === LoadMoreView.STATUS_FAIL) {
                notifyLoadMoreToLoading()
            }
            if (mEnableLoadMoreEndClick && mLoadMoreView.getLoadMoreStatus() === LoadMoreView.STATUS_END) {
                notifyLoadMoreToLoading()
            }
        }
        return holder
    }

    /**
     * The notification starts the callback and loads more
     */
    open fun notifyLoadMoreToLoading() {
        if (mLoadMoreView.getLoadMoreStatus() === LoadMoreView.STATUS_LOADING) {
            return
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT)
        notifyItemChanged(getLoadMoreViewPosition())
    }

    /**
     * Load more without data when settings are clicked loaded
     *
     * @param enable
     */
    open fun enableLoadMoreEndClick(enable: Boolean) {
        mEnableLoadMoreEndClick = enable
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * [.setFullSpan]
     *
     * @param holder
     */
    override fun onViewAttachedToWindow(holder: K) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected open fun setFullSpan(holder: RecyclerView.ViewHolder) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val params: StaggeredGridLayoutManager.LayoutParams =
                holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.setFullSpan(true)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val gridManager: GridLayoutManager? = manager as GridLayoutManager?
            gridManager.setSpanSizeLookup(object : SpanSizeLookup() {
                fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == HEADER_VIEW && isHeaderViewAsFlow()) {
                        return 1
                    }
                    if (type == FOOTER_VIEW && isFooterViewAsFlow()) {
                        return 1
                    }
                    return if (mSpanSizeLookup == null) {
                        if (isFixedViewType(type)) gridManager.getSpanCount() else 1
                    } else {
                        if (isFixedViewType(type)) gridManager.getSpanCount() else mSpanSizeLookup!!.getSpanSize(
                            gridManager,
                            position - getHeaderLayoutCount()
                        )
                    }
                }
            })
        }
    }

    protected open fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type ==
                LOADING_VIEW
    }

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    private var headerViewAsFlow = false,
    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    private  var footerViewAsFlow:kotlin.Boolean = false

    open fun setHeaderViewAsFlow(headerViewAsFlow: Boolean) {
        this.headerViewAsFlow = headerViewAsFlow
    }

    open fun isHeaderViewAsFlow(): Boolean {
        return headerViewAsFlow
    }

    open fun setFooterViewAsFlow(footerViewAsFlow: Boolean) {
        footerViewAsFlow = footerViewAsFlow
    }

    open fun isFooterViewAsFlow(): Boolean {
        return footerViewAsFlow
    }

    private var mSpanSizeLookup: SpanSizeLookup? = null

    interface SpanSizeLookup {
        fun getSpanSize(gridLayoutManager: GridLayoutManager?, position: Int): Int
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     */
    open fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
        mSpanSizeLookup = spanSizeLookup
    }

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param position
     * @see .getDefItemViewType
     */
    override fun onBindViewHolder(holder: K, position: Int) {
        //Add up fetch logic, almost like load more, but simpler.
        autoUpFetch(position)
        //Do not move position, need to change before LoadMoreView binding
        autoLoadMore(position)
        val viewType = holder.itemViewType
        when (viewType) {
            0 -> convert(holder, getItem(position - getHeaderLayoutCount()))
            LOADING_VIEW -> mLoadMoreView.convert(holder)
            HEADER_VIEW -> {}
            EMPTY_VIEW -> {}
            FOOTER_VIEW -> {}
            else -> convert(holder, getItem(position - getHeaderLayoutCount()))
        }
    }

    private open fun bindViewClickListener(baseViewHolder: BaseViewHolder?) {
        if (baseViewHolder == null) {
            return
        }
        val view: View = baseViewHolder.itemView ?: return
        if (getOnItemClickListener() != null) {
            view.setOnClickListener { v ->
                setOnItemClick(
                    v,
                    baseViewHolder.getLayoutPosition() - getHeaderLayoutCount()
                )
            }
        }
        if (getOnItemLongClickListener() != null) {
            view.setOnLongClickListener { v ->
                setOnItemLongClick(
                    v,
                    baseViewHolder.getLayoutPosition() - getHeaderLayoutCount()
                )
            }
        }
    }

    /**
     * override this method if you want to override click event logic
     *
     * @param v
     * @param position
     */
    open fun setOnItemClick(v: View?, position: Int) {
        getOnItemClickListener()!!.onItemClick(this@BaseQuickAdapter, v, position)
    }

    /**
     * override this method if you want to override longClick event logic
     *
     * @param v
     * @param position
     * @return
     */
    open fun setOnItemLongClick(v: View?, position: Int): Boolean {
        return getOnItemLongClickListener()!!.onItemLongClick(this@BaseQuickAdapter, v, position)
    }

    private var mMultiTypeDelegate: MultiTypeDelegate<T>? = null

    open fun setMultiTypeDelegate(multiTypeDelegate: MultiTypeDelegate<T>?) {
        mMultiTypeDelegate = multiTypeDelegate
    }

    open fun getMultiTypeDelegate(): MultiTypeDelegate<T>? {
        return mMultiTypeDelegate
    }

    protected open fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): K {
        var layoutId = mLayoutResId
        if (mMultiTypeDelegate != null) {
            layoutId = mMultiTypeDelegate.getLayoutId(viewType)
        }
        return createBaseViewHolder(parent, layoutId)
    }

    protected open fun createBaseViewHolder(parent: ViewGroup?, layoutResId: Int): K {
        return createBaseViewHolder(getItemView(layoutResId, parent))
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    protected open fun createBaseViewHolder(view: View?): K {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        val k: K
        // 泛型擦除会导致z为null
        k = z?.let { createGenericKInstance(it, view) } ?: BaseViewHolder(view) as K
        return k ?: BaseViewHolder(view) as K
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    private open fun createGenericKInstance(z: Class<*>, view: View?): K? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as K
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as K
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private open fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    val tempClass = temp
                    if (BaseViewHolder::class.java.isAssignableFrom(tempClass)) {
                        return tempClass
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(
                            rawType
                        )
                    ) {
                        return rawType
                    }
                }
            }
        }
        return null
    }

    /**
     * Return root layout of header
     */
    open fun getHeaderLayout(): LinearLayout? {
        return mHeaderLayout
    }

    /**
     * Return root layout of footer
     */
    open fun getFooterLayout(): LinearLayout? {
        return mFooterLayout
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     *
     * @param header
     */
    open fun addHeaderView(header: View): Int {
        return addHeaderView(header, -1)
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     */
    open fun addHeaderView(header: View, index: Int): Int {
        return addHeaderView(header, index, LinearLayout.VERTICAL)
    }

    /**
     * @param header
     * @param index
     * @param orientation
     */
    open fun addHeaderView(header: View, index: Int, orientation: Int): Int {
        var index = index
        if (mHeaderLayout == null) {
            mHeaderLayout = LinearLayout(header.context)
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout!!.orientation = LinearLayout.VERTICAL
                mHeaderLayout!!.layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            } else {
                mHeaderLayout!!.orientation = LinearLayout.HORIZONTAL
                mHeaderLayout!!.layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        }
        val childCount = mHeaderLayout!!.childCount
        if (index < 0 || index > childCount) {
            index = childCount
        }
        mHeaderLayout!!.addView(header, index)
        if (mHeaderLayout!!.childCount == 1) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    open fun setHeaderView(header: View): Int {
        return setHeaderView(header, 0, LinearLayout.VERTICAL)
    }

    open fun setHeaderView(header: View, index: Int): Int {
        return setHeaderView(header, index, LinearLayout.VERTICAL)
    }

    open fun setHeaderView(header: View, index: Int, orientation: Int): Int {
        return if (mHeaderLayout == null || mHeaderLayout!!.childCount <= index) {
            addHeaderView(header, index, orientation)
        } else {
            mHeaderLayout!!.removeViewAt(index)
            mHeaderLayout!!.addView(header, index)
            index
        }
    }

    /**
     * Append footer to the rear of the mFooterLayout.
     *
     * @param footer
     */
    open fun addFooterView(footer: View): Int {
        return addFooterView(footer, -1, LinearLayout.VERTICAL)
    }

    open fun addFooterView(footer: View, index: Int): Int {
        return addFooterView(footer, index, LinearLayout.VERTICAL)
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of [.addFooterView].
     *
     * @param footer
     * @param index  the position in mFooterLayout of this footer.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of [.addFooterView].
     */
    open fun addFooterView(footer: View, index: Int, orientation: Int): Int {
        var index = index
        if (mFooterLayout == null) {
            mFooterLayout = LinearLayout(footer.context)
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout!!.orientation = LinearLayout.VERTICAL
                mFooterLayout!!.layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            } else {
                mFooterLayout!!.orientation = LinearLayout.HORIZONTAL
                mFooterLayout!!.layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        }
        val childCount = mFooterLayout!!.childCount
        if (index < 0 || index > childCount) {
            index = childCount
        }
        mFooterLayout!!.addView(footer, index)
        if (mFooterLayout!!.childCount == 1) {
            val position = getFooterViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    open fun setFooterView(header: View): Int {
        return setFooterView(header, 0, LinearLayout.VERTICAL)
    }

    open fun setFooterView(header: View, index: Int): Int {
        return setFooterView(header, index, LinearLayout.VERTICAL)
    }

    open fun setFooterView(header: View, index: Int, orientation: Int): Int {
        return if (mFooterLayout == null || mFooterLayout!!.childCount <= index) {
            addFooterView(header, index, orientation)
        } else {
            mFooterLayout!!.removeViewAt(index)
            mFooterLayout!!.addView(header, index)
            index
        }
    }

    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     * @param header
     */
    open fun removeHeaderView(header: View?) {
        if (getHeaderLayoutCount() == 0) return
        mHeaderLayout!!.removeView(header)
        if (mHeaderLayout!!.childCount == 0) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * remove footer view from mFooterLayout,
     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
     *
     * @param footer
     */
    open fun removeFooterView(footer: View?) {
        if (getFooterLayoutCount() == 0) return
        mFooterLayout!!.removeView(footer)
        if (mFooterLayout!!.childCount == 0) {
            val position = getFooterViewPosition()
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     */
    open fun removeAllHeaderView() {
        if (getHeaderLayoutCount() == 0) return
        mHeaderLayout!!.removeAllViews()
        val position = getHeaderViewPosition()
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    open fun removeAllFooterView() {
        if (getFooterLayoutCount() == 0) return
        mFooterLayout!!.removeAllViews()
        val position = getFooterViewPosition()
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    private open fun getHeaderViewPosition(): Int {
        //Return to header view notify position
        if (getEmptyViewCount() == 1) {
            if (mHeadAndEmptyEnable) {
                return 0
            }
        } else {
            return 0
        }
        return -1
    }

    private open fun getFooterViewPosition(): Int {
        //Return to footer view notify position
        if (getEmptyViewCount() == 1) {
            var position = 1
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++
            }
            if (mFootAndEmptyEnable) {
                return position
            }
        } else {
            return getHeaderLayoutCount() + mData!!.size
        }
        return -1
    }

    open fun setEmptyView(layoutResId: Int, viewGroup: ViewGroup?) {
        val view = LayoutInflater.from(viewGroup!!.context).inflate(layoutResId, viewGroup, false)
        setEmptyView(view)
    }

    /**
     * bind recyclerView [.bindToRecyclerView] before use!
     * Recommend you to use [.setEmptyView]
     * @see .bindToRecyclerView
     */
    @Deprecated("")
    open fun setEmptyView(layoutResId: Int) {
        checkNotNull()
        setEmptyView(layoutResId, getRecyclerView())
    }

    open fun setEmptyView(emptyView: View) {
        var insert = false
        if (mEmptyLayout == null) {
            mEmptyLayout = FrameLayout(emptyView.context)
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            val lp = emptyView.layoutParams
            if (lp != null) {
                layoutParams.width = lp.width
                layoutParams.height = lp.height
            }
            mEmptyLayout!!.layoutParams = layoutParams
            insert = true
        }
        mEmptyLayout!!.removeAllViews()
        mEmptyLayout!!.addView(emptyView)
        mIsUseEmpty = true
        if (insert) {
            if (getEmptyViewCount() == 1) {
                var position = 0
                if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                    position++
                }
                notifyItemInserted(position)
            }
        }
    }

    /**
     * Call before [RecyclerView.setAdapter]
     *
     * @param isHeadAndEmpty false will not show headView if the data is empty true will show emptyView and headView
     */
    open fun setHeaderAndEmpty(isHeadAndEmpty: Boolean) {
        setHeaderFooterEmpty(isHeadAndEmpty, false)
    }

    /**
     * set emptyView show if adapter is empty and want to show headview and footview
     * Call before [RecyclerView.setAdapter]
     *
     * @param isHeadAndEmpty
     * @param isFootAndEmpty
     */
    open fun setHeaderFooterEmpty(isHeadAndEmpty: Boolean, isFootAndEmpty: Boolean) {
        mHeadAndEmptyEnable = isHeadAndEmpty
        mFootAndEmptyEnable = isFootAndEmpty
    }

    /**
     * Set whether to use empty view
     *
     * @param isUseEmpty
     */
    open fun isUseEmpty(isUseEmpty: Boolean) {
        mIsUseEmpty = isUseEmpty
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    open fun getEmptyView(): View? {
        return mEmptyLayout
    }

    private var mPreLoadNumber = 1

    @Deprecated("")
    open fun setAutoLoadMoreSize(preLoadNumber: Int) {
        setPreLoadNumber(preLoadNumber)
    }

    open fun setPreLoadNumber(preLoadNumber: Int) {
        if (preLoadNumber > 1) {
            mPreLoadNumber = preLoadNumber
        }
    }

    private open fun autoLoadMore(position: Int) {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        if (position < itemCount - mPreLoadNumber) {
            return
        }
        if (mLoadMoreView.getLoadMoreStatus() !== LoadMoreView.STATUS_DEFAULT) {
            return
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING)
        if (!mLoading) {
            mLoading = true
            if (getRecyclerView() != null) {
                getRecyclerView()!!.post { mRequestLoadMoreListener!!.onLoadMoreRequested() }
            } else {
                mRequestLoadMoreListener!!.onLoadMoreRequested()
            }
        }
    }


    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private open fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.layoutPosition > mLastPosition) {
                var animation: BaseAnimation? = null
                animation = if (mCustomAnimation != null) {
                    mCustomAnimation
                } else {
                    mSelectAnimation
                }
                for (anim in animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.layoutPosition)
                }
                mLastPosition = holder.layoutPosition
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected open fun startAnim(anim: Animator, index: Int) {
        anim.setDuration(mDuration.toLong()).start()
        anim.interpolator = mInterpolator
    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
     * provides a set of LayoutParams values for root of the returned
     * hierarchy
     * @return view will be return
     */
    protected open fun getItemView(@LayoutRes layoutResId: Int, parent: ViewGroup?): View {
        return mLayoutInflater!!.inflate(layoutResId, parent, false)
    }


    interface RequestLoadMoreListener {
        fun onLoadMoreRequested()
    }


    /**
     * Set the view animation type.
     *
     * @param animationType One of [.ALPHAIN], [.SCALEIN], [.SLIDEIN_BOTTOM],
     * [.SLIDEIN_LEFT], [.SLIDEIN_RIGHT].
     */
    open fun openLoadAnimation(@AnimationType animationType: Int) {
        mOpenAnimationEnable = true
        mCustomAnimation = null
        when (animationType) {
            ALPHAIN -> mSelectAnimation = AlphaInAnimation()
            SCALEIN -> mSelectAnimation = ScaleInAnimation()
            SLIDEIN_BOTTOM -> mSelectAnimation = SlideInBottomAnimation()
            SLIDEIN_LEFT -> mSelectAnimation = SlideInLeftAnimation()
            SLIDEIN_RIGHT -> mSelectAnimation = SlideInRightAnimation()
            else -> {}
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    open fun openLoadAnimation(animation: BaseAnimation?) {
        mOpenAnimationEnable = true
        mCustomAnimation = animation
    }

    /**
     * To open the animation when loading
     */
    open fun openLoadAnimation() {
        mOpenAnimationEnable = true
    }

    /**
     * To close the animation when loading
     */
    open fun closeLoadAnimation() {
        mOpenAnimationEnable = false
    }

    /**
     * [.addAnimation]
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    open fun isFirstOnly(firstOnly: Boolean) {
        mFirstOnlyEnable = firstOnly
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun convert(helper: K, item: T?)

    /**
     * get the specific view by position,e.g. getViewByPosition(2, R.id.textView)
     *
     *
     * bind recyclerView [.bindToRecyclerView] before use!
     *
     * @see .bindToRecyclerView
     */
    @Nullable
    open fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        checkNotNull()
        return getViewByPosition(getRecyclerView(), position, viewId)
    }

    @Nullable
    open fun getViewByPosition(
        recyclerView: RecyclerView?,
        position: Int,
        @IdRes viewId: Int
    ): View? {
        if (recyclerView == null) {
            return null
        }
        val viewHolder: BaseViewHolder =
            recyclerView.findViewHolderForLayoutPosition(position) as BaseViewHolder?
                ?: return null
        return viewHolder.getView(viewId)
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private open fun recursiveExpand(position: Int, @NonNull list: List<*>): Int {
        var count = list.size
        var pos = position + list.size - 1
        var i = list.size - 1
        while (i >= 0) {
            if (list[i] is IExpandable) {
                val item: IExpandable = list[i] as IExpandable
                if (item.isExpanded() && hasSubItems(item)) {
                    val subList: List<*> = item.getSubItems()
                    mData!!.addAll(pos + 1, subList)
                    val subItemCount = recursiveExpand(pos + 1, subList)
                    count += subItemCount
                }
            }
            i--
            pos--
        }
        return count
    }

    /**
     * Expand an expandable item
     *
     * @param position     position of the item
     * @param animate      expand items with animation
     * @param shouldNotify notify the RecyclerView to rebind items, **false** if you want to do it
     * yourself.
     * @return the number of items that have been added.
     */
    open fun expand(
        @IntRange(from = 0) position: Int,
        animate: Boolean,
        shouldNotify: Boolean
    ): Int {
        var position = position
        position -= getHeaderLayoutCount()
        val expandable: IExpandable = getExpandableItem(position) ?: return 0
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(true)
            notifyItemChanged(position)
            return 0
        }
        var subItemCount = 0
        if (!expandable.isExpanded()) {
            val list: List<*> = expandable.getSubItems()
            mData!!.addAll(position + 1, list)
            subItemCount += recursiveExpand(position + 1, list)
            expandable.setExpanded(true)
            //            subItemCount += list.size();
        }
        val parentPos = position + getHeaderLayoutCount()
        if (shouldNotify) {
            if (animate) {
                notifyItemChanged(parentPos)
                notifyItemRangeInserted(parentPos + 1, subItemCount)
            } else {
                notifyDataSetChanged()
            }
        }
        return subItemCount
    }

    /**
     * Expand an expandable item
     *
     * @param position position of the item, which includes the header layout count.
     * @param animate  expand items with animation
     * @return the number of items that have been added.
     */
    open fun expand(@IntRange(from = 0) position: Int, animate: Boolean): Int {
        return expand(position, animate, true)
    }

    /**
     * Expand an expandable item with animation.
     *
     * @param position position of the item, which includes the header layout count.
     * @return the number of items that have been added.
     */
    open fun expand(@IntRange(from = 0) position: Int): Int {
        return expand(position, true, true)
    }

    open fun expandAll(position: Int, animate: Boolean, notify: Boolean): Int {
        var position = position
        position -= getHeaderLayoutCount()
        var endItem: T? = null
        if (position + 1 < mData!!.size) {
            endItem = getItem(position + 1)
        }
        val expandable: IExpandable = getExpandableItem(position) ?: return 0
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(true)
            notifyItemChanged(position)
            return 0
        }
        var count = expand(position + getHeaderLayoutCount(), false, false)
        for (i in position + 1 until mData!!.size) {
            val item = getItem(i)
            if (item === endItem) {
                break
            }
            if (isExpandable(item)) {
                count += expand(i + getHeaderLayoutCount(), false, false)
            }
        }
        if (notify) {
            if (animate) {
                notifyItemRangeInserted(position + getHeaderLayoutCount() + 1, count)
            } else {
                notifyDataSetChanged()
            }
        }
        return count
    }

    /**
     * expand the item and all its subItems
     *
     * @param position position of the item, which includes the header layout count.
     * @param init     whether you are initializing the recyclerView or not.
     * if **true**, it won't notify recyclerView to redraw UI.
     * @return the number of items that have been added to the adapter.
     */
    open fun expandAll(position: Int, init: Boolean): Int {
        return expandAll(position, true, !init)
    }

    open fun expandAll() {
        for (i in mData!!.size - 1 + getHeaderLayoutCount() downTo getHeaderLayoutCount()) {
            expandAll(i, false, false)
        }
    }

    private open fun recursiveCollapse(@IntRange(from = 0) position: Int): Int {
        val item = getItem(position)
        if (!isExpandable(item)) {
            return 0
        }
        val expandable: IExpandable? = item as IExpandable?
        var subItemCount = 0
        if (expandable.isExpanded()) {
            val subItems: List<T> = expandable.getSubItems() ?: return 0
            for (i in subItems.indices.reversed()) {
                val subItem = subItems[i]
                var pos = getItemPosition(subItem)
                if (pos < 0) {
                    continue
                } else if (pos < position) {
                    pos = position + i + 1
                    if (pos >= mData!!.size) {
                        continue
                    }
                }
                if (subItem is IExpandable) {
                    subItemCount += recursiveCollapse(pos)
                }
                mData!!.removeAt(pos)
                subItemCount++
            }
        }
        return subItemCount
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @param animate  collapse with animation or not.
     * @param notify   notify the recyclerView refresh UI or not.
     * @return the number of subItems collapsed.
     */
    open fun collapse(@IntRange(from = 0) position: Int, animate: Boolean, notify: Boolean): Int {
        var position = position
        position -= getHeaderLayoutCount()
        val expandable: IExpandable = getExpandableItem(position) ?: return 0
        val subItemCount = recursiveCollapse(position)
        expandable.setExpanded(false)
        val parentPos = position + getHeaderLayoutCount()
        if (notify) {
            if (animate) {
                notifyItemChanged(parentPos)
                notifyItemRangeRemoved(parentPos + 1, subItemCount)
            } else {
                notifyDataSetChanged()
            }
        }
        return subItemCount
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    open fun collapse(@IntRange(from = 0) position: Int): Int {
        return collapse(position, true, true)
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    open fun collapse(@IntRange(from = 0) position: Int, animate: Boolean): Int {
        return collapse(position, animate, true)
    }

    private open fun getItemPosition(item: T?): Int {
        return if (item != null && mData != null && !mData!!.isEmpty()) mData!!.indexOf(item) else -1
    }

    open fun hasSubItems(item: IExpandable?): Boolean {
        if (item == null) {
            return false
        }
        val list: List<*> = item.getSubItems()
        return list != null && list.size > 0
    }

    open fun isExpandable(item: T?): Boolean {
        return item != null && item is IExpandable
    }

    private open fun getExpandableItem(position: Int): IExpandable? {
        val item = getItem(position)
        return if (isExpandable(item)) {
            item as IExpandable?
        } else {
            null
        }
    }

    /**
     * Get the parent item position of the IExpandable item
     *
     * @return return the closest parent item position of the IExpandable.
     * if the IExpandable item's level is 0, return itself position.
     * if the item's level is negative which mean do not implement this, return a negative
     * if the item is not exist in the data list, return a negative.
     */
    open fun getParentPosition(@NonNull item: T): Int {
        val position = getItemPosition(item)
        if (position == -1) {
            return -1
        }

        // if the item is IExpandable, return a closest IExpandable item position whose level smaller than this.
        // if it is not, return the closest IExpandable item position whose level is not negative
        val level: Int
        level = if (item is IExpandable) {
            (item as IExpandable).getLevel()
        } else {
            Int.MAX_VALUE
        }
        if (level == 0) {
            return position
        } else if (level == -1) {
            return -1
        }
        for (i in position downTo 0) {
            val temp = mData!![i]
            if (temp is IExpandable) {
                val expandable: IExpandable = temp as IExpandable
                if (expandable.getLevel() >= 0 && expandable.getLevel() < level) {
                    return i
                }
            }
        }
        return -1
    }

    /**
     * Interface definition for a callback to be invoked when an itemchild in this
     * view has been clicked
     */
    interface OnItemChildClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The view whihin the ItemView that was clicked
         * @param position The position of the view int the adapter
         */
        fun onItemChildClick(adapter: BaseQuickAdapter?, view: View?, position: Int)
    }


    /**
     * Interface definition for a callback to be invoked when an childView in this
     * view has been clicked and held.
     */
    interface OnItemChildLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The childView whihin the itemView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        fun onItemChildLongClick(adapter: BaseQuickAdapter?, view: View?, position: Int): Boolean
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held.
     */
    interface OnItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param adapter  the adpater
         * @param view     The view whihin the RecyclerView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        fun onItemLongClick(adapter: BaseQuickAdapter?, view: View?, position: Int): Boolean
    }


    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView itemView has been clicked.
     */
    interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         *
         * @param adapter  the adpater
         * @param view     The itemView within the RecyclerView that was clicked (this
         * will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        fun onItemClick(adapter: BaseQuickAdapter?, view: View?, position: Int)
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    open fun setOnItemClickListener(@Nullable listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    /**
     * Register a callback to be invoked when an itemchild in View has
     * been  clicked
     *
     * @param listener The callback that will run
     */
    open fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        mOnItemChildClickListener = listener
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    open fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }

    /**
     * Register a callback to be invoked when an itemchild  in this View has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    open fun setOnItemChildLongClickListener(listener: OnItemChildLongClickListener?) {
        mOnItemChildLongClickListener = listener
    }


    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been long clicked and held, or null id no callback as been set.
     */
    fun getOnItemLongClickListener(): OnItemLongClickListener? {
        return mOnItemLongClickListener
    }

    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been clicked and held, or null id no callback as been set.
     */
    fun getOnItemClickListener(): OnItemClickListener? {
        return mOnItemClickListener
    }

    /**
     * @return The callback to be invoked with an itemchild in this RecyclerView has
     * been clicked, or null id no callback has been set.
     */
    @Nullable
    fun getOnItemChildClickListener(): OnItemChildClickListener? {
        return mOnItemChildClickListener
    }

    /**
     * @return The callback to be invoked with an itemChild in this RecyclerView has
     * been long clicked, or null id no callback has been set.
     */
    @Nullable
    fun getOnItemChildLongClickListener(): OnItemChildLongClickListener? {
        return mOnItemChildLongClickListener
    }*/
}
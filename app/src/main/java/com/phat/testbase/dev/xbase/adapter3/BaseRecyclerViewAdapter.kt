package com.phat.testbase.dev.xbase.adapter3

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.phat.testbase.dev.xbase.BaseMvvmViewModel
import com.phat.testbase.model.LoadMoreResponse
import com.techup.pos.base.LoadMoreViewHolder

private const val VIEW_TYPE_NORMAL = 1
private const val VIEW_TYPE_LOAD_MORE = 2
private const val PER_PAGE = 10

/**
 * @param T is a data class
 */
@Suppress("SameParameterValue")
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewViewHolder<T>>() {

    val listData = ArrayList<T>()

    private var callBack: CallBack? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var viewModel: BaseMvvmViewModel? = null

    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var isLimitedData: Boolean = false
    private var isHandleEventNoData: Boolean = false

    fun init(callBack: CallBack,
             viewModel: BaseMvvmViewModel,
             owner: LifecycleOwner,
             refreshLayout: SwipeRefreshLayout? = null,
             isHandleEventNoData: Boolean = true
    ) {
        this.callBack = callBack
        this.viewModel = viewModel
        this.refreshLayout = refreshLayout
        this.refreshLayout?.setOnRefreshListener(onSwipeRefreshListener)
        this.isHandleEventNoData = isHandleEventNoData

        currentPage = 1
        isLoading = false
        isLimitedData = false

//        viewModel.eventErrorMessage.collect {
//            refreshLayout?.setIsRefreshing(false)
//        }
    }

    abstract fun getViewHolder(parent: ViewGroup): BaseRecyclerViewViewHolder<T>

    open fun setupActions(itemView: View, item: T, position: Int) {}

    open fun onViewHolderCreated(holder: BaseRecyclerViewViewHolder<T>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewViewHolder<T> {
        return if (viewType == VIEW_TYPE_LOAD_MORE) {
            LoadMoreViewHolder.create(parent)
        } else {
            getViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewViewHolder<T>, position: Int) {
        if (holder is LoadMoreViewHolder) {
            handleLoadMore()
        } else {
            holder.bind(listData[position])
            setupActions(holder.itemView, listData[position], position)
            onViewHolderCreated(holder)
        }
    }

    // if limit data or list data is empty or list data's size not divisible by per page to return list.size (not load more)
    override fun getItemCount(): Int {
        return if (isLimitedData || listData.isEmpty() || listData.size % PER_PAGE != 0) {
            listData.size
        } else {
            listData.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listData.size) {
            VIEW_TYPE_LOAD_MORE
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    /**
     * Change data
     */
    fun setListData(response: LoadMoreResponse<T>?) {
        if (currentPage == 1) {
            newListData(response)

            if (response?.data?.isNotEmpty() == true) {
//                viewModel?.eventNoData?.sendValueOnChange(false)
            }
        } else {
            moreListData(response)
        }
    }

    fun newData(data: T?) {
        data?.let {
            listData.add(0, data)
            notifyDataSetChanged()
        }
    }

    fun moreData(data: T?) {
        data?.let {
            listData.add(data)
            notifyItemChanged(itemCount)
        }
    }

    fun replaceData(position: Int, data: T?) {
        data?.let {
            listData[position] = it
            notifyItemChanged(position)
        }
    }

    fun removeData(position: Int) {
        listData.removeAt(position)
//        notifyDataSetChanged()
        notifyItemRemoved(position)
        checkData()
    }

    fun clearListData() {
        listData.clear()
        notifyDataSetChanged()
    }

    fun setCurrentPage(page: Int) {
        currentPage = page
    }

    /**
     * Support functions
     */
    private val onSwipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        currentPage = 1
        setIsRefreshOrLoadMore(true)
        callBack?.onRefreshListener()
        checkData()
    }

    private fun newListData(response: LoadMoreResponse<T>?) {
        listData.clear()
        response?.data?.let { listData.addAll(it) }
        notifyDataSetChanged()

        if (refreshLayout?.isRefreshing == true) refreshLayout?.isRefreshing = false
        checkData()
    }

    private fun moreListData(response: LoadMoreResponse<T>?) {
        response?.isLimitedData?.let {
            isLimitedData = it
        }
        response?.data?.let {
            listData.addAll(it)
            notifyDataSetChanged()
            isLoading = false
        }
    }

    private fun handleLoadMore() {
        currentPage ++
        setIsRefreshOrLoadMore(true)
        callBack?.onLoadMoreListener(currentPage)
        isLoading = true
    }

    private fun setIsRefreshOrLoadMore(value: Boolean) {
//        if (viewModel?.isRefreshOrLoadMore != value) {
//            viewModel?.isRefreshOrLoadMore = value
//        }
    }

    private fun checkData() {
        if (isHandleEventNoData) {
//            viewModel?.eventNoData?.value = listData.isEmpty()
        }
    }

    /**
     * Call back interface
     */
    interface CallBack {
        fun onLoadMoreListener(page: Int)
        fun onRefreshListener()
    }

}

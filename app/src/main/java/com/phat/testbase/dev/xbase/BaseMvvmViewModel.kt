package com.phat.testbase.dev.xbase

import androidx.lifecycle.ViewModel
import com.phat.testbase.dev.utils.ContainsUtils
import com.phat.testbase.model.RepoState
import com.phat.testbase.model.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.await
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeoutException

fun <T> MutableStateFlow<T>.sendValue(value: T) {
    this.value = value
}

abstract class BaseMvvmViewModel : ViewModel() {

    protected var viewModelJob = SupervisorJob()
    protected val ioContext = viewModelJob + Dispatchers.IO // background context
    protected val uiContext = viewModelJob + Dispatchers.Main // ui context
    protected val ioScope = CoroutineScope(ioContext)
    protected val uiScope = CoroutineScope(uiContext)

    var _eventLoading = MutableStateFlow(false)
    var _eventErrorMessage = MutableStateFlow("")
    var _eventNoData = MutableStateFlow(false)

    var eventLoading: StateFlow<Boolean> = _eventLoading
    var eventErrorMessage: StateFlow<String> = _eventErrorMessage
    var eventNoData: StateFlow<Boolean> = _eventNoData
    var isRefreshOrLoadMore: Boolean = false
    var isMultiLoading: Boolean = false
    var isMultiCall: Boolean = false

    protected open fun getHeaders(token: String?): HashMap<String, Any> {
        val headers = HashMap<String, Any>()
        if (token != null && ContainsUtils.HEADER_HEAD_TOKEN.isNotEmpty())
            headers[ContainsUtils.HEADER_AUTHORIZATION] = token
        headers[ContainsUtils.HEADER_CONTENT_TYPE] = ContainsUtils.HEADER_CONTENT_TYPE_VALUE_JSON
        if (ContainsUtils.isMultiLanguage) headers[ContainsUtils.HEADER_LANG] =
            ContainsUtils.DEFAULT_LANGUAGE
        headers[ContainsUtils.HEADER_API_KEY] = ""
        headers[ContainsUtils.HEADER_UUID] = UUID.randomUUID()
        return headers
    }

    fun CoroutineScope.start(block: suspend CoroutineScope.() -> Unit): Job {
        showLoading(true)
        return this.launch(block = block)
    }

    fun showLoading(isShow: Boolean) {
        _eventLoading.sendValue(isShow)
    }

    fun CoroutineScope.startWithLoadMore(block: suspend CoroutineScope.() -> Unit): Job {
        if (!isRefreshOrLoadMore) {
            showLoading(true)
        }
        return this.launch(block = block)
    }

    fun CoroutineScope.startWithMultiCall(block: suspend CoroutineScope.() -> Unit): Job {
        if (!isMultiCall) {
            showLoading(true)
        }
        return this.launch(block = block)
    }

    suspend fun <T : Any> makeApiCall(call: suspend () -> Call<T>): T? {
        return try {
            val result = call.invoke().await()
            result.run {
                return this
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun <T> Call<T>.callApi(): UiState<T> {
        val result: UiState<T>
        val response = try {
            execute()
        } catch (e: TimeoutException) {
            throw TimeoutException()
        } catch (e: Throwable) {
            throw e
        }
        result = if (response.isSuccessful) {
            UiState(RepoState.SUCCESS, response.body())
        } else {
            Timber.tag("Error").e(response.message())
            Timber.tag("ErrorCode").e(response.code().toString())
            _eventErrorMessage.sendValue(response.message())
            UiState(RepoState.FAIL, message = response.message(), code = response.code())
        }
        return result
    }

    fun <T> Call<T>.callApiAny(): Any? {
        val response = try {
            execute()
        } catch (e: TimeoutException) {
            throw TimeoutException()
        } catch (e: Throwable) {
            throw e
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            Timber.tag("Error").d(response.message())
            Timber.tag("ErrorCode").d(response.code().toString())
            response.message()
        }
    }

    fun <T> Call<T>.callApi(function: UiState<T>?.() -> Unit): UiState<T> {
        return callApi().apply(function)
    }

    fun <T> Call<T>.tryCall(shouldBeSuccess: Throwable.() -> Boolean): UiState<T>? {
        return try {
            callApi()
        } catch (e: Throwable) {
            if (!shouldBeSuccess(e)) throw e else null
        }
    }

    fun clearAll(){
        viewModelJob.cancel()
        _eventLoading.sendValue(false)
        _eventErrorMessage.sendValue("")
        _eventNoData.sendValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        clearAll()
    }

}
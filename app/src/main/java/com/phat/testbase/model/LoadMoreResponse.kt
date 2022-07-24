package com.phat.testbase.model

data class LoadMoreResponse<T>(
        val data: List<T>? = null,
        val isLimitedData: Boolean = false
)

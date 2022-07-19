package com.phat.testbase.devphat.view

import com.phat.testbase.model.ProfileResponse
import com.phat.testbase.network.TestService
import com.phat.testbase.devphat.xbase.BaseMvvmViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestViewModel(private val service: TestService) : BaseMvvmViewModel() {

    var _result = MutableStateFlow(ProfileResponse())

    var result: StateFlow<ProfileResponse> = _result

    fun testData() {
        ioScope.start {
            service.getProfileAsync(getHeaders("")).await().callApi {
                _result.value = this!!
                showLoading(false)
            }

//            service.getProfileAsync(getHeaders("")).await()
        }
    }
}


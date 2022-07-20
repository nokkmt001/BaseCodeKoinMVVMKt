package com.phat.testbase.dev.view

import com.phat.testbase.model.ProfileResponse
import com.phat.testbase.network.TestService
import com.phat.testbase.dev.xbase.BaseMvvmViewModel
import com.phat.testbase.di.TestClient
import com.phat.testbase.model.TestBanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestViewModel : BaseMvvmViewModel() {

    private val service: TestService = TestClient.getClient()

    var _result = MutableStateFlow(ProfileResponse())

    var result: StateFlow<ProfileResponse> = _result

    var _resultBanner = MutableStateFlow(TestBanner())

    var resultBanner: StateFlow<TestBanner> = _resultBanner

    fun testDataBanner() {
        ioScope.start {
            service.getBannerAsync().callApi {
                _resultBanner.value = this?.result!!
                showLoading(false)
            }
        }
    }
}


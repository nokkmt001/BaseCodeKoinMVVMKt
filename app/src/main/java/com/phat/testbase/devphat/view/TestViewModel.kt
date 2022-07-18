package com.phat.testbase.devphat.view

import com.phat.testbase.model.ProfileResponse
import com.phat.testbase.network.TestService
import com.phat.testbase.view.ui.base.BaseMvvmViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestViewModel(private val service: TestService) : BaseMvvmViewModel() {
    var _result = MutableStateFlow(ProfileResponse())

    var result: StateFlow<ProfileResponse> = _result

    fun testData() {
        ioScope.launch {
            service.getProfileAsync(getHeaders("")).await().callApi {
                _result.value = this!!
            }

            service.getProfileAsync(getHeaders("")).call {

}
            }
        }
    }
}


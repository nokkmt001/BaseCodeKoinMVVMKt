/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phat.testbase.network

import com.phat.testbase.MainCoroutinesRule
import com.phat.sandwich.ApiResponse
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class DisneyServiceTest : ApiAbstract<DisneyService>() {

  private lateinit var service: DisneyService

  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @Before
  fun initService() {
    service = createService(DisneyService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchDisneyPosterListTest() = runBlocking {
    enqueueResponse("/DisneyPosters.json")
    val response = service.fetchDisneyPosterList()
    val responseBody = requireNotNull((response as ApiResponse.Success).data)
    mockWebServer.takeRequest()

    assertThat(responseBody[0].id, `is`(0L))
    assertThat(responseBody[0].name, `is`("Frozen II"))
    assertThat(responseBody[0].release, `is`("2019"))
  }
}

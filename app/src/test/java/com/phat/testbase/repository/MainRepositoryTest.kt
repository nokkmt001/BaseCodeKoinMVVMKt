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

@file:Suppress("SpellCheckingInspection")

package com.phat.testbase.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.phat.testbase.MainCoroutinesRule
import com.phat.testbase.network.DisneyService
import com.phat.testbase.persistence.PosterDao
import com.phat.testbase.utils.MockTestUtil.mockPosterList
import com.phat.sandwich.ApiResponse
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class MainRepositoryTest {

  private lateinit var repository: MainRepository
  private val service: DisneyService = mock()
  private val posterDao: PosterDao = mock()

  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    repository = MainRepository(service, posterDao)
  }

  @Test
  fun loadDisneyPostersTest() = runBlocking {
    val mockData = mockPosterList()
    whenever(posterDao.getPosterList()).thenReturn(emptyList())
    whenever(service.fetchDisneyPosterList()).thenReturn(
      ApiResponse.of { Response.success(mockData) }
    )

    repository.loadDisneyPosters(
      onSuccess = {}
    ).collect {
      assertThat(it[0].id, `is`(0L))
      assertThat(it[0].name, `is`("Frozen II"))
      assertThat(it[0].release, `is`("2019"))
      assertThat(it, `is`(mockData))
    }

    verify(posterDao, atLeastOnce()).getPosterList()
    verify(service, atLeastOnce()).fetchDisneyPosterList()
    verify(posterDao, atLeastOnce()).insertPosterList(mockData)
  }
}

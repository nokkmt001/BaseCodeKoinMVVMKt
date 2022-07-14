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

package com.phat.testbase.view.adapter

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.SectionRow
import com.phat.testbase.R
import com.phat.testbase.model.Poster
import com.phat.testbase.view.viewholder.PosterCircleViewHolder

class PosterCircleAdapter : BaseAdapter() {

  init {
    addSection(arrayListOf<Poster>())
  }

  fun addPosterList(posters: List<Poster>) {
    sections().first().run {
      clear()
      addAll(posters)
      notifyDataSetChanged()
    }
  }

  override fun layout(sectionRow: SectionRow) = R.layout.item_poster_circle

  override fun viewHolder(layout: Int, view: View) = PosterCircleViewHolder(view)
}

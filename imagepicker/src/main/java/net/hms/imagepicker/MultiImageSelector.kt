package net.hms.imagepicker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.*

class MultiImageSelector private constructor() {

    private var mShowCamera = true
    private var mMaxCount = 9
    private var mMinCount = 1
    private var mMode = MultiImageSelectorActivity.MODE_MULTI
    private var mOriginData: ArrayList<String>? = null

    fun showCamera(show: Boolean): MultiImageSelector? {
        mShowCamera = show
        return sSelector
    }

    fun count(count: Int): MultiImageSelector? {
        mMaxCount = count
        return sSelector
    }

    fun minCount(count: Int): MultiImageSelector? {
        mMinCount = count
        return sSelector
    }

    fun single(): MultiImageSelector? {
        mMode = MultiImageSelectorActivity.MODE_SINGLE
        return sSelector
    }

    fun multi(): MultiImageSelector? {
        mMode = MultiImageSelectorActivity.MODE_MULTI
        return sSelector
    }

    fun cameraOnly(): MultiImageSelector? {
        mMode = MultiImageSelectorActivity.MODE_CAMERA
        return sSelector
    }

    fun origin(images: ArrayList<String>): MultiImageSelector? {
        mOriginData = images
        return sSelector
    }

    fun start(activity: Activity, requestCode: Int) {
        if (hasPermission(activity)) {
            activity.startActivityForResult(createIntent(activity), requestCode)
        } else {
            Toast.makeText(activity, R.string.error_no_permission, Toast.LENGTH_SHORT).show()
        }
    }

    fun start(fragment: Fragment, requestCode: Int) {
        fragment.context?.let {

            if (hasPermission(it)) {
                fragment.startActivityForResult(createIntent(it), requestCode)
            } else {
                Toast.makeText(it, R.string.error_no_permission, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun createIntent(context: Context?): Intent {
        val intent = Intent(context, MultiImageSelectorActivity::class.java)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, mShowCamera)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, mMaxCount)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MIN_COUNT, mMinCount)
        if (mOriginData != null) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData)
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mMode)
        return intent
    }

    companion object {

        const val EXTRA_RESULT =
            MultiImageSelectorActivity.EXTRA_RESULT
        private var sSelector: MultiImageSelector? = null

        fun create(): MultiImageSelector {
            if (sSelector == null) {
                sSelector =
                    MultiImageSelector()
            }
            return sSelector as MultiImageSelector
        }
    }
}

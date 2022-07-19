package net.hms.imagepicker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import net.hms.imagepicker.adapters.FolderAdapter
import net.hms.imagepicker.adapters.ImageGridAdapter
import net.hms.imagepicker.models.Folder
import net.hms.imagepicker.models.Image
import net.hms.imagepicker.utils.FileUtils
import net.hms.imagepicker.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_default.*
import kotlinx.android.synthetic.main.popup_folder.view.*
import java.io.File
import java.io.IOException
import java.util.*

class MultiImageSelectorActivity : AppCompatActivity() {

    private var resultList: ArrayList<String> = ArrayList()
    private var mDefaultCount =
        DEFAULT_IMAGE_SIZE
    private var mDefaultMinCount =
        DEFAULT_MIN_IMAGE_SIZE

    private var mImageAdapter: ImageGridAdapter? = null
    private var mFolderAdapter: FolderAdapter? = null

    private var mFolderPopupWindow: PopupWindow? = null

    private var mTmpFile: File? = null

    private var hasFolderGened = false

    private var mode = MODE_MULTI
    private var isShowCamera = true

    lateinit var viewModel: MultiImageSelectorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.ThemeImagePicker)
        setContentView(R.layout.activity_default)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)

        mDefaultCount = intent.getIntExtra(
            EXTRA_SELECT_COUNT,
            DEFAULT_IMAGE_SIZE
        )
        mDefaultMinCount = intent.getIntExtra(
            EXTRA_SELECT_MIN_COUNT,
            DEFAULT_MIN_IMAGE_SIZE
        )
        mode = intent.getIntExtra(
            EXTRA_SELECT_MODE,
            MODE_MULTI
        )
        isShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true)
        if (mode == MODE_MULTI && intent.hasExtra(
                EXTRA_DEFAULT_SELECTED_LIST
            )) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST)
        }

        setupViews()
        setupPopupFolder(this)

        viewModel = ViewModelProviders.of(this).get(MultiImageSelectorViewModel::class.java)
        viewModel.getListImage()

        viewModel.listImage.observe(this, androidx.lifecycle.Observer {

            mImageAdapter?.setData(it)
            if (resultList.size > 0) {
                mImageAdapter?.setDefaultSelected(resultList)
            }

        })

        viewModel.listFolder.observe(this, androidx.lifecycle.Observer {
            if (!hasFolderGened) {
                mFolderAdapter?.setData(it)
                hasFolderGened = true
            }
        })

        // camera only
        if (mode == MODE_CAMERA) {
            showCameraAction()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                mTmpFile?.let {tmpFile ->
                    if (mode == MODE_MULTI) {
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(tmpFile)))

                        if (mDefaultCount <= resultList.size) {
                            onCameraResultUpdate(tmpFile.absolutePath, true)
                        } else {
                            resultList.add(tmpFile.absolutePath)
                            onCameraResultUpdate(tmpFile.absolutePath, false)
                        }

                        MediaScannerConnection.scanFile(this, arrayOf(tmpFile.absolutePath), null) { path, uri ->
                            viewModel.getListImage()
                        }

                    } else {
                        onCameraShot(tmpFile)
                    }
                }
            } else {
                // delete tmp file
                mTmpFile?.let {tmpFile ->
                    while (tmpFile.exists()) {
                        val success = tmpFile.delete()
                        if (success) {
                            mTmpFile = null
                        }
                    }
                }

                finish()
            }
        }

    }

    private fun setupViews() {

        // list images
        mImageAdapter = ImageGridAdapter(this, isShowCamera, 3)
        mImageAdapter?.showSelectIndicator(mode == MODE_MULTI)
        mImageAdapter?.itemClick = {position, image ->
            imageItemClick(position, image)
        }
        recycler_view.adapter = mImageAdapter

        if (mode == MODE_MULTI) {
            updateDoneText(resultList)
            button_done.visibility = View.VISIBLE
            button_done.setOnClickListener {
                if (resultList.size > 0) {
                    val data = Intent()
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList)
                    setResult(Activity.RESULT_OK, data)
                    finish()
                } else {
                    Toast.makeText(this@MultiImageSelectorActivity, getString(R.string.msg_please_choose), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            button_done.visibility = View.GONE
        }

        button_category.setOnClickListener {
            mFolderPopupWindow?.let {

                if (it.isShowing) {
                    it.dismiss()
                } else {
                    it.showAsDropDown(toolbar)
                }
            }
        }

    }

    /**
     * Create popup ListView
     */
    private fun setupPopupFolder(context: Context) {
        val point = ScreenUtils.getScreenSize(context)
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_folder, null)
        mFolderPopupWindow = PopupWindow(popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)

        mFolderPopupWindow?.apply {
            //width = point.x
            //height =  (point.y * (4.5f / 8.0f)).toInt()
            //height = point.y
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(
                this@MultiImageSelectorActivity,
                R.color.bg_popup)))
        }

        mFolderAdapter = FolderAdapter(context)
        mFolderAdapter?.itemClick = {position, folder ->
            folderItemClick(position, folder)
        }

        popupView.recycler_view_folder.adapter = mFolderAdapter
    }

    /**
     * imageItem Click
     */
    private fun imageItemClick(position: Int, image: Image?) {

        mImageAdapter?.let {
            if (position == 0 && it.isShowCamera) {
                showCameraAction()
            } else {
                selectImage(position, image, mode)
            }
        }
    }

    private fun folderItemClick(position: Int, folder: Folder?) {
        mFolderAdapter?.setSelectIndex(position)

        Handler().postDelayed({
            mFolderPopupWindow?.dismiss()

            if (position == 0) {

                viewModel.notifyObserver()
                button_category.setText(R.string.folder_all)
                mImageAdapter?.isShowCamera = isShowCamera
            } else {
                if (null != folder) {
                    mImageAdapter?.setData(folder.images)
                    button_category.text = folder.name
                    if (resultList.size > 0) {
                        mImageAdapter?.setDefaultSelected(resultList)
                    }
                }
                mImageAdapter?.isShowCamera = false
            }

            recycler_view.smoothScrollToPosition(0)
        }, 100)

    }

    /**
     * Open camera
     */
    private fun showCameraAction() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getString(R.string.permission_rationale_write_storage),
                REQUEST_STORAGE_WRITE_ACCESS_PERMISSION
            )
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                try {
                    mTmpFile = FileUtils.createTmpFile(this)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mTmpFile?.let { tmpFile ->

                    if (tmpFile.exists()) {
                        val imgPathUri: Uri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imgPathUri =
                                FileProvider.getUriForFile(this, packageName + ".provider", tmpFile)
                        } else {
                            imgPathUri = Uri.fromFile(tmpFile)
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgPathUri)
                        startActivityForResult(intent,
                            REQUEST_CAMERA
                        )
                    } else {
                        Toast.makeText(this, R.string.error_image_not_exist, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun selectImage(position: Int, image: Image?, mode: Int) {
        if (image != null) {
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path)
                    onImageUnselected(image.path)
                } else {
                    if (mDefaultCount == resultList.size) {
                        Toast.makeText(this, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show()
                        return
                    }
                    resultList.add(image.path)
                    onImageSelected(image.path)
                }
                mImageAdapter?.select(position, image)
            } else if (mode == MODE_SINGLE) {
                onSingleImageSelected(image.path)
            }
        }
    }


    private fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, rationale, Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Update done button by select image data
     *
     * @param resultList selected image data
     */
    private fun updateDoneText(resultList: ArrayList<String>) {
        val size = resultList.size
        button_done.text = getString(R.string.action_button_string, getString(R.string.action_done), size, mDefaultCount)

    }

    private fun onSingleImageSelected(path: String) {
        val data = Intent()
        resultList.add(path)
        data.putStringArrayListExtra(EXTRA_RESULT, resultList)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun onImageSelected(path: String) {
        if (!resultList.contains(path)) {
            resultList.add(path)
        }
        updateDoneText(resultList)
    }

    private fun onImageUnselected(path: String) {
        if (resultList.contains(path)) {
            resultList.remove(path)
        }
        updateDoneText(resultList)
    }

    private fun onCameraResultUpdate(path: String, isLimitExceed: Boolean) {

        if (!isLimitExceed) {
            if (!resultList.contains(path)) {
                resultList.add(path)
            }
            updateDoneText(resultList)
        }

    }

    private fun onCameraShot(imageFile: File) {
        // notify system the image has change
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)))
        val data = Intent()
        resultList.add(imageFile.absolutePath)
        data.putStringArrayListExtra(EXTRA_RESULT, resultList)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    companion object {

        private const val REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110

        private const val REQUEST_CAMERA = 100

        // Single choice
        const val MODE_SINGLE = 0
        // Multi choice
        const val MODE_MULTI = 1

        const val MODE_CAMERA = 2

        /**
         * Max image size，int，[.DEFAULT_IMAGE_SIZE] by default
         */
        const val EXTRA_SELECT_COUNT = "max_select_count"

        /**
         * Min image size，int，[.DEFAULT_IMAGE_SIZE] by default
         */
        const val EXTRA_SELECT_MIN_COUNT = "min_select_count"
        /**
         * Select mode，[.MODE_MULTI] by default
         */
        const val EXTRA_SELECT_MODE = "select_count_mode"
        /**
         * Whether show camera，true by default
         */
        const val EXTRA_SHOW_CAMERA = "show_camera"
        /**
         * Result data set，ArrayList
         */
        const val EXTRA_RESULT = "select_result"
        /**
         * Original data set
         */
        const val EXTRA_DEFAULT_SELECTED_LIST = "default_list"
        // Default image size
        const val DEFAULT_IMAGE_SIZE = 9
        // Default min image size
        const val DEFAULT_MIN_IMAGE_SIZE = 1

    }
}

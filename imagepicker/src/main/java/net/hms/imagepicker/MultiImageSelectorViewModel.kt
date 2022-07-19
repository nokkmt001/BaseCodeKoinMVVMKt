package net.hms.imagepicker

import android.app.Application
import android.content.Context
import android.provider.MediaStore
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.hms.imagepicker.models.Folder
import net.hms.imagepicker.models.Image
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class MultiImageSelectorViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val ioContext = viewModelJob + Dispatchers.IO // background context
    //private val uiContext = viewModelJob + Dispatchers.Main // ui context
    private val ioScope = CoroutineScope(ioContext)
    //private val uiScope = CoroutineScope(uiContext)

    private var context: Context = application

    private var _listImage: MutableLiveData<ArrayList<Image>> = MutableLiveData()
    private var _listFolder: MutableLiveData<ArrayList<Folder>> = MutableLiveData()

    val listImage: LiveData<ArrayList<Image>> = _listImage
    val listFolder: LiveData<ArrayList<Folder>> = _listFolder

    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media._ID
    )

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getListImage() {

        viewModelJob = ioScope.launch {

            val images = ArrayList<Image>()
            val folders = ArrayList<Folder>()

            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val selection = IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? "
            val selectionArgs = arrayOf("image/jpeg", "image/png")
            val sortOrder = IMAGE_PROJECTION[2] + " DESC"

            val cursor = context.contentResolver.query(uri, IMAGE_PROJECTION, selection, selectionArgs, sortOrder)
            cursor?.let { data ->

                if (data.count > 0) {
                    data.moveToFirst()
                    do {
                        val path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                        val name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                        val dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                        if (!fileExist(path)) {
                            continue
                        }

                        var image: Image? = null
                        if (!TextUtils.isEmpty(name)) {
                            image = Image(path, name, dateTime)
                            images.add(image)
                        }

                        val folderFile = File(path).parentFile
                        if (folderFile != null && folderFile.exists()) {
                            val fp = folderFile.absolutePath
                            val f = getFolderByPath(fp, folders)

                            if (f == null) {
                                val folder = Folder()
                                folder.name = folderFile.name
                                folder.path = fp
                                folder.cover = image
                                val imageList = ArrayList<Image>()
                                image?.let { imageList.add(it) }
                                folder.images = imageList
                                folders.add(folder)
                            } else {
                                image?.let { f.images.add(it) }
                            }
                        }

                    } while (data.moveToNext())
                }

            }

            cursor?.close()

            _listImage.postValue(images)
            _listFolder.postValue(folders)

        }

    }

    private fun fileExist(path: String): Boolean {
        return if (!TextUtils.isEmpty(path)) {
            File(path).exists()
        } else false
    }

    private fun getFolderByPath(path: String, list: ArrayList<Folder>): Folder? {
        for (folder in list) {
            if (TextUtils.equals(folder.path, path)) {
                return folder
            }
        }
        return null
    }

    fun notifyObserver() {
        _listImage.value = _listImage.value
        _listFolder.value = _listFolder.value
    }

}
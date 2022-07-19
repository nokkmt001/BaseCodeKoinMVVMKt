package net.hms.imagepicker.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.text.TextUtils

import java.io.File
import java.io.IOException

import android.os.Environment.MEDIA_MOUNTED

object FileUtils {

    private const val JPEG_FILE_PREFIX = "IMG_"
    private const val JPEG_FILE_SUFFIX = ".jpg"

    private const val EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE"

    @Throws(IOException::class)
    fun createTmpFile(context: Context): File {
        var dir: File?
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            if (!dir!!.exists()) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera")
                if (!dir!!.exists()) {
                    dir = getCacheDirectory(context, true)
                }
            }
        } else {
            dir = getCacheDirectory(context, true)
        }
        return File.createTempFile(
            JPEG_FILE_PREFIX,
            JPEG_FILE_SUFFIX, dir)
    }

    @JvmOverloads
    fun getCacheDirectory(context: Context, preferExternal: Boolean = true): File {
        var appCacheDir: File? = null
        val externalStorageState: String = try {
            Environment.getExternalStorageState()
        } catch (e: Exception) {
            ""
        }

        if (preferExternal && MEDIA_MOUNTED == externalStorageState && hasExternalStoragePermission(
                context
            )
        ) {
            appCacheDir = getExternalCacheDir(context)
        }
        if (appCacheDir == null) {
            appCacheDir = context.cacheDir
        }
        if (appCacheDir == null) {
            val cacheDirPath = "/data/data/" + context.packageName + "/cache/"
            appCacheDir = File(cacheDirPath)
        }
        return appCacheDir
    }

    private fun getExternalCacheDir(context: Context): File? {
        val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
        val appCacheDir = File(File(dataDir, context.packageName), "cache")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null
            }
            try {
                File(appCacheDir, ".nomedia").createNewFile()
            } catch (e: IOException) {
            }

        }
        return appCacheDir
    }

    private fun hasExternalStoragePermission(context: Context): Boolean {
        val perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)
        return perm == PackageManager.PERMISSION_GRANTED
    }

}

package net.hms.imagepicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.hms.imagepicker.R
import net.hms.imagepicker.models.Folder
import java.io.File
import java.util.*

class FolderAdapter(private val mContext: Context) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    var itemClick: ((Int, Folder) -> Unit)? = null

    private var mFolders: MutableList<Folder> = ArrayList()
    private var lastSelected = 0

    private val totalImageSize: Int
        get() {
            var result = 0
            if (mFolders.size > 0) {
                for (f in mFolders) {
                    result += f.images.size
                }
            }
            return result
        }

    fun setData(folders: MutableList<Folder>?) {
        if (folders != null && folders.size > 0) {
            mFolders = folders
        } else {
            mFolders.clear()
        }
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val cover: ImageView = v.findViewById<View>(R.id.cover) as ImageView
        val name: TextView = v.findViewById<View>(R.id.name) as TextView
        val size: TextView = v.findViewById<View>(R.id.size) as TextView
        val indicator: ImageView = v.findViewById<View>(R.id.indicator) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_folder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = mFolders[holder.adapterPosition]
        Glide.with(mContext).load(File(folder.cover?.path)).error(R.drawable.default_error).centerCrop()
            .into(holder.cover)
        if (holder.adapterPosition == 0) {
            when {
                totalImageSize > 1 -> holder.size.text = String.format(mContext.resources.getString(R.string.photos_unit), totalImageSize)
                totalImageSize == 1 -> holder.size.text = String.format(mContext.resources.getString(R.string.photo_unit), totalImageSize)
                else -> holder.size.text = mContext.resources.getString(R.string.no_photo_unit)
            }
        } else {
            when {
                folder.images.size > 1 -> holder.size.text = String.format(
                    mContext.resources.getString(R.string.photos_unit),
                    mFolders[holder.adapterPosition].images.size
                )
                folder.images.size == 1 -> holder.size.text = String.format(
                    mContext.resources.getString(R.string.photo_unit),
                    mFolders[holder.adapterPosition].images.size
                )
                else -> holder.size.text = mContext.resources.getString(R.string.no_photo_unit)
            }
        }

        if (holder.adapterPosition == 0) {
            holder.name.setText(R.string.folder_all)
        } else {
            holder.name.text = folder.name
        }

        if (lastSelected == holder.adapterPosition) {
            holder.indicator.visibility = View.VISIBLE
        } else {
            holder.indicator.visibility = View.INVISIBLE
        }

        itemClick?.let {
            holder.itemView.setOnClickListener {
                itemClick?.invoke(holder.adapterPosition, folder)
            }
        }
    }

    override fun getItemCount(): Int {
        return mFolders.size
    }

    fun setSelectIndex(i: Int) {
        if (lastSelected == i) return

        lastSelected = i
        notifyDataSetChanged()
    }

}

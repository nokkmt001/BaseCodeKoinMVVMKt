package net.hms.imagepicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import net.hms.imagepicker.R
import net.hms.imagepicker.models.Image

import java.io.File
import java.util.ArrayList

class ImageGridAdapter(private val mContext: Context, showCamera: Boolean, column: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showCamera = true
    private var showSelectIndicator = true

    private var mImages: MutableList<Image> = ArrayList()
    private val mSelectedImages = ArrayList<Image>()

    var itemClick: ((Int, Image?) -> Unit)? = null

    var isShowCamera: Boolean
        get() = showCamera
        set(b) {
            if (showCamera == b) return

            showCamera = b
            notifyDataSetChanged()
        }

    init {
        this.showCamera = showCamera
    }

    fun showSelectIndicator(b: Boolean) {
        showSelectIndicator = b
    }

    fun select(position: Int, image: Image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image)
        } else {
            mSelectedImages.add(image)
        }
        notifyItemChanged(position)
    }


    fun setDefaultSelected(resultList: ArrayList<String>) {
        for (path in resultList) {
            val image = getImageByPath(path)
            if (image != null) {
                mSelectedImages.add(image)
            }
        }
        if (mSelectedImages.size > 0) {
            notifyDataSetChanged()
        }
    }

    private fun getImageByPath(path: String): Image? {
        if (mImages.size > 0) {
            for (image in mImages) {
                if (image.path.equals(path, ignoreCase = true)) {
                    return image
                }
            }
        }
        return null
    }


    fun setData(images: MutableList<Image>?) {
        mSelectedImages.clear()
        if (images != null && images.size > 0) {
            mImages = images
        } else {
            mImages.clear()
        }
        notifyDataSetChanged()
    }

    class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val image: ImageView = v.findViewById<View>(R.id.image) as ImageView
        val indicator: ImageView = v.findViewById<View>(R.id.checkmark) as ImageView
        val mask: View = v.findViewById(R.id.mask)
    }

    class CameraViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun getItemViewType(position: Int): Int {
        return if (showCamera) {
            if (position == 0) {
                TYPE_CAMERA
            } else {
                TYPE_NORMAL
            }
        } else {
            TYPE_NORMAL
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CAMERA) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_camera, parent, false)
            CameraViewHolder(view)
        } else {
            val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image, parent, false)
            ImageViewHolder(view)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_CAMERA -> {
                val cameraViewHolder = holder as CameraViewHolder
                itemClick?.let {
                    cameraViewHolder.itemView.setOnClickListener {
                        itemClick?.invoke(holder.adapterPosition, null)
                    }
                }
            }

            TYPE_NORMAL -> {
                val imageViewHolder = holder as ImageViewHolder
                val image = mImages[if (isShowCamera) holder.getAdapterPosition() - 1 else holder.getAdapterPosition()]
                if (showSelectIndicator) {
                    imageViewHolder.indicator.visibility = View.VISIBLE
                    if (mSelectedImages.contains(image)) {
                        imageViewHolder.indicator.setImageResource(R.drawable.btn_selected)
                        imageViewHolder.mask.visibility = View.VISIBLE
                    } else {
                        imageViewHolder.indicator.setImageResource(R.drawable.btn_unselected)
                        imageViewHolder.mask.visibility = View.GONE
                    }
                } else {
                    imageViewHolder.indicator.visibility = View.GONE
                }
                val imageFile = File(image.path)
                if (imageFile.exists()) {
                    Glide.with(mContext).load(imageFile).placeholder(R.drawable.default_error).centerCrop()
                        .into(imageViewHolder.image)
                } else {
                    imageViewHolder.image.setImageResource(R.drawable.default_error)
                }

                itemClick?.let {
                    imageViewHolder.itemView.setOnClickListener {
                        itemClick?.invoke(holder.adapterPosition, image)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (showCamera) mImages.size + 1 else mImages.size
    }

    companion object {

        private const val TYPE_CAMERA = 0
        private const val TYPE_NORMAL = 1
    }

}

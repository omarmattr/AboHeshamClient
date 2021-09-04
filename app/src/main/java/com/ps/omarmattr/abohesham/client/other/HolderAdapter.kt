package com.ps.omarmattr.abohesham.client.other

import android.annotation.SuppressLint
import android.graphics.Paint
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

object HolderAdapter {


    @JvmStatic
    @BindingAdapter("loadImageRec")
    fun loadImage(image:ImageView,img:Int){
        image.setImageResource(img)
    }
    @JvmStatic
    @BindingAdapter("loadImageUri")
    fun loadImageUri(image:ImageView,img: Uri){
        image.setImageURI(img)
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImagePath(image:ImageView,path:String?){
        if (!path.isNullOrEmpty())
        loadImage(image, path)
    }

    @JvmStatic
    @BindingAdapter("format_date")
    fun formatDate(textview: TextView,date:Long){
        textview.text = convertLongToTime(date)
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd  hh:mm a")
        return format.format(date)
    }

    @JvmStatic
    @BindingAdapter("paintFlags")
    fun paintFlags(textview:TextView,mtext:String){
        textview.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = mtext
        }

    }



    fun loadImage(view: ImageView,path:String){
        try {
            Glide
                .with(view.context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(view)
        } catch (e: Exception) {
        }
    }


}

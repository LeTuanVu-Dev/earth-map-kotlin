package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.earthmap.map.ltv.tracker.R

fun ImageView.loadThumbnail(
    context: Context, path: String, @DimenRes cornerRadiusDp: Int = 0
) {
    val options: RequestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .placeholder(R.drawable.ic_place_holder)
        .fallback(R.drawable.ic_place_holder)
        .error(R.drawable.ic_place_holder)
        .apply(RequestOptions().transform(CenterCrop()))
        .transform(CenterCrop(), RoundedCorners(cornerRadiusDp))

    Glide.with(context)
        .load(path)
        .apply(options)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

enum class Scale {
    FIT, FILL
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    src: Int,
    scaleType: Scale = Scale.FIT,
    placeholder: Int = R.drawable.ic_place_holder,
    error: Int = R.drawable.ic_place_holder
) {
    val requestOptions = RequestOptions().apply {
        when (scaleType) {
            Scale.FIT -> fitCenter()
            Scale.FILL -> centerCrop()
        }
    }

    Glide.with(this.context)
        .load(src)
        .apply(requestOptions)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

@SuppressLint("CheckResult")
fun preloadData(
    context: Context,
    src: String,
    id:String,
) {
    Glide.with(context)
        .load(GlideUrlCustomCacheKey(src,id))
        .preload()
}

fun loadImageWithConvertUrlForBitmap(
    context: Context,
    src: String,
    bitmapReady:(Bitmap)->Unit
) {
    Glide.with(context)
        .asBitmap()
        .load(src)
        .override(1024, 512)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
            ) {
                bitmapReady(resource)
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })

}

@SuppressLint("CheckResult")
fun ImageView.loadImageThumb(
    src: String,
    id:String,
    scaleType: Scale = Scale.FIT,
    placeholder: Int = R.drawable.ic_place_holder,
    error: Int = R.drawable.ic_place_holder
) {
    val requestOptions = RequestOptions().apply {
        when (scaleType) {
            Scale.FIT -> fitCenter()
            Scale.FILL -> centerCrop()
        }
    }

    Glide.with(this.context)
        .load(GlideUrlCustomCacheKey(src,id))
        .apply(requestOptions)
        .placeholder(placeholder)
        .error(error)
        .override(300,300)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

@SuppressLint("CheckResult")
fun ImageFilterView.loadImageThumb(
    src: String,
    id:String,
    placeholder: Int = R.drawable.ic_place_holder,
    error: Int = R.drawable.ic_place_holder
) {

    Glide.with(this.context)
        .load(GlideUrlCustomCacheKey(src,id))
        .placeholder(placeholder)
        .error(error)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

@SuppressLint("CheckResult")
fun ImageView.loadImageThumbHome(
    src: Int,
    scaleType: Scale = Scale.FIT,
    placeholder: Int = R.drawable.ic_place_holder,
    error: Int = R.drawable.ic_place_holder
) {
    val requestOptions = RequestOptions().apply {
        when (scaleType) {
            Scale.FIT -> fitCenter()
            Scale.FILL -> centerCrop()
        }
    }
    try {
        Glide.with(this.context)
            .load(src)
            .apply(requestOptions)
            .placeholder(placeholder)
            .error(error)
            .override(512,512)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }catch (e:Exception){
        e.printStackTrace()
    }
}



@SuppressLint("CheckResult")
fun ImageView.loadImage(
    src: String,
    id:String,
    scaleType: Scale = Scale.FIT,
    placeholder: Int = R.drawable.ic_place_holder,
    error: Int = R.drawable.ic_place_holder
) {
    val requestOptions = RequestOptions().apply {
        when (scaleType) {
            Scale.FIT -> fitCenter()
            Scale.FILL -> centerCrop()
        }
    }

    Glide.with(this.context)
        .load(GlideUrlCustomCacheKey(src,id))
        .apply(requestOptions)
        .placeholder(placeholder)
        .error(error)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

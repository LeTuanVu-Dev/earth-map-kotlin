package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import com.bumptech.glide.load.model.GlideUrl

class GlideUrlCustomCacheKey(
    url: String,
    private val idUrlCache: String
) : GlideUrl(url) {

    override fun getCacheKey(): String {
        return idUrlCache
    }
}

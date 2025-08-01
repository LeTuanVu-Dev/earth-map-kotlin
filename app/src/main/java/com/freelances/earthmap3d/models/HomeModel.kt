package com.freelances.earthmap3d.com.freelances.earthmap3d.models

import com.freelances.earthmap3d.R
import com.freelances.earthmap3d.extensions.utils.AIR_QUALITY
import com.freelances.earthmap3d.extensions.utils.CAMERA_360
import com.freelances.earthmap3d.extensions.utils.CAMERA_LIVE
import com.freelances.earthmap3d.extensions.utils.COMPASS
import com.freelances.earthmap3d.extensions.utils.EARTH_3D
import com.freelances.earthmap3d.extensions.utils.FAMOUS_PLACE
import com.freelances.earthmap3d.extensions.utils.TRAFFIC_MAP
import com.freelances.earthmap3d.extensions.utils.WORLD_CLOCK

data class HomeModel(
    val homeID: String,
    val icon: Int,
    val title: Int,
) {
    companion object {
        val listHome = listOf(
            HomeModel(
                homeID = CAMERA_360,
                icon = R.drawable.ic_camera_360,
                title = R.string.camera_360
            ),
            HomeModel(
                homeID = CAMERA_LIVE,
                icon = R.drawable.ic_camera_live,
                title = R.string.camera_live
            ),
            HomeModel(homeID = EARTH_3D, icon = R.drawable.ic_earth_3d, title = R.string.earth_3d),
            HomeModel(
                homeID = FAMOUS_PLACE,
                icon = R.drawable.ic_famous_place,
                title = R.string.famous_place
            ),
            HomeModel(
                homeID = TRAFFIC_MAP,
                icon = R.drawable.ic_traffic_map,
                title = R.string.traffic_map
            ),
            HomeModel(
                homeID = WORLD_CLOCK,
                icon = R.drawable.ic_world_clock,
                title = R.string.world_clock
            ),
            HomeModel(
                homeID = AIR_QUALITY,
                icon = R.drawable.ic_air_quality,
                title = R.string.air_quality
            ),
            HomeModel(homeID = COMPASS, icon = R.drawable.ic_compass, title = R.string.compass),
        )
    }
}

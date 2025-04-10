package com.wngud.pomosound.data.source

import com.wngud.pomosound.R
import com.wngud.pomosound.data.model.PlaceItemData

object PlaceDataSource {
    val places = listOf(
        PlaceItemData(1, "모닥불", R.drawable.ic_place_fire, R.raw.fire_bg),
        PlaceItemData(2, "비오는 방 안", R.drawable.ic_place_rain, R.raw.rain_bg),
        PlaceItemData(3, "도서관", R.drawable.ic_place_library, R.raw.library_bg),
        PlaceItemData(4, "숲 속", R.drawable.ic_place_forest, R.raw.forest_bg),
        PlaceItemData(5, "해변", R.drawable.ic_place_beach, R.raw.beach_bg),
        PlaceItemData(6, "계곡", R.drawable.ic_place_valley, R.raw.valley_bg),
        PlaceItemData(7, "바람 부는 들판", R.drawable.ic_place_field, R.raw.field_bg),
        PlaceItemData(8, "느리게 흘러가는 구름", R.drawable.ic_place_cloud, R.raw.cloud_bg),
        PlaceItemData(9, "카페 안", R.drawable.ic_place_cafe, R.raw.cafe_bg),
        PlaceItemData(10, "우주", R.drawable.ic_place_space, R.raw.space_bg),
        PlaceItemData(11, "강가의 자갈밭", R.drawable.ic_place_stone, R.raw.stone_bg),
        PlaceItemData(12, "떨어지는 물방울", R.drawable.ic_place_water_drop, R.raw.water_drop_bg),
        PlaceItemData(13, "도시의 야경", R.drawable.ic_place_nightscape, R.raw.night_bg),
        PlaceItemData(14, "공원", R.drawable.ic_place_park, R.raw.park_bg),
        PlaceItemData(15, "고요히 내리는 눈", R.drawable.ic_place_snow, R.raw.snow_bg),
        PlaceItemData(16, "바다속", R.drawable.ic_place_deep_sea, R.raw.deep_sea_bg),
        PlaceItemData(17, "폭포", R.drawable.ic_place_waterfall, R.raw.waterfall_bg),
        PlaceItemData(18, "사막", R.drawable.ic_place_desert, R.raw.desert_bg),
        PlaceItemData(19, "비행", R.drawable.ic_place_fly, R.raw.fly_bg),
        PlaceItemData(20, "꽃", R.drawable.ic_place_flower, R.raw.flower_bg),
    )
}
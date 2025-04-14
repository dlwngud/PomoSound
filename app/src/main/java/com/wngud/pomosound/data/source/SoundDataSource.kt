package com.wngud.pomosound.data.source

import com.wngud.pomosound.R
import com.wngud.pomosound.data.model.SoundCategory
import com.wngud.pomosound.data.model.SoundData

object SoundDataSource {
    val soundCategories = listOf(
        SoundCategory(
            name = "자연",
            sounds = listOf(
                SoundData(1, "비", R.drawable.ic_rain, R.raw.rain),
                SoundData(2, "천둥", R.drawable.ic_thunder, R.raw.thunder),
                SoundData(3, "파도", R.drawable.ic_sea_waves, R.raw.wave),
                SoundData(4, "새", R.drawable.ic_bird, R.raw.bird),
                SoundData(5, "계곡", R.drawable.ic_valley, R.raw.valley),
                SoundData(6, "바람", R.drawable.ic_wind, R.raw.soft_wind),
                SoundData(7, "풀벌레", R.drawable.ic_bug, R.raw.bug),
                SoundData(8, "모닥불", R.drawable.ic_fire, R.raw.fire),
                SoundData(9, "낙엽", R.drawable.ic_leaf, R.raw.fallen_leaves),
                SoundData(10, "물방울", R.drawable.ic_water_drop, R.raw.water_drop),
                SoundData(11, "눈", R.drawable.ic_snow, R.raw.snow),
            )
        ),
        SoundCategory(
            name = "일상",
            sounds = listOf(
                SoundData(12, "책", R.drawable.ic_book, R.raw.book),
                SoundData(13, "선풍기", R.drawable.ic_pan, R.raw.fan),
                SoundData(14, "커피 머신", R.drawable.ic_cafe, R.raw.coffee),
                SoundData(15, "시계", R.drawable.ic_clock, R.raw.clock),
                SoundData(16, "연필", R.drawable.ic_pencil, R.raw.pencil),
                SoundData(17, "키보드", R.drawable.ic_keyboard, R.raw.keyboard),
            )
        ),
        SoundCategory(
            name = "도시",
            sounds = listOf(
                SoundData(18, "웅성거림", R.drawable.ic_noisily, R.raw.conversation),
                SoundData(19, "자동차", R.drawable.ic_car, R.raw.car),
                SoundData(20, "기차", R.drawable.ic_train, R.raw.train),
                SoundData(21, "공사", R.drawable.ic_build, R.raw.roadworks),
                SoundData(22, "비행기", R.drawable.ic_plane, R.raw.airplane),
            )
        ),
    )
}
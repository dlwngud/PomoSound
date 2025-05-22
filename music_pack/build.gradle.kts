plugins {
    id("com.android.asset-pack")
}

assetPack {
    packName.set("music_pack")
    dynamicDelivery {
        deliveryType.set("on-demand")
    }
}
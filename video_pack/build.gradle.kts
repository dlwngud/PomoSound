plugins {
    id("com.android.asset-pack")
}

assetPack {
    packName.set("video_pack")
    dynamicDelivery {
        deliveryType.set("on-demand")
    }
}
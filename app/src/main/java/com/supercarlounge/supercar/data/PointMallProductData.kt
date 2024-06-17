package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class PointMallProductData

    (var goodsNo:String?,
     var goodsCode:String?,
    var rmIdBuyCntFlagCd:String?,
    var endDate:String?,
    var saleDiscountPrice:String?,
    var discountPrice:String?,
    var mmsGoodsImg:String?,
     var limitDay:String?,
     var content:String?,
     var goodsImgB:String?,
     var goodsComName:String?,
     var goodsName:String?,
     var goodsComId:String?,
     var brandName:String?,
     var realPrice:String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(goodsNo)
        parcel.writeString(goodsCode)
        parcel.writeString(rmIdBuyCntFlagCd)
        parcel.writeString(endDate)
        parcel.writeString(saleDiscountPrice)
        parcel.writeString(discountPrice)
        parcel.writeString(mmsGoodsImg)
        parcel.writeString(limitDay)
        parcel.writeString(content)
        parcel.writeString(goodsImgB)
        parcel.writeString(goodsComName)
        parcel.writeString(goodsName)
        parcel.writeString(goodsComId)
        parcel.writeString(brandName)
        parcel.writeString(realPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PointMallProductData> {
        override fun createFromParcel(parcel: Parcel): PointMallProductData {
            return PointMallProductData(parcel)
        }

        override fun newArray(size: Int): Array<PointMallProductData?> {
            return arrayOfNulls(size)
        }
    }

}

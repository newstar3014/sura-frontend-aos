package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class UserInformationCarData(
    var carb_image: String?,
    var carb_name: String?,
    var carm_name: String?,
    var csm_name: String?,
    var csm_seq: String?,
    val u_seq: Int,
    var uc_brand: String?,
    var uc_car_main: String?,
    var uc_check: String?,
    val uc_contract_url: String?,
    val uc_date: String?,
    val uc_insur_date: String?,
    var uc_insur_url: String?,
    var uc_model: String?,
    var uc_return: String?,
    var uc_seq: Int,
    var uc_car_main_yn: String?,
    val uc_contract_url_yn: String?,
    val uc_insur_url_yn: String?,
    val uc_income_yn: String?,
    val uc_income: String?,
    var uc_before_url: String?,
    var new_url: String?,
    var uc_car_main_temp: String?,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
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
        parcel.writeString(carb_image)
        parcel.writeString(carb_name)
        parcel.writeString(carm_name)
        parcel.writeString(csm_name)
        parcel.writeString(csm_seq)
        parcel.writeInt(u_seq)
        parcel.writeString(uc_brand)
        parcel.writeString(uc_car_main)
        parcel.writeString(uc_check)
        parcel.writeString(uc_contract_url)
        parcel.writeString(uc_date)
        parcel.writeString(uc_insur_date)
        parcel.writeString(uc_insur_url)
        parcel.writeString(uc_model)
        parcel.writeString(uc_return)
        parcel.writeInt(uc_seq)
        parcel.writeString(uc_car_main_yn)
        parcel.writeString(uc_contract_url_yn)
        parcel.writeString(uc_insur_url_yn)
        parcel.writeString(uc_income_yn)
        parcel.writeString(uc_income)
        parcel.writeString(uc_before_url)
        parcel.writeString(new_url)
        parcel.writeString(uc_car_main_temp)

    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInformationCarData> {
        override fun createFromParcel(parcel: Parcel): UserInformationCarData {
            return UserInformationCarData(parcel)
        }

        override fun newArray(size: Int): Array<UserInformationCarData?> {
            return arrayOfNulls(size)
        }
    }
}
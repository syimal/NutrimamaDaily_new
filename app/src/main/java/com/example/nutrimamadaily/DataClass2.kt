package com.example.nutrimamadaily

import android.media.Image
import android.os.Parcel
import android.os.Parcelable

data class DataClass2(var dataImage:Int, var dataTitle:String, var dataDesc: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dataImage)
        parcel.writeString(dataTitle)
        parcel.writeString(dataDesc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataClass2> {
        override fun createFromParcel(parcel: Parcel): DataClass2 {
            return DataClass2(parcel)
        }

        override fun newArray(size: Int): Array<DataClass2?> {
            return arrayOfNulls(size)
        }
    }
}

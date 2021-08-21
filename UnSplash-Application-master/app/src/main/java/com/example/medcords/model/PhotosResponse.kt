package com.example.medcords.model

import android.os.Parcel
import android.os.Parcelable

data class PhotosResponse(
    val results: MutableList<Result>,
    val total: Int,
    val total_pages: Int
) :Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("results"),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<PhotosResponse> {
        override fun createFromParcel(parcel: Parcel): PhotosResponse {
            return PhotosResponse(parcel)
        }

        override fun newArray(size: Int): Array<PhotosResponse?> {
            return arrayOfNulls(size)
        }
    }
}
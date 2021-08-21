package com.example.medcords.model

import android.os.Parcel
import android.os.Parcelable

data class Result(
    val alt_description: String?,
    val categories: List<Any>,
    val color: String?,
    val created_at: String?,
    val current_user_collections: List<Any>,
    val description: String?,
    val height: Int,
    val id: String?,
    val liked_by_user: Boolean,
    val likes: Int,
    val links: Links,
    val promoted_at: Any,
    val sponsorship: Any,
    val tags: List<Tag>,
    val updated_at: String?,
    val urls: UrlsX,
    val user: UserX,
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        TODO("categories"),
        parcel.readString(),
        parcel.readString(),
        TODO("current_user_collections"),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        TODO("links"),
        TODO("promoted_at"),
        TODO("sponsorship"),
        TODO("tags"),
        parcel.readString(),
        TODO("urls"),
        TODO("user"),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(alt_description)
        parcel.writeString(color)
        parcel.writeString(created_at)
        parcel.writeString(description)
        parcel.writeInt(height)
        parcel.writeString(id)
        parcel.writeByte(if (liked_by_user) 1 else 0)
        parcel.writeInt(likes)
        parcel.writeString(updated_at)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}
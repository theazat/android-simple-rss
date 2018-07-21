package com.lelloman.read.persistence.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.lelloman.read.core.ModelWithId
import com.lelloman.read.utils.Constants.ARTICLE_TABLE_NAME

@Entity(tableName = ARTICLE_TABLE_NAME, foreignKeys = [ForeignKey(
    entity = Source::class,
    parentColumns = ["id"],
    childColumns = ["sourceId"],
    onDelete = CASCADE
)])
data class Article(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    val title: String,
    val subtitle: String,
    val content: String,
    val link: String,
    val imageUrl: String?,
    val time: Long,
    val sourceId: Long
) : ModelWithId, Parcelable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(title)
        writeString(subtitle)
        writeString(content)
        writeString(link)
        writeString(imageUrl)
        writeLong(time)
        writeLong(sourceId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
            override fun createFromParcel(source: Parcel): Article = Article(source)
            override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
        }
    }
}
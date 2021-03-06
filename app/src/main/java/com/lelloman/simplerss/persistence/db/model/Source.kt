package com.lelloman.simplerss.persistence.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.SOURCE_TABLE_NAME

@Suppress("ArrayInDataClass")
@Entity(tableName = SOURCE_TABLE_NAME)
data class Source(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val name: String,
    val url: String,
    var lastFetched: Long = 0L,
    val isActive: Boolean,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var favicon: ByteArray? = null,
    val immutableHashCode: Int = name.hashCode() * url.hashCode()
) : ModelWithId<Long>
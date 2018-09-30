package com.lelloman.launcher.packages

import android.graphics.drawable.Drawable
import com.lelloman.common.utils.model.ModelWithId

class Package(
    override val id: Long,
    val label: CharSequence,
    val packageName: String,
    val drawable: Drawable
) : ModelWithId
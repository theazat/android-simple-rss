package com.lelloman.launcher.ui.launches

import android.graphics.drawable.Drawable
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.launcher.persistence.model.PackageLaunch

class PackageLaunchListItem(
    val packageLaunch: PackageLaunch,
    val icon: Drawable
) : ModelWithId by packageLaunch
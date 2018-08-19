package com.lelloman.read.core.navigation

open class ScreenNavigationEvent(
    val screen: NavigationScreen,
    val args: Array<Any> = emptyArray()
) : NavigationEvent
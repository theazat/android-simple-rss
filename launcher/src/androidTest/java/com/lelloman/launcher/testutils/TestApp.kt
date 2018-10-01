package com.lelloman.launcher.testutils

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.launcher.LauncherApplication
import com.lelloman.launcher.di.DaggerAppComponent
import com.lelloman.launcher.di.LauncherApplicationModule
import com.lelloman.launcher.di.ViewModelModule
import com.lelloman.launcher.packages.PackagesModule

class TestApp : LauncherApplication() {

    var baseApplicationModule = BaseApplicationModule(this)
    var baseSettingsModule = BaseSettingsModule()
    var launcherApplicationModule = LauncherApplicationModule()
    var packagesModule = PackagesModule()
    var viewModelModule = ViewModelModule()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(baseApplicationModule)
        .baseSettingsModule(baseSettingsModule)
        .launcherApplicationModule(launcherApplicationModule)
        .packagesModule(packagesModule)
        .viewModelModule(viewModelModule)
        .build()
        .inject(this)

    companion object {
        private lateinit var instance: TestApp

        fun dependenciesUpdate(action: (TestApp) -> Unit) {
            action.invoke(instance)
            instance.inject()
        }

        fun resetPersistence() {

        }
    }
}
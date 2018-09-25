package com.lelloman.read.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.read.feed.FeedRefreshBroadcastReceiver
import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.discover.view.DiscoverUrlActivity
import com.lelloman.read.ui.discover.view.FoundFeedListActivity
import com.lelloman.read.ui.launcher.view.LauncherActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributeArticlesListActivity(): ArticlesListActivity

    @ContributesAndroidInjector
    fun contributeSourcesListActivity(): SourcesListActivity

    @ContributesAndroidInjector
    fun contributeAddSourceActivity(): AddSourceActivity

    @ContributesAndroidInjector
    fun contributeSourceActivity(): SourceActivity

    @ContributesAndroidInjector
    fun contributeArticleActivity(): ArticleActivity

    @ContributesAndroidInjector
    fun contributeFeedRefreshReceiver(): FeedRefreshBroadcastReceiver

    @ContributesAndroidInjector
    fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    fun contributeWalkthroughActivity(): WalkthroughActivity

    @ContributesAndroidInjector
    fun contributeLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector
    fun contributeFoundFeedListActivity(): FoundFeedListActivity

    @ContributesAndroidInjector
    fun contributeDiscoverUrlActivity(): DiscoverUrlActivity
}
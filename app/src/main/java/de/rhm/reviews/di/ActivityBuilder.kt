package de.rhm.reviews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.rhm.reviews.ReviewListActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindReviewListActivity(): ReviewListActivity

}
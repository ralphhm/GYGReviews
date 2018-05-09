package de.rhm.reviews.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.rhm.reviews.ReviewListActivity
import de.rhm.reviews.review.SubmitReviewActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindReviewListActivity(): ReviewListActivity

    @ContributesAndroidInjector
    abstract fun bindSubmitReviewActivity(): SubmitReviewActivity

}
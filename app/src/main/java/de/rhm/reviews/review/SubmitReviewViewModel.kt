package de.rhm.reviews.review

import android.arch.lifecycle.ViewModel
import de.rhm.reviews.ReviewRepository
import de.rhm.reviews.api.model.Review
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SubmitReviewViewModel @Inject constructor(private val reviewRepo: ReviewRepository): ViewModel() {

    fun submitReview(review: Review): Completable = reviewRepo.submitReview(review).observeOn(AndroidSchedulers.mainThread())

}


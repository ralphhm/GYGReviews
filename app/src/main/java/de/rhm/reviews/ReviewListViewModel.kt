package de.rhm.reviews

import android.arch.lifecycle.ViewModel
import de.rhm.reviews.api.model.Review
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ReviewListViewModel @Inject constructor(reviewRepo: ReviewRepository): ViewModel() {

    private val uiActions = PublishSubject.create<FetchReviewsAction>()
    val uiStates: Observable<out ReviewsUiState> = uiActions
            //trigger action to fetch reviews at ViewModel creation
            .startWith(FetchReviewsAction)
            .switchMap {
                reviewRepo.getReviews().toObservable()
                        .map<ReviewsUiState> { Result(it) }
                        //emit loading ui state before every async fetch call
                        .startWith(Loading)
                        .onErrorReturn { Failure(it, {uiActions.onNext(FetchReviewsAction)}) }
            }
            //cache last emitted ui state to preserve state on orientation change
            .replay(1)
            .autoConnect()
            .observeOn(AndroidSchedulers.mainThread())

}

object FetchReviewsAction

sealed class ReviewsUiState
object Loading : ReviewsUiState()
data class Failure(val cause: Throwable, val retryAction: () -> Unit) : ReviewsUiState()
data class Result(val reviews: List<Review>) : ReviewsUiState()

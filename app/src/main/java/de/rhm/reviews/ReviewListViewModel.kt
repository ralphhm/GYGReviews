package de.rhm.reviews

import de.rhm.reviews.api.model.Review
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class ReviewListViewModel(reviewRepo: ReviewRepository) {

    val uiActions = PublishSubject.create<RequestListAction>()
    val uiStates: Observable<out ReviewsUiState> = uiActions
            //trigger action to refresh review list at ViewModel creation time
            .startWith(RequestListAction)
            .switchMap {
                reviewRepo.getReviews().toObservable()
                        .map<ReviewsUiState> { Result(it) }
                        //emit loading ui state before every async fetch call
                        .startWith(Loading)
                        .onErrorReturn { Failure(it) }
            }
            //cache the last emited ui state for resubscription
            .replay(1)
            .autoConnect()
            .observeOn(AndroidSchedulers.mainThread())

}

object RequestListAction

sealed class ReviewsUiState
object Loading : ReviewsUiState()
data class Failure(val cause: Throwable) : ReviewsUiState()
data class Result(val reviews: List<Review>) : ReviewsUiState()

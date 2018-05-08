package de.rhm.reviews

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.rhm.reviews.api.model.Review
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.BeforeClass
import org.junit.Test

class ReviewListViewModelTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }
    }

    val uiStatesObserver = TestObserver<ReviewsUiState>()

    @Test
    fun initialSubscription_returnsLoadingState() {
        mock<ReviewRepository> {
            on { getReviews() } doReturn Single.never()
        }.let { ReviewListViewModel(it) }.uiStates.subscribe(uiStatesObserver)
        uiStatesObserver.assertValue(Loading)
    }

    @Test
    fun uiStates_emitsFailureState_whenException() {
        val exception = Exception()
        mock<ReviewRepository> {
            on { getReviews() } doReturn Single.error(exception)
        }.let { ReviewListViewModel(it) }.uiStates.subscribe(uiStatesObserver)
        uiStatesObserver.assertValues(Loading, Failure(exception))
    }

    @Test
    fun uiStates_triggersResultState_whenResult() {
        val reviews = listOf(Review())
        mock<ReviewRepository> {
            on { getReviews() } doReturn Single.just(reviews)
        }.let { ReviewListViewModel(it) }.uiStates.subscribe(uiStatesObserver)
        uiStatesObserver.assertValues(Loading, Result(reviews))
    }

    @Test
    fun subscription_emitsLastState_whenResubscribed() {
        val reviews = listOf(Review())
        mock<ReviewRepository> {
            on { getReviews() } doReturn Single.just(reviews)
        }.let { ReviewListViewModel(it) }.apply {
            uiStates.subscribe()
            uiStates.subscribe(uiStatesObserver)
        }
        uiStatesObserver.assertValue(Result(reviews))
    }

}
package de.rhm.reviews

import android.util.Log
import de.rhm.reviews.api.GetyourguideService
import de.rhm.reviews.api.model.Review
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "ReviewRepository"

@Singleton
class ReviewRepository @Inject constructor(private val service: GetyourguideService) {

    fun getReviews(): Single<List<Review>> = service.getReviews().map { it.reviews }.doOnError { Log.e(TAG, "Error fetching reviews", it) }

    fun submitReview(review: Review): Completable = Completable.timer(5, TimeUnit.SECONDS)

}

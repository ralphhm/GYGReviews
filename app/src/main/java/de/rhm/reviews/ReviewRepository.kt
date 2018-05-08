package de.rhm.reviews

import android.util.Log
import de.rhm.reviews.api.GetyourguideService
import de.rhm.reviews.api.model.Review
import io.reactivex.Single

const val TAG = "ReviewRepository"

class ReviewRepository(val service: GetyourguideService) {

    fun getReviews(): Single<List<Review>> = service.getReviews().map { it.reviews }.doOnError { Log.e(TAG, "Error fetching reviews", it) }

}

package de.rhm.reviews

import de.rhm.reviews.api.model.Review
import io.reactivex.Single

interface ReviewRepository {

    fun getReviews(): Single<List<Review>>

}

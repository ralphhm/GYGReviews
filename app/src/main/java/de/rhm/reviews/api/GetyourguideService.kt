package de.rhm.reviews.api

import de.rhm.reviews.api.model.ReviewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface GetyourguideService {

    @Headers("User-Agent: Something_without_slash")
    @GET("berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json?sortBy=date_of_review&direction=DESC")
    fun getReviews(): Single<ReviewsResponse>

}
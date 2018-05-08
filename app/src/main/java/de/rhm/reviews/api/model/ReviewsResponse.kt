package de.rhm.reviews.api.model

import com.google.gson.annotations.SerializedName

class ReviewsResponse(@SerializedName("data") val reviews: List<Review>)
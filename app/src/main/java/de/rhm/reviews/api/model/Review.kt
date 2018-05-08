package de.rhm.reviews.api.model

import com.google.gson.annotations.SerializedName

class Review(@SerializedName("reviewerName") val author: String,
             @SerializedName("title") val title: String,
             @SerializedName("message") val message: String,
             @SerializedName("date") val date: String,
             @SerializedName("rating") val rating: Float)

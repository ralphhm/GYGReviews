package de.rhm.reviews

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import de.rhm.reviews.api.GetyourguideService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_review_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ReviewListActivity : AppCompatActivity() {

    val service = Retrofit.Builder()
            .baseUrl("http://www.getyourguide.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build().create(GetyourguideService::class.java)
    val viewModel = ReviewListViewModel(ReviewRepository(service))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)
        setSupportActionBar(toolbar)
        viewModel.uiStates.subscribe { bind(it) }
    }

    fun bind(uiState: ReviewsUiState): Unit = when (uiState) {
        Loading -> Snackbar.make(window.decorView, "Loading", Snackbar.LENGTH_LONG).show()
        is Failure -> Snackbar.make(window.decorView, "Error ${uiState.cause.localizedMessage}", Snackbar.LENGTH_LONG).show()
        is Result -> Snackbar.make(window.decorView, "Result ${uiState.reviews.size}", Snackbar.LENGTH_LONG).show()
    }

}

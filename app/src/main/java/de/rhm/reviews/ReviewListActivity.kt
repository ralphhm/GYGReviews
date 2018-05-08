package de.rhm.reviews

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import de.rhm.reviews.api.GetyourguideService
import de.rhm.reviews.api.model.Review
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_review_list.*
import kotlinx.android.synthetic.main.content_review_list.*
import kotlinx.android.synthetic.main.item_review.*
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
    val section = Section()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)
        setSupportActionBar(toolbar)
        content.adapter = GroupAdapter<ViewHolder>().apply {
            add(section)
        }
        viewModel.uiStates.subscribe { bind(it) }
    }

    fun bind(uiState: ReviewsUiState): Unit = when (uiState) {
        Loading -> Snackbar.make(window.decorView, "Loading", Snackbar.LENGTH_LONG).show()
        is Failure -> Snackbar.make(window.decorView, "Error ${uiState.cause.localizedMessage}", Snackbar.LENGTH_LONG).show()
        is Result -> section.update(uiState.reviews.map { ReviewItem(it) })
    }

}

class ReviewItem(private val review: Review): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) = with(viewHolder) {
        author.text = review.author
        rating.rating = review.rating
        title.text = review.title
        message.text = review.message
        date.text = review.date
    }

    override fun getLayout() = R.layout.item_review

}

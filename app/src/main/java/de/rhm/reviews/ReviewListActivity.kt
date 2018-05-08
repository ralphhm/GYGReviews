package de.rhm.reviews

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.AndroidInjection
import de.rhm.reviews.api.model.Review
import kotlinx.android.synthetic.main.activity_review_list.*
import kotlinx.android.synthetic.main.content_review_list.*
import kotlinx.android.synthetic.main.item_review.*
import javax.inject.Inject

class ReviewListActivity : AppCompatActivity() {

    @Inject
    lateinit var repo: ReviewRepository
    private val section = Section()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)
        setSupportActionBar(toolbar)
        content.adapter = GroupAdapter<ViewHolder>().apply {
            add(section)
        }
        ReviewListViewModel(repo).uiStates.subscribe { bind(it) }
    }

    fun bind(uiState: ReviewsUiState): Unit = when (uiState) {
        Loading -> section.update(listOf(LoadingItem))
        is Failure -> section.update(listOf(ErrorItem))
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

object LoadingItem: Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) = Unit
    override fun getLayout() = R.layout.item_loading
}

object ErrorItem: Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) = Unit
    override fun getLayout() = R.layout.item_error
}

package de.rhm.reviews

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.AndroidInjection
import de.rhm.reviews.api.model.Review
import de.rhm.reviews.di.TypedViewModelFactory
import de.rhm.reviews.review.SubmitReviewActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_review_list.*
import kotlinx.android.synthetic.main.content_review_list.*
import kotlinx.android.synthetic.main.item_review.*
import javax.inject.Inject

class ReviewListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewHolderFactory: TypedViewModelFactory<ReviewListViewModel>
    private lateinit var reviewListViewModel: ReviewListViewModel
    private val section = Section()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)
        setSupportActionBar(toolbar)
        content.adapter = GroupAdapter<ViewHolder>().apply {
            add(section)
        }
        reviewListViewModel = ViewModelProviders.of(this, viewHolderFactory).get(ReviewListViewModel::class.java).apply {
            uiStates.subscribe { bind(it) }.let { disposable.add(it) }
        }
        fab.setOnClickListener { startActivity(Intent(this, SubmitReviewActivity::class.java)) }
    }

    private fun bind(uiState: ReviewsUiState): Unit = when (uiState) {
        Loading -> section.update(listOf(LoadingItem))
        is Failure -> section.update(listOf(ErrorItem(uiState.retryAction)))
        is Result -> {
            section.update(uiState.reviews.map { ReviewItem(it) })
            content.scrollToPosition(uiState.scrollPosition)
            content.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) uiState.scrollPosition = recyclerView.scrollPosition
                }
            })
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

}

class ReviewItem(private val review: Review) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) = with(viewHolder) {
        author.text = review.author
        rating.rating = review.rating
        title.text = review.title
        message.text = review.message
        date.text = review.date
    }

    override fun getLayout() = R.layout.item_review

}

object LoadingItem : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) = Unit
    override fun getLayout() = R.layout.item_loading
}

class ErrorItem(private val retryAction: () -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) = viewHolder.itemView.setOnClickListener { retryAction.invoke() }
    override fun getLayout() = R.layout.item_error
}

val RecyclerView.scrollPosition get() = (layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: 0

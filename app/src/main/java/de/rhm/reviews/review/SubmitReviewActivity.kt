package de.rhm.reviews.review

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import dagger.android.AndroidInjection
import de.rhm.reviews.R
import de.rhm.reviews.api.model.Review
import de.rhm.reviews.di.TypedViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_submit_review.*
import kotlinx.android.synthetic.main.content_submit_review.*
import javax.inject.Inject

class SubmitReviewActivity : AppCompatActivity() {

    @Inject
    lateinit var viewHolderFactory: TypedViewModelFactory<SubmitReviewViewModel>
    private lateinit var submitReviewViewModel: SubmitReviewViewModel
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        submitReviewViewModel = ViewModelProviders.of(this, viewHolderFactory).get(SubmitReviewViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_submit_review, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_submit) {
            submitReviewViewModel.submitReview(Review(name.getString(), summary.getString(), text.getString(), "", rating.rating))
                    .subscribe { Snackbar.make(toolbar, "Submitted", Snackbar.LENGTH_LONG).show() }
                    .let { disposable.add(it) }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}

private fun EditText.getString() = text.toString()

package com.example.paulina.assignmentapplication.recipes.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.paulina.assignmentapplication.MainModule
import com.example.paulina.assignmentapplication.R
import com.example.paulina.assignmentapplication.recipes.adapter.RecipesListAdapter
import com.example.paulina.assignmentapplication.recipes.contract.RecipeContract
import com.example.paulina.assignmentapplication.recipes.di.RecipesModule
import com.example.paulina.assignmentapplication.recipes.presenter.RecipePresenter
import com.example.paulina.assignmentapplication.recipes.provider.RecipesRealmProvider
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity(), RecipeContract.View {

    val TAG = MainActivity::class.java.simpleName

    @Inject
    lateinit var presenter: RecipePresenter
    @Inject
    lateinit var adapter: RecipesListAdapter

    private var searchDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupInjection()
        presenter.attachView(this)
        presenter.getRecipes()
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribe(searchDisposable)
        presenter.detachView()
    }

    private fun setupInjection() {
        MainModule
                .mainComponent
                .plusRecipesListComponent(RecipesModule(this))
                .inject(this)
    }

    override fun setAdapterWithData(recipeList: List<RealmRecipe>) {
        recipes_list_recycler.setHasFixedSize(true)
        recipes_list_recycler.isLayoutFrozen = true
        recipes_list_recycler.layoutManager = LinearLayoutManager(applicationContext)
        adapter.setRecipesList(recipeList)
        recipes_list_recycler.adapter = adapter
        showContent()
        initSearch()
    }


    override fun showError(errorMessage: String) {
        message_tv.text = errorMessage
        recipes_list_recycler.visibility = View.INVISIBLE
        recipes_list_progress_bar.visibility = View.INVISIBLE
        recipes_list_search_edit_text.visibility = View.INVISIBLE
        message_tv.visibility = View.VISIBLE

    }

    override fun showLoader(show: Boolean) {
        recipes_list_recycler.visibility = View.INVISIBLE
        message_tv.visibility = View.INVISIBLE
        recipes_list_search_edit_text.visibility = View.INVISIBLE
        recipes_list_progress_bar.visibility = View.VISIBLE
    }

    fun showContent() {
        message_tv.visibility = View.INVISIBLE
        recipes_list_progress_bar.visibility = View.INVISIBLE
        recipes_list_recycler.visibility = View.VISIBLE
        recipes_list_search_edit_text.visibility = View.VISIBLE


    }


    private fun initSearch() {
        searchDisposable = recipes_list_search_edit_text
                .textChanges()
                .skip(1)
                .map { it.toString() }
                .doOnNext { showLoader(true) }
                .flatMap {
                    if (it.isNotBlank()) {
                        Log.d(TAG, it)
                        presenter.searchRecipes(it).subscribeOn(Schedulers.io())
                    } else {
                        Observable.just(null).subscribeOn(Schedulers.io())
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach { showContent() }
                .doOnError { Log.d(TAG, "error "); }
                .retry()
                .subscribe({
                    if (it != null){
                        recipes_list_search_edit_text.requestFocus()
                        adapter.updateRecipes(it.recipes!!)
                    }
                    else showError("No data")
                }, {
                    Log.e(TAG, "error while subscribe")
                })



    }

    fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }


}

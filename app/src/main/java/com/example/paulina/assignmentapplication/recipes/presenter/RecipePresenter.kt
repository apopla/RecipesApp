package com.example.paulina.assignmentapplication.recipes.presenter

import android.util.Log
import com.example.paulina.assignmentapplication.recipes.contract.RecipeContract
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.repository.RecipeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Paulina on 2018-03-08.
 */


class RecipePresenter @Inject constructor() : RecipeContract.Presenter {

    private var view: RecipeContract.View? = null
    @Inject
    lateinit var repository: RecipeRepository

    private var recipesDisposable: Disposable? = null

    override fun detachView() {
        unsubscribe(recipesDisposable)
        this.view = null
    }

    override fun attachView(view: RecipeContract.View) {
        this.view = view
    }

    override fun searchRecipes(fraze: String): List<Recipe> {
        Log.d("Presenter", fraze)
      return repository.getRecipesFromDbByFraze(fraze)
    }


    override fun getRecipes(){
       repository.getRecipse()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(
                       {view?.setAdapterWithData(it)},
                       { handleError(it) }
               )

    }


    fun handleError(cause: Throwable) {
        view?.showError(cause.localizedMessage)
    }

    fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }


}
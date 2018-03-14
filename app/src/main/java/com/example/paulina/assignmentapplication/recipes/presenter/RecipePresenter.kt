package com.example.paulina.assignmentapplication.recipes.presenter

import android.util.Log
import com.example.paulina.assignmentapplication.recipes.contract.RecipeContract
import com.example.paulina.assignmentapplication.recipes.provider.RecipesProvider
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipes
import io.reactivex.Observable
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
    lateinit var provider: RecipesProvider

    private var recipesDisposable: Disposable? = null

    override fun detachView() {
        unsubscribe(recipesDisposable)
        this.view = null
    }

    override fun attachView(view: RecipeContract.View) {
        this.view = view
    }

    override fun searchRecipes(fraze: String): List<RealmRecipe> {
        Log.d("Presenter", fraze)
        return provider.getRecipesByFrazeFromDb(fraze)
    }

    override fun getRecipes() {
        recipesDisposable = provider.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showLoader(true) }
                .subscribe({ r ->
                    provider.saveRecipesToDb(r)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                provider.getRecepiesFromDb()
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                { r -> view?.setAdapterWithData(r) },
                                                { e -> handleError(e) }
                                        )
                            }

                }, { e ->
                    if (provider.checkIfDbIsEmpty()) {
                        handleError(e)
                    } else {
                        provider.getRecepiesFromDb()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { r -> view?.setAdapterWithData(r) },
                                        { e -> handleError(e) }
                                )
                    }
                })
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
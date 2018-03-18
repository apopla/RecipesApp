package com.example.paulina.assignmentapplication

import com.example.paulina.assignmentapplication.recipes.contract.RecipeContract
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.presenter.RecipePresenter
import com.example.paulina.assignmentapplication.recipes.repository.RecipeRepository
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Created by Paulina on 2018-03-18.
 */
class RecipePresenterTest {

    private val immediate = object : Scheduler() {
        override fun scheduleDirect(run: Runnable,
                                    delay: Long, unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Scheduler.Worker {
            return ExecutorScheduler.ExecutorWorker(
                    Executor { it.run() })
        }
    }

    private val view: RecipeContract.View = mock()
    private val repo: RecipeRepository = mock()
    var mockedResponse: List<Recipe> = mock()
    private lateinit var presenter: RecipeContract.Presenter
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

        testScheduler = TestScheduler()
        presenter = RecipePresenter(repo)
        presenter.attachView(view)

    }

    @Test
    fun test_getRecipes_should_callSuccess() {

        doReturn(Observable.just(mockedResponse))
                .`when`(repo)
                .getRecipse()

        presenter.getRecipes()

        testScheduler.triggerActions()
        verify(view).setAdapterWithData(mockedResponse)

    }

    @Test
    fun test_getRecipes_should_callError() {

        doReturn(Observable.error<List<Recipe>>(Exception("error")))
                .`when`(repo)
                .getRecipse()

        presenter.getRecipes()

        testScheduler.triggerActions()
        verify(view).showError("error")

    }


}
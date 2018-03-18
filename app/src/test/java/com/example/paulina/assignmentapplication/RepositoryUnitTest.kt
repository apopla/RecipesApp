package com.example.paulina.assignmentapplication

import com.example.paulina.assignmentapplication.network.NetworkService
import com.example.paulina.assignmentapplication.realm.RecipesRealmProvider
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.repository.RecipeRepository
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Created by Paulina on 2018-03-18.
 */
class RepositoryUnitTest {

    private val networkService: NetworkService = mock()
    private val realmProvider: RecipesRealmProvider = mock()
    private lateinit var testScheduler: TestScheduler
    private lateinit var repository: RecipeRepository

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

    @Before
    fun setup() {
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

        repository = RecipeRepository(networkService, realmProvider)
        testScheduler = TestScheduler()
    }

    @Test
    fun testGetRecipesWithInternetAndNoDatabase_success() {
        var mockedResponse: List<Recipe> = mock()

        doReturn(Observable.just(mockedResponse))
                .`when`(networkService)
                .getRecipes(0, 5, "", "thumbnail-medium")

        var testSub = TestObserver<List<Recipe>>()
        repository.getRecipse().subscribe(testSub)
        testSub.assertNoErrors()
        testSub.assertComplete()
    }

    @Test
    fun testGetRecipesWithNoInternetAndNoDatabase_error() {
        var mockedResponse: List<Recipe> = mock()
        var exception  = Exception("error")

        //internet response
        doReturn(Observable.error<List<Recipe>>(exception))
                .`when`(networkService)
                .getRecipes(0, 5, "", "thumbnail-medium")

        //empty response
        doReturn(mockedResponse)
                .`when`(realmProvider)
                .getRecipesFromDb()

        var testSub = TestObserver<List<Recipe>>()
        repository.getRecipse().subscribe(testSub)
        testSub.assertError(exception)
    }

    @Test
    fun testGetRecipesWithNoInternetAndFullDatabase_success() {
        var mockedFullResponse = listOf<Recipe>(Recipe(), Recipe(), Recipe())
        var exception  = Exception("error")

        //no internet
        doReturn(Observable.error<List<Recipe>>(exception))
                .`when`(networkService)
                .getRecipes(0, 5, "", "thumbnail-medium")

        //empty response
        doReturn(mockedFullResponse)
                .`when`(realmProvider)
                .getRecipesFromDb()

        var testSub = TestObserver<List<Recipe>>()
        repository.getRecipse().subscribe(testSub)
        testSub.assertNoErrors()
        testSub.assertComplete()
    }

    @Test
    fun testGetRecipesWithInternetAndFullDatabase_success() {
        var mockedFullResponse = listOf<Recipe>(Recipe(), Recipe(), Recipe())

        doReturn(Observable.just(mockedFullResponse))
                .`when`(networkService)
                .getRecipes(0, 5, "", "thumbnail-medium")

        //empty response
        doReturn(mockedFullResponse)
                .`when`(realmProvider)
                .getRecipesFromDb()

        var testSub = TestObserver<List<Recipe>>()
        repository.getRecipse().subscribe(testSub)
        testSub.assertNoErrors()
        testSub.assertComplete()
    }

}
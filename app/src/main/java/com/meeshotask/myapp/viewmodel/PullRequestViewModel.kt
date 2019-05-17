package com.meeshotask.myapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.meeshotask.myapp.R
import com.meeshotask.myapp.model.PullRequest
import com.meeshotask.myapp.model.PullRequestUIModel
import com.meeshotask.myapp.networkhelper.GithubApiService
import com.meeshotask.myapp.networkhelper.RetrofitHelper
import com.meeshotask.myapp.util.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class PullRequestViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var responseLiveData :MutableLiveData<PullRequestResponse>
    private val somethingWentWrong = application.applicationContext.getString(R.string.something_went_wrong)
    private val noItemsFound = application.applicationContext.getString(R.string.no_items_found)
    var disposable: CompositeDisposable?=null

    /**
     * initializes the disposable and live data*/
    fun init(){
        disposable = CompositeDisposable()
        responseLiveData = MutableLiveData()
    }

    /**uses the retrofit and RxJava to make a HTTP request to fetch the pull requests
     *  for the given github owner and repo name
     *  uses the Live data to update the result
     *
     *
     * @param ownerName is the owner of github repo
     * @param repoName is the name of the github repository
     *
    *
    * */
    fun getPullRequests(ownerName:String,repoName:String){
        try {
            responseLiveData.value = PullRequestResponse.Loading
            val pullRequestObservable = RetrofitHelper.getInstance().
                create(GithubApiService::class.java).getOpenPullRequests(ownerName,repoName)
            disposable?.add(pullRequestObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Response<List<PullRequest>>?>() {
                    override fun onSuccess(t: Response<List<PullRequest>>) {
                        if (t.isSuccessful){
                            t.body()?.let {
                                if (it.isNotEmpty()){
                                    processList(it)
                                }else{
                                    responseLiveData.value = PullRequestResponse.Error(noItemsFound)
                                }
                            }
                        }else responseLiveData.value = PullRequestResponse.Error(noItemsFound)
                    }

                    override fun onError(e: Throwable) {
                        responseLiveData.value = PullRequestResponse.Error(somethingWentWrong)
                    }
                }))
        }catch (e:Exception){
            e.printStackTrace()
            responseLiveData.value = PullRequestResponse.Error(somethingWentWrong)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
    }



    /**
     * Converts the API reposnse model into the UIModel
     * @param pullRequestModel is the API response model
     * @return PullRequestUIModel is the UI model
    * */
   private fun convertToUiModel(pullRequestModel:PullRequest): PullRequestUIModel {
       val info = "#"+pullRequestModel.number + " opened on " +
               DateUtil.getDate(pullRequestModel.createdAt) +
               " by " + pullRequestModel.user.login

       return PullRequestUIModel(title = pullRequestModel.title,
           info = info,
           watchCount = pullRequestModel.base.repo.watchersCount.toString(),
           forkCount = pullRequestModel.base.repo.forksCount.toString(),
           language = pullRequestModel.base.repo.language)
   }

    /**
     * converts the list UIModel list and updates the observable*/
    private fun processList(pullRequestModelList:List<PullRequest>){
        val uiModelList = ArrayList<PullRequestUIModel>()
        for (item in pullRequestModelList){
            uiModelList.add(convertToUiModel(item))
        }
        responseLiveData.value = PullRequestResponse.Success(uiModelList)

    }
}

/*
* It is the sealed class which is used as the State of the reponse
* Succes is the state which means the HTTP  request is Successful
* Error is the list which means the HTTP request is failed due to some reasons
* Loading is the state which means that HTTP request is in progress
* */
sealed class PullRequestResponse{
    data class Success(var list:List<PullRequestUIModel>):PullRequestResponse()
    data class Error(var errorMsg:String):PullRequestResponse()
    object Loading:PullRequestResponse()
}
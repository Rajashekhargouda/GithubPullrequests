package com.meeshotask.myapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.meeshotask.myapp.R
import com.meeshotask.myapp.adapter.PullRequestAdapter
import com.meeshotask.myapp.databinding.ActivityPullRequestBinding
import com.meeshotask.myapp.model.PullRequestUIModel
import com.meeshotask.myapp.util.Constants
import com.meeshotask.myapp.util.hide
import com.meeshotask.myapp.util.show
import com.meeshotask.myapp.viewmodel.PullRequestResponse
import com.meeshotask.myapp.viewmodel.PullRequestViewModel
import kotlinx.android.synthetic.main.activity_pull_request.*

class PullRequestActivity : AppCompatActivity() {
    lateinit var pullRequestViewModel:PullRequestViewModel
    lateinit var activityBinding: ActivityPullRequestBinding
    lateinit var repoName: String
    lateinit var ownerName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_pull_request)
        pullRequestViewModel = ViewModelProviders.of(this)[PullRequestViewModel::class.java]
        pullRequestViewModel.init()
        setIntentData()
        setUpObserver()
        getPullRequests()
        setToolBar()
    }

    private fun setIntentData(){
        repoName = intent.getBundleExtra(Constants.KEY_DATA).getString(Constants.KEY_REPO_NAME)!!
        ownerName = intent.getBundleExtra(Constants.KEY_DATA).getString(Constants.KEY_OWNER_NAME)!!
    }

   private fun getPullRequests(){
       try {
           pullRequestViewModel.getPullRequests(ownerName,repoName)
       }catch (e:Exception){
           e.printStackTrace()
       }
   }

    private fun setToolBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title  = "$ownerName/$repoName"
    }


    /**
    * observes for the changes and updates the adapter according the state
     *
    * */
    private fun setUpObserver(){
        pullRequestViewModel.responseLiveData.observe(this, Observer {
            when(it){
                is PullRequestResponse.Success ->{
                    progressBar.hide()
                    txt_error_msg.hide()
                    updateList(it.list)

                }
                is PullRequestResponse.Error ->{
                    progressBar.hide()
                    txt_error_msg.show()
                    txt_error_msg.text = it.errorMsg


                }
                is PullRequestResponse.Loading ->{
                    progressBar.show()
                    txt_error_msg.hide()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    /**
     * update the adapter
     * @param itemList is the list of the items of type PullRequestUIModel
    * */
    private fun updateList(itemList:List<PullRequestUIModel>){
        activityBinding.rvPullRequest.layoutManager = LinearLayoutManager(this)
        val pullRequestAdapter = PullRequestAdapter(itemList)
        activityBinding.adapter = pullRequestAdapter
    }
}

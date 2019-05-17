package com.meeshotask.myapp.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.widget.Toast
import com.meeshotask.myapp.R
import com.meeshotask.myapp.util.Constants
import com.meeshotask.myapp.util.NetworkUtil
import com.meeshotask.myapp.util.ViewUtil
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title = getString(R.string.search_activity_title)
        setUpSearchView()
    }


    fun onSearched(input:String?){
        if (NetworkUtil.isNetworkConnected(this)){
            val bundle = validateInput(input)
            bundle?.let {
                navigateToPullRequestActivity(it)
            }?:ViewUtil.showToastErrorMsg(this,getString(R.string.invalid_input),Toast.LENGTH_SHORT)
        }else ViewUtil.showToastErrorMsg(this,getString(R.string.connect_to_internet),Toast.LENGTH_SHORT)

    }

    private fun setUpSearchView(){
        search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                onSearched(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }




    private fun validateInput(input:String?):Bundle?{
        try {
            input?.let {
                if (input.isNotEmpty()){
                    val inputQuery = input.split("/")
                    if (inputQuery.size==2){
                        val ownerName = inputQuery[0].trim()
                        val repoName = inputQuery[1].trim()
                        val bundle = Bundle()
                        bundle.putString(Constants.KEY_REPO_NAME,repoName)
                        bundle.putString(Constants.KEY_OWNER_NAME,ownerName)
                        return bundle
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }
        return null
    }


    private fun navigateToPullRequestActivity(bundle: Bundle){
        startActivity(Intent(this,PullRequestActivity::class.java)
            .putExtra(Constants.KEY_DATA,bundle))

    }

}

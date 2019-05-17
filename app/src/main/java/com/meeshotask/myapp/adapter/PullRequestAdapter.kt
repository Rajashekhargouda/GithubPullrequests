package com.meeshotask.myapp.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.meeshotask.myapp.R
import com.meeshotask.myapp.databinding.ItemPullRequestBinding
import com.meeshotask.myapp.model.PullRequestUIModel

class PullRequestAdapter(var itemList:List<PullRequestUIModel>):RecyclerView.Adapter<PullRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): ViewHolder {
        val itemPullRequestBinding = DataBindingUtil.inflate<ItemPullRequestBinding>(LayoutInflater
            .from(viewGroup.context),
            R.layout.item_pull_request,viewGroup,false)
        return ViewHolder(itemPullRequestBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        val item = itemList[pos]
        viewHolder.bind(item)
    }


    class ViewHolder(var pullRequestBinding: ItemPullRequestBinding) :
        RecyclerView.ViewHolder(pullRequestBinding.root){

        fun  bind(pullRequest: PullRequestUIModel){
            pullRequestBinding.pullRequestUiModel = pullRequest
            pullRequestBinding.executePendingBindings()
        }

    }
}
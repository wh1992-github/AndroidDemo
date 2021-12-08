package com.example.customview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.customview.R
import com.example.customview.bean.VoteBean
import com.example.customview.bean.VoteOption
import com.example.customview.data.getMockData
import com.example.customview.widget.vote.VoteLayoutAdapter
import kotlinx.android.synthetic.main.activity_sina_vote.*

class SinaVoteActivity : AppCompatActivity(), VoteLayoutAdapter.OnVoteClickListener {

    var voteLayoutAdapter: VoteLayoutAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sina_vote)
        voteLayoutAdapter = VoteLayoutAdapter(vote_ll)
        voteLayoutAdapter?.setData(getMockData())
        voteLayoutAdapter?.onVoteClickListener = this
    }

    override fun onDestroy() {
        super.onDestroy()
        voteLayoutAdapter?.onDestroy()
    }

    override fun onVoteCommitBtnClick(mainVote: VoteBean?, optionIds: ArrayList<Int>, position: Int) {
        voteLayoutAdapter?.refreshDataAfterVotedSuccess(position)
    }

    override fun onVoteItemClick(mainVote: VoteBean?, voteOption: VoteOption?, position: Int) {
    }

}
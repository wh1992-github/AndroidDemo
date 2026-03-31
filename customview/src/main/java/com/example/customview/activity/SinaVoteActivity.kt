package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.bean.VoteBean
import com.example.customview.bean.VoteOption
import com.example.customview.data.getMockData
import com.example.customview.databinding.ActivitySinaVoteBinding
import com.example.customview.widget.vote.VoteLayoutAdapter
/**
 * 用于展示 Sina Vote 功能的 Activity。
 */

class SinaVoteActivity : AppCompatActivity(), VoteLayoutAdapter.OnVoteClickListener {
    private lateinit var binding: ActivitySinaVoteBinding

    var voteLayoutAdapter: VoteLayoutAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinaVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        voteLayoutAdapter = VoteLayoutAdapter(binding.voteLl)
        voteLayoutAdapter?.setData(getMockData())
        voteLayoutAdapter?.onVoteClickListener = this
    }

    override fun onDestroy() {
        super.onDestroy()
        voteLayoutAdapter?.onDestroy()
    }

    override fun onVoteCommitBtnClick(
        mainVote: VoteBean?,
        optionIds: ArrayList<Int>,
        position: Int
    ) {
        voteLayoutAdapter?.refreshDataAfterVotedSuccess(position)
    }

    override fun onVoteItemClick(mainVote: VoteBean?, voteOption: VoteOption?, position: Int) {
    }

}

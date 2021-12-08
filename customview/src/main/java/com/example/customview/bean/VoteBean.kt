package com.example.customview.bean

class VoteBean(val id: Int = 0,
               val title: String?,
               val choiceType: String?,
               val maxSelect: Int?,
               var voted: Boolean?,
               val sumVoteCount: Int?,
               val options: ArrayList<VoteOption>?
)

data class VoteOption(var id: Int?,
                      var content: String?,
                      var voteId: Int?,
                      var showCount: Int?,
                      var voted: Boolean?)
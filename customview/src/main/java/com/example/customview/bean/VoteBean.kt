package com.example.customview.bean
/**
 * 用于描述 Vote 数据的实体类。
 */

class VoteBean(
    val id: Int = 0,
    val title: String?,
    val choiceType: String?,
    val maxSelect: Int?,
    var voted: Boolean?,
    val sumVoteCount: Int?,
    val options: ArrayList<VoteOption>?
)
/**
 * 封装 Vote Option 相关逻辑的类。
 */

data class VoteOption(
    var id: Int?,
    var content: String?,
    var voteId: Int?,
    var showCount: Int?,
    var voted: Boolean?
)
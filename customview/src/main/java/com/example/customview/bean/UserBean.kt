package com.example.customview.bean

/**
 * 用于描述 User 数据的实体类。
 */
data class UserBean @JvmOverloads constructor(
    var userName: String? = null,
    var sortLetters: String? = ""
)

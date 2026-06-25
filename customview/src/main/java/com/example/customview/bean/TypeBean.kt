package com.example.customview.bean

/**
 * 用于描述 Type 数据的实体类。
 */
data class TypeBean(
    var title: String?,
    var type: Int
) {
    override fun toString(): String {
        return "TypeBean: {title=${title},type=${type}}"
    }
}

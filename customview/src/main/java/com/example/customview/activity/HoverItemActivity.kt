package com.example.customview.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.adapter.HoverItemAdapter
import com.example.customview.bean.UserBean
import com.example.customview.databinding.ActivityHoverItemBinding
import com.example.customview.utils.CharacterParser
import com.example.customview.utils.PinyinComparator
import com.example.customview.widget.HoverItemDecoration
import com.example.customview.widget.IndexView
import java.util.Collections
import java.util.Locale

/**
 * 用于展示 Hover Item 功能的 Activity。
 */
open class HoverItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHoverItemBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var indexView: IndexView
    private lateinit var showTextDialog: TextView
    private lateinit var adapter: HoverItemAdapter
    private var userBeans = mutableListOf<UserBean>()
    private val names = arrayOf("阿妹", "打黑牛", "张三", "李四", "王五", "田鸡", "孙五")
    private lateinit var characterParser: CharacterParser
    private lateinit var pinyinComparator: PinyinComparator
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoverItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        characterParser = CharacterParser.getInstance()
        pinyinComparator = PinyinComparator()
        userBeans = filledData(getData()).toMutableList()

        recyclerView = binding.recyclerView
        indexView = binding.indexView
        showTextDialog = binding.showTextDialog

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            HoverItemDecoration(this) { position -> userBeans[position].sortLetters }
        )

        adapter = HoverItemAdapter(userBeans)
        recyclerView.adapter = adapter
        initIndexView()
    }

    private fun initIndexView() {
        indexView.setShowTextDialog(showTextDialog)
        indexView.setOnTouchingLetterChangedListener { letter ->
            val position = getPositionForSection(letter)
            if (position != -1) {
                layoutManager.scrollToPositionWithOffset(position, 0)
                layoutManager.stackFromEnd = false
            }
        }
    }

    fun getPositionForSection(section: String): Int =
        userBeans.indexOfFirst { it.sortLetters!!.equals(section) }

    private fun getData(): List<UserBean> =
        MutableList(50) { index -> UserBean().apply { userName = names[index % names.size] } }

    private fun filledData(sortList: List<UserBean>): List<UserBean> {
        sortList.forEach { userBean ->
            if ("" == userBean.userName) {
                userBean.sortLetters = "#"
            } else {
                val pinyin = characterParser.getSelling(userBean.userName!!)
                val sortString = pinyin.substring(0, 1).uppercase(Locale.getDefault())
                userBean.sortLetters = if (sortString.matches(Regex("[A-Z]"))) {
                    sortString.uppercase(Locale.getDefault())
                } else {
                    "#"
                }
            }
        }
        Collections.sort(sortList, pinyinComparator)
        return sortList
    }
}

package com.example.customview.kotlin

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class JiHe {

    companion object {
        private const val TAG = "JiHe---"

        @RequiresApi(Build.VERSION_CODES.N)
        fun test(): Unit {

            Log.i(TAG, "dispatchTouchEvent: ${2}")

            val list = arrayListOf<Int>()
            list.add(1)
            list.add(3)

            val users = mutableMapOf<String, User>()
            users["A"] = User("aa", 22)
            users["B"] = User("bb", 233)
            users["C"] = User("cc", 22444)

            users.forEach { (key, value) ->
                Log.i(TAG, "dispatchTouchEvent: ${key} - ${value}")
            }

            users.map { (string, user) ->
                user.name == "aa"
            }

            Log.i(TAG, "dispatchTouchEvent: ${2}")
            Log.i(TAG, "dispatchTouchEvent: ${3}")

            val values: List<String?> = listOf("A", null, "B")
            val s = values.firstOrNull { s ->
                s == "A" || s == "B"
            }


            var datas = mutableListOf<String>()

            datas += listOf("a", "b")
            datas.addAll(listOf("c", "d"))
            datas += "e"
            datas.add("f")
            datas = (datas + "f").toMutableList()

            var datas2 = listOf<String>()
            datas2 += listOf("a")
            datas2 += listOf("b")
            datas2 = datas2 + "cd"

        }
    }
}

data class User(val name: String, val age: Int)

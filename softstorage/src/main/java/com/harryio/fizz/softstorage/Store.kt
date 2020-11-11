package com.harryio.fizz.softstorage

interface Store {

    val editor: Editor

    fun getString(key: String, defValue: String? = ""): String?

    fun getBoolean(key: String, defValue: Boolean? = false): Boolean?

    fun getInt(key: String, defValue: Int? = 0): Int?

    fun getLong(key: String, defValue: Long? = 0L): Long?

    fun getFloat(key: String, defValue: Float? = 0f): Float?

    fun hasKey(key: String): Boolean

    interface Editor {

        fun putString(key: String, `val`: String)

        fun putBoolean(key: String, `val`: Boolean)

        fun putInt(key: String, `val`: Int)

        fun putLong(key: String, `val`: Long)

        fun putFloat(key: String, `val`: Float)
    }
}
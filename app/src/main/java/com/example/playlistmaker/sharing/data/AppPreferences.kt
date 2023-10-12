package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.sharing.domain.MUSIC_MAKER_PREFERENCES

object AppPreferences  {
    private var sharedPreferences: SharedPreferences? = null

    // TODO step 1: call `AppPreferences.setup(applicationContext)` in your MainActivity's `onCreate` method
    fun setup(context: Context) {
    // TODO step 2: set your app name here
        sharedPreferences = context.getSharedPreferences(MUSIC_MAKER_PREFERENCES, MODE_PRIVATE)
    }

    // TODO step 4: replace these example attributes with your stored values
    var heightInCentimeters: Int?
        get() = Key.HEIGHT.getInt()
        set(value) = Key.HEIGHT.setInt(value)

    var darkTheme: Boolean?
        get() = Key.DARK_THEME_ENABLED.getBoolean()
        set(value) = Key.DARK_THEME_ENABLED.setBoolean(value)

    private enum class Key {
        DARK_THEME_ENABLED, HEIGHT; // TODO step 3: replace these cases with your stored values keys

        fun getBoolean(): Boolean? = if (sharedPreferences !!.contains(name)) sharedPreferences !!.getBoolean(name, false) else null
        fun getFloat(): Float? = if (sharedPreferences !!.contains(name)) sharedPreferences !!.getFloat(name, 0f) else null
        fun getInt(): Int? = if (sharedPreferences !!.contains(name)) sharedPreferences !!.getInt(name, 0) else null
        fun getLong(): Long? = if (sharedPreferences !!.contains(name)) sharedPreferences !!.getLong(name, 0) else null
        fun getString(): String? = if (sharedPreferences !!.contains(name)) sharedPreferences !!.getString(name, "") else null

        fun setBoolean(value: Boolean?) = value?.let { sharedPreferences !!.edit { putBoolean(name, value) } } ?: remove()
        fun setFloat(value: Float?) = value?.let { sharedPreferences !!.edit { putFloat(name, value) } } ?: remove()
        fun setInt(value: Int?) = value?.let { sharedPreferences !!.edit { putInt(name, value) } } ?: remove()
        fun setLong(value: Long?) = value?.let { sharedPreferences !!.edit { putLong(name, value) } } ?: remove()
        fun setString(value: String?) = value?.let { sharedPreferences !!.edit { putString(name, value) } } ?: remove()

        fun remove() = sharedPreferences !!.edit { remove(name) }
    }
}
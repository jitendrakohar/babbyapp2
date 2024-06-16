package com.example.yourapp

import android.content.Context

object LoginManager {
    private const val PREFS_NAME = "your_app_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    // Function to set login status
    fun setLoginStatus(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Function to get login status
    fun getLoginStatus(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Function to clear login status
    fun clearLoginStatus(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_IS_LOGGED_IN)
        editor.apply()
    }
    private const val KEY_USERNAME = "username"
    fun saveUsername(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getUsername(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USERNAME, "abcd@gmail.com") ?: "abcd@gmail.com"
    }
}

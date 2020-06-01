package com.undp.fleettracker.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferenceUtil private constructor(context: Context) {

    private val TAG = SharedPreferenceUtil::class.java.simpleName
    var SHARED_PREF_NAME: kotlin.String? = "applicationSharedPreferences"
    var sharedPreferences: SharedPreferences? = null
    var mContext: Context? = null

    companion object :
        SingletonHolderParameter<SharedPreferenceUtil, Context>(::SharedPreferenceUtil)

    init {
        mContext = context
        sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Write the data to the shared preference with respective key
     *
     * @param key  key to save
     * @param data data
     * @return result operation
     */
    fun writeStringSharePreferences(
        key: String?,
        data: String?
    ): Boolean {
        try {
            if (null == key) {
                return false
            }
            // initialize the shared preference
            initSharedPreference()
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putString(key, data)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Read the shared preference data from the given key
     *
     * @param key          key
     * @param defaultValue default value
     * @return string result
     */
    fun getStringSharedPreferences(
        key: String?,
        defaultValue: String?
    ): String? {
        try {
            // initialize the shared preference
            initSharedPreference()
            return sharedPreferences!!.getString(key, defaultValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    private fun initSharedPreference() {
        if (null == sharedPreferences) {
            sharedPreferences = mContext?.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

    /**
     * Write the data to the shared preference with respective key
     *
     * @param key  key
     * @param data data
     * @return boolean result
     */
    fun writeIntegerSharePreferences(key: String?, data: Int): Boolean {
        try {
            if (null == key) {
                return false
            }
            // initialize the shared preference
            if (null == sharedPreferences) {
                sharedPreferences = mContext?.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            }
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putInt(key, data)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Read the shared preference data from the given key
     *
     * @param key          key
     * @param defaultValue default value
     * @return int result
     */
    fun getIntegerSharedPreferences(key: String?, defaultValue: Int): Int {
        try {
            // initialize the shared preference
            if (null == sharedPreferences) {
                sharedPreferences = mContext?.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            }
            return sharedPreferences!!.getInt(key, defaultValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    /**
     * Write the data to the shared preference with respective key
     *
     * @param key  key to save
     * @param data data to save
     * @ status of operation
     */
    fun writeBooleanSharePreferences(
        key: String?,
        data: Boolean
    ): Boolean {
        try {
            if (null == key) {
                return false
            }
            // initialize the shared preference
            initSharedPreference()
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putBoolean(key, data)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Read the shared preference data from the given key
     *
     * @param key          key to save
     * @param defaultValue data to save
     * @ status of operation
     */
    fun getBooleanSharedPreferences(
        key: String?,
        defaultValue: Boolean
    ): Boolean {
        try {
            // initialize the shared preference
            initSharedPreference()
            return sharedPreferences!!.getBoolean(key, defaultValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    /**
     * Write the data to the shared preference with respective key
     *
     * @param key  key to save
     * @param data data to save
     * @return status of operation
     */
    fun writeObjectsSharePreferences(key: String?, data: Any?): Boolean {
        try {
            if (null == key) {
                return false
            }
            // initialize the shared preference
            if (null == sharedPreferences) {
                sharedPreferences = mContext?.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            }
            if (null == data) {
                Log.e(TAG, "Null object received for update.")
                return false
            }
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            val value = Gson().toJson(data)
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Read the shared preference data from the given key
     *
     * @param key          key to save
     * @param defaultValue default value
     * @return object
     */
    fun <T> getObjectSharedPreferences(
        key: String?,
        defaultValue: T,
        classOfT: Class<T>?
    ): T? {
        try {
            if (null == key) {
                return null
            }
            // initialize the shared preference
            if (null == sharedPreferences) {
                sharedPreferences = mContext?.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            }
            val value: String? = sharedPreferences!!.getString(key, null)
            val data: T = Gson().fromJson(value, classOfT as Type?)
            if (null == data) {
                Log.e(TAG, "Object received is null")
                return defaultValue
            }
            return data
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    /**
     * Read the shared preference data from the given key
     *
     * @param key          key to save
     * @param defaultValue default value
     * @return object
     */
    fun <T> getObjectSharedPreferences(
        key: String?,
        defaultValue: T,
        classOfT: TypeToken<*>
    ): T? {
        try {
            if (null == key) {
                return null
            }
            // initialize the shared preference
            if (null == sharedPreferences) {
                sharedPreferences = mContext?.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            }
            val value: String? = sharedPreferences!!.getString(key, null)
            val data: T = Gson().fromJson(value, classOfT.type)
            if (null == data) {
                Log.e(TAG, "Object received is null")
                return defaultValue
            }
            return data
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    fun remove(key: String?) {
        if (null != sharedPreferences) sharedPreferences!!.edit()
            .remove(key).apply()
    }

    /**
     * Clears sharedpreferences values.
     */
    fun clear() {
        if (null != sharedPreferences) sharedPreferences!!.edit()
            .clear().apply()
    }

}
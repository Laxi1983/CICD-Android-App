package com.undp.fleettracker.network.api.auth

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.microsoft.identity.client.*
import com.microsoft.identity.client.IPublicClientApplication.IMultipleAccountApplicationCreatedListener
import com.microsoft.identity.client.IPublicClientApplication.LoadAccountsCallback
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.undp.fleettracker.R
import com.undp.fleettracker.constants.SHARED_PREFERENCE_USER_NAME
import com.undp.fleettracker.util.SharedPreferenceUtil
import com.undp.fleettracker.util.SingletonHolder


class MSALADLoginUpdate private constructor() {

    companion object : SingletonHolder<MSALADLoginUpdate>(::MSALADLoginUpdate)

    var mContext: Context? = null
    var mActivity: Activity? = null
    var mCallBack: AADCallBack? = null

    fun setParameters(context: Context, activity: Activity, callBack: AADCallBack) {
        this.mContext = context
        this.mActivity = activity
        this.mCallBack = callBack
    }

    var SCOPES = arrayOf("46ef45fa-dc03-45e0-a709-e16e0c9df6a6/User.Read")
    var mMultipleAccountApp: IMultipleAccountPublicClientApplication? = null
    var mFirstAccount: IAccount? = null
    var accounts: List<IAccount>? = null

    init {
//        mMultipleAccountApp = PublicClientApplication.createMultipleAccountPublicClientApplication(
//            mContext,
//            R.raw.msal_config
//        );
    }

    fun loginNew() {
        // The user will be required to interact with a UI to obtain a token
        if (null == mMultipleAccountApp || null == mActivity) {
            mCallBack?.onError("Something went wrong please try again.")
        }
        mActivity?.let {
            getAuthInteractiveCallback()?.let { it1 ->
                mMultipleAccountApp!!.acquireToken(
                    it, SCOPES,
                    it1
                )
            }
        }
    }

    fun init() {
        mContext!!.let {
            PublicClientApplication.createMultipleAccountPublicClientApplication(
                it,
                R.raw.msal_config,
                object : IMultipleAccountApplicationCreatedListener {
                    override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                        mMultipleAccountApp = application
//                        loadAccounts()

                    }

                    override fun onError(exception: MsalException) {
                        //Log Exception Here
                        mCallBack!!.onError(exception.message!!)
                    }
                })
        }
    }

    /**
     * Load currently signed-in accounts, if there's any.
     */
    private fun loadAccounts() {
        if (mMultipleAccountApp == null) {
            return
        }
        mMultipleAccountApp!!.getAccounts(object : LoadAccountsCallback {
            override fun onTaskCompleted(result: List<IAccount>) {
                // You can use the account data to update your UI or your app database.
                Log.d("ROHAN", "****** Accounts fetch compete")
                accounts = result
                mCallBack!!.contextReturn(null)
            }

            override fun onError(exception: MsalException) {
                mCallBack!!.onError(exception.message.toString())
            }
        })
    }

    private fun loadAccounts1() {
        if (null == mMultipleAccountApp) {
            Log.d("ROHAN", "****** AccountApp null")
        }
        mMultipleAccountApp?.getAccounts(object : LoadAccountsCallback {
            override fun onError(exception: MsalException?) {
                mCallBack?.onError(exception?.message.toString())
            }

            override fun onTaskCompleted(result: MutableList<IAccount>?) {
                Log.d("ROHAN", "****** Accounts fetch compete")
                accounts = result
                mCallBack?.contextReturn(null)
            }
        })
    }

    private fun getAuthInteractiveCallback(): AuthenticationCallback? {
        return object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource */
                val accessToken = authenticationResult.accessToken
                mFirstAccount = authenticationResult.account
                SharedPreferenceUtil.getInstance(mContext!!)
                    .writeStringSharePreferences(
                        SHARED_PREFERENCE_USER_NAME,
                        mFirstAccount!!.username
                    )
                mCallBack?.tokenReceived(accessToken)
            }

            override fun onError(exception: MsalException?) {
                if (exception is MsalClientException) {
                    //And exception from the client (MSAL)
                } else if (exception is MsalServiceException) {
                    //An exception from the server
                }
                mCallBack?.onError(exception!!.message!!)
            }

            override fun onCancel() {
                /* User canceled the authentication */
                mCallBack?.onError("User canceled the authentication")
            }
        }
    }


    fun getTokenSilently() {
        val authority: String =
            mMultipleAccountApp!!.configuration.defaultAuthority.authorityURL.toString()

        val userName = SharedPreferenceUtil.getInstance(mContext!!).getStringSharedPreferences(
            SHARED_PREFERENCE_USER_NAME, ""
        )

        if (accounts == null || TextUtils.isEmpty(userName)) {
            mCallBack?.onError("No account is configured")
            return
        }

// Pick an account to obtain a token from without prompting the user to sign in
        var selectedAccount: IAccount? = null
        for (account in accounts!!) {
            if (account.username.equals(userName!!, true)) {
                selectedAccount = account
            }
        }

// Get a token without prompting the user
        mMultipleAccountApp?.acquireTokenSilentAsync(
            SCOPES,
            selectedAccount!!,
            authority,
            object : SilentAuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult) {
                    mFirstAccount = authenticationResult.account
                    val accessToken = authenticationResult.accessToken
                    mCallBack?.tokenReceived(accessToken)
                }

                override fun onError(exception: MsalException) {
                    mCallBack?.onError(exception.message!!)
                }
            })
    }

}
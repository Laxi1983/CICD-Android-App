package com.undp.fleettracker.network.api.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.*
import com.microsoft.identity.client.IPublicClientApplication.IMultipleAccountApplicationCreatedListener
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.undp.fleettracker.R


class MSALADLogin(context: Context, activity: Activity, callBack: AADCallBack) {


    val mContext: Context = context
    val mActivity: Activity = activity
    val mCallBack: AADCallBack = callBack

    var SCOPES = arrayOf("46ef45fa-dc03-45e0-a709-e16e0c9df6a6/User.Read")
    var mMultipleAccountApp: IMultipleAccountPublicClientApplication? = null
    var mFirstAccount: IAccount? = null

    init {
//        mMultipleAccountApp = PublicClientApplication.createMultipleAccountPublicClientApplication(
//            mContext,
//            R.raw.msal_config
//        );
    }

    fun loginNew() {
        Log.d("ROHAN", "Login called")
        mMultipleAccountApp = PublicClientApplication.createMultipleAccountPublicClientApplication(
            mContext,
            R.raw.msal_config
        );
        // The user will be required to interact with a UI to obtain a token

        // The user will be required to interact with a UI to obtain a token
        mMultipleAccountApp!!.acquireToken(mActivity, SCOPES, object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                mFirstAccount = authenticationResult.account
                val accessToken = authenticationResult.accessToken
                mCallBack.tokenReceived(accessToken)
            }

            override fun onError(exception: MsalException?) {}
            override fun onCancel() {}
        })
    }

    fun login() {
        PublicClientApplication.createMultipleAccountPublicClientApplication(mContext,
            R.raw.msal_config,
            object : IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    mMultipleAccountApp = application
                    getAuthInteractiveCallback()?.let {
                        mMultipleAccountApp!!.acquireToken(
                            mActivity, SCOPES,
                            it
                        )
                    }
                }

                override fun onError(exception: MsalException) {
                    //Log Exception Here
                    mCallBack.onError(exception.message!!)
                }
            })
    }

    private fun getAuthInteractiveCallback(): AuthenticationCallback? {
        return object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource */
                val accessToken = authenticationResult.accessToken
                mCallBack.tokenReceived(accessToken)
            }

            override fun onError(exception: MsalException?) {
                if (exception is MsalClientException) {
                    //And exception from the client (MSAL)
                } else if (exception is MsalServiceException) {
                    //An exception from the server
                }
                mCallBack.onError(exception!!.message!!)
            }

            override fun onCancel() {
                /* User canceled the authentication */
                mCallBack.onError("User canceled the authentication")
            }
        }
    }

    fun getTokenSilently() {
        mMultipleAccountApp = PublicClientApplication.createMultipleAccountPublicClientApplication(
            mContext,
            R.raw.msal_config
        );
        val authority: String =
            mMultipleAccountApp!!.configuration.defaultAuthority.authorityURL.toString()
        // Get a list of accounts on the device

        // Get a list of accounts on the device
        val accounts: List<IAccount> = mMultipleAccountApp!!.accounts

        if (accounts.isEmpty()) return

// Pick an account to obtain a token from without prompting the user to sign in
        val selectedAccount = accounts[0]

// Get a token without prompting the user

// Get a token without prompting the user
        mMultipleAccountApp?.acquireTokenSilentAsync(
            SCOPES,
            selectedAccount,
            authority,
            object : SilentAuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult) {
                    mFirstAccount = authenticationResult.account
                    val accessToken = authenticationResult.accessToken
                    mCallBack.tokenReceived(accessToken)
                }

                override fun onError(exception: MsalException) {}
            })
    }

}
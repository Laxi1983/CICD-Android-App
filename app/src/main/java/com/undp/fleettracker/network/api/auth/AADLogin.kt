package com.undp.fleettracker.network.api.auth

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.microsoft.aad.adal.*
import com.undp.fleettracker.constants.AD_CLIENT_ID
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @author: << sandip.mahajan >>
 */

class AADLogin(context: Context, activity: Activity, callBack: AADCallBack) {

    private val TAG = AADLogin::class.java.simpleName

    val mContext: Context = context
    val mActivity: Activity = activity
    val mCallBack: AADCallBack = callBack

    // Azure AD Constants
    var SCOPES = arrayOf("User.Read", "profile", "email")

    // Authority is in the form of https://login.microsoftonline.com/yourtenant.onmicrosoft.com
    private val AUTHORITY = "https://login.microsoftonline.com/common"

    // The clientID of your application is a unique identifier which can be obtained from the app registration portal
    private val CLIENT_ID = AD_CLIENT_ID

    // Resource URI of the endpoint which will be accessed
    private val RESOURCE_ID = "https://graph.microsoft.com/"

    // The Redirect URI of the application (Optional)
    private val REDIRECT_URI =
        "http://localhost"//""msauth://com.undp.fleettracker/67DkFSM5SuOkVoV6DJTeGIO30pI="

    // Microsoft Graph Constants
    private val MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me"

    // Azure AD Variables
    private var mAuthContext: AuthenticationContext? = null
    private var mAuthResult: AuthenticationResult? = null

    // Handler to do an interactive sign in and acquire token
    private var mAcquireTokenHandler: Handler? = null

    // Boolean variable to ensure invocation of interactive sign-in only once in case of multiple  acquireTokenSilent call failures
    private val sIntSignInInvoked = AtomicBoolean()

    // Constant to send message to the mAcquireTokenHandler to do acquire token with Prompt.Auto*/
    private val MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO = 1

    // Constant to send message to the mAcquireTokenHandler to do acquire token with Prompt.Always
    private val MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS = 2

    init {
        mAuthContext = AuthenticationContext(mContext, AUTHORITY, false)
        // Instantiate handler which can invoke interactive sign-in to get the Resource
        // sIntSignInInvoked ensures interactive sign-in is invoked one at a time
        mCallBack.contextReturn(mAuthContext!!)

        // Kill the token cache
        // Optionally call the signout endpoint to fully sign out the user account
        mAuthContext!!.cache.clear()

        mAcquireTokenHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (sIntSignInInvoked.compareAndSet(false, true)) {
                    if (msg.what == MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO) {
                        mAuthContext!!.acquireToken(
                            activity,
                            SCOPES,
                            null,
                            CLIENT_ID,
                            REDIRECT_URI,
                            PromptBehavior.Auto,
                            getAuthInteractiveCallback()
                        )
                    } else if (msg.what == MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS) {
                        mAuthContext!!.acquireToken(
                            activity,
                            SCOPES,
                            null,
                            CLIENT_ID,
                            REDIRECT_URI,
                            PromptBehavior.Always,
                            getAuthInteractiveCallback()
                        )
                    }
                }
            }
        }
    }

    fun login() {
        //Attempt an acquireTokenSilent call to see if we're signed in*/
        val userId = ""
        if (!TextUtils.isEmpty(userId)) {
            mAuthContext!!.acquireTokenSilent(
                SCOPES,
                CLIENT_ID,
                UserIdentifier(userId, UserIdentifier.UserIdentifierType.UniqueId),
                getAuthInteractiveCallback()
            )
            return
        }
        mAcquireTokenHandler!!.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
    }

    fun logout() {
        // Kill the token cache
        mAuthContext!!.cache.clear()
    }

    // Callback used for interactive request.  If succeeds we use the access
    // token to call the Microsoft Graph. Does not check cache

    private fun getAuthInteractiveCallback(): AuthenticationCallback<AuthenticationResult> {
        return object : AuthenticationCallback<AuthenticationResult> {
            override fun onSuccess(authenticationResult: AuthenticationResult?) {
                if (authenticationResult == null || TextUtils.isEmpty(authenticationResult.token)
                    || authenticationResult.status != AuthenticationResult.AuthenticationStatus.Succeeded
                ) {
                    Log.e(TAG, "Authentication Result is invalid")
                    return
                }
                // Successfully got a token, call graph now
                Log.d(TAG, "Successfully authenticated")
                Log.d(TAG, "ID Token: " + authenticationResult.token)

                // Store the auth result
                mAuthResult = authenticationResult

                // TODO() Decode Token
                //

                // Store User id to SharedPreferences to use it to acquire token silently later
                if (!TextUtils.isEmpty(authenticationResult.userInfo.uniqueId)) {

                }

                // set the sIntSignInInvoked boolean back to false 
                sIntSignInInvoked.set(false)
                mCallBack.tokenReceived(authenticationResult.token)
            }

            override fun onError(exception: Exception) {
                // Failed to acquireToken
                Log.e(TAG, "Authentication failed: $exception")
                if (exception is AuthenticationException) {
                    val error = exception.code
                    if (error == ADALError.AUTH_FAILED_CANCELLED) {
                        Log.e(TAG, "The user cancelled the authorization request")
                    } else if (error == ADALError.AUTH_FAILED_NO_TOKEN) {
                        // In this case ADAL has found a token in cache but failed to retrieve it.
                        // Retry interactive with Prompt.Always to ensure we do an interactive sign in
                        mAcquireTokenHandler!!.sendEmptyMessage(
                            MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS
                        )
                    } else {
                        Log.e(TAG, "$error")
                    }
                }
                // set the sIntSignInInvoked boolean back to false 
                sIntSignInInvoked.set(false)
                mCallBack.onError(exception.message!!)
            }
        }
    }
}
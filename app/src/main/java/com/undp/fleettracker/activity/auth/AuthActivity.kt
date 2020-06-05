package com.undp.fleettracker.activity.auth

//import com.undp.fleettracker.activity.notifications.NotificationsActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.astritveliu.boom.Boom
import com.microsoft.aad.adal.AuthenticationContext
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.dashboard.DashboardActivity
import com.undp.fleettracker.adapter.IntroductionAdapter
import com.undp.fleettracker.constants.APP_CENTER_SECRET_KEY
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.constants.userModel
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.auth.AuthRequestModel
import com.undp.fleettracker.models.auth.AuthResponseModel
import com.undp.fleettracker.network.api.auth.AADCallBack
import com.undp.fleettracker.network.api.auth.AuthAPI
import com.undp.fleettracker.network.api.auth.MSALADLoginUpdate
import com.undp.fleettracker.util.AppUtil
import com.undp.fleettracker.viewmodels.auth.AuthViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.content_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


/**
 * @author: << sandip.mahajan >>
 */
class AuthActivity : AppCompatActivity(), AADCallBack {

    val TAG = AuthActivity::class.java.name

    /* Azure AD Variables */
    private var mAuthContext: AuthenticationContext? = null

    private var authViewModel: AuthViewModel? = null

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory
    lateinit var mContext: Context
    lateinit var mActivity: AuthActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
//        initCrashlytics()
        mContext = this
        mActivity = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initWidgets()

        authViewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel::class.java)
//        MSALADLogin(this, this, this).getTokenSilently()
        MSALADLoginUpdate.getInstance().setParameters(this, this, this)
        MSALADLoginUpdate.getInstance().init()
//        redirectToNotifications()
//        throw RuntimeException("This is a crash")
    }

    private fun initWidgets() {
        supportActionBar!!.hide()
        Boom(btn_login)
        Boom(txt_header)
        smoolider.adapter = IntroductionAdapter(this)
    }

    fun login(view: View) {
        AppUtil.showProgress(this)
//        MSALADLogin(this, this, this).loginNew()

        MSALADLoginUpdate.getInstance().loginNew()
//        Runnable { MSALADLogin(mContext, mActivity, mActivity).loginNew() }.run()
//        Thread {
//            MSALADLoginUpdate.getInstance().loginNew()
//        }.start()
    }

    override fun contextReturn(context: AuthenticationContext?) {
        mAuthContext = context
//        val thread = Thread() {
//        MSALADLoginUpdate.getInstance().getTokenSilently()
//        }
//        thread.start()

    }

    override fun tokenReceived(token: String) {
        // Decode token and call Sign in API
        val authRequestModel = AuthRequestModel(
            AppUtil.decodeToken(token, "unique_name"),
            AppUtil.decodeToken(token, "name"),
            AppUtil.decodeToken(token, "oid")
        );

        BEARER_TOKEN = "bearer $token"

        val service = NetworkModule.provideRetrofitInstance().create(AuthAPI::class.java)

        val call = service.login(BEARER_TOKEN, authRequestModel)
        call.enqueue(object : Callback<AuthResponseModel> {
            override fun onFailure(call: Call<AuthResponseModel>, t: Throwable) {
                AppUtil.hideProgress()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<AuthResponseModel>,
                response: Response<AuthResponseModel>
            ) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Success:$response")
                if (response.code() == 200) {
                    val data = response.body()
                    Log.d(TAG, "Response object: $data")
                    val authResponseModel = response.body()!!
                    TENANT_ID = authResponseModel.data!!.tenantId!!
                    userModel = authResponseModel.data!!

                    runOnUiThread {
                        redirectToDashboard()
//                        redirectToNotifications()
                    }
                }
            }
        })
    }

    private fun redirectToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun redirectToNotifications() {
        //startActivity(Intent(this, NotificationsActivity::class.java))
        finish()
    }

    override fun onError(error: String) {
        AppUtil.hideProgress()
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        MSALADLoginUpdate.getInstance().init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mAuthContext!!.onActivityResult(requestCode, resultCode, data);
    }


    private fun initCrashlytics() {
        AppCenter.start(
            application, APP_CENTER_SECRET_KEY,
            Analytics::class.java, Crashes::class.java
        )
    }
}

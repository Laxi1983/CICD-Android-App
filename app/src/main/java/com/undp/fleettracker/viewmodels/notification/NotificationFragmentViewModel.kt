package com.undp.fleettracker.viewmodels.notification

import android.util.Log
import androidx.lifecycle.*
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.notifications.NotificationAck
import com.undp.fleettracker.models.notifications.NotificationRequestModel
import com.undp.fleettracker.models.notifications.NotificationsResponseModel
import com.undp.fleettracker.network.api.notifications.NotificationsAPI
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationFragmentViewModel : ViewModel() {

    private val notifications = MediatorLiveData<NotificationsResponseModel>()

    fun getNotificationList(notificationRequestModel: NotificationRequestModel) {
        Log.d("TAG", "Get API called")
        val service = NetworkModule.provideRetrofitInstance().create(NotificationsAPI::class.java)
        val source = LiveDataReactiveStreams.fromPublisher<NotificationsResponseModel> {
            service.getAlertsList(BEARER_TOKEN, notificationRequestModel)
                .subscribeOn(Schedulers.io())
        }

        notifications.addSource(source, object : Observer<NotificationsResponseModel> {
            override fun onChanged(notificationsResponseModel: NotificationsResponseModel?) {
                Log.d("TAG", "Add source")
                notifications.value = notificationsResponseModel
                notifications.removeSource(source)

            }
        })
    }

    fun observeNotificationData(): LiveData<NotificationsResponseModel> {
        return notifications
    }


    fun getNotificationListUpdated(notificationRequestModel: NotificationRequestModel) {
        Log.d("TAG", "Get API called: $notificationRequestModel")
        val service = NetworkModule.provideRetrofitInstance().create(NotificationsAPI::class.java)
        service.getAlertsListUpdated(BEARER_TOKEN, notificationRequestModel)
            .enqueue(object : Callback<NotificationsResponseModel> {
                override fun onFailure(call: Call<NotificationsResponseModel>, t: Throwable) {
                    notifications.postValue(NotificationsResponseModel())
                }

                override fun onResponse(
                    call: Call<NotificationsResponseModel>,
                    response: Response<NotificationsResponseModel>
                ) {
                    val data = response.body()!!
                    Log.d("TAG", "Response Alerts: $data")
                    notifications.postValue(response.body())
                }
            })

    }

    fun sendAcknowledgement(userId: Int, alertID: Int) {
        Log.d("TAG", "Send alert acknowledgement")
        val service = NetworkModule.provideRetrofitInstance().create(NotificationsAPI::class.java)
        val notificationAck = NotificationAck()
        notificationAck.tenantId = TENANT_ID
        notificationAck.userId = userId
        notificationAck.alertId = alertID
        service.notificationAcknowledge(BEARER_TOKEN, notificationAck)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    notifications.postValue(NotificationsResponseModel())
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val data = response.body()!!
                    Log.d("TAG", "Response Alerts: $data")
//                    notifications.postValue(response.body())
                }
            })

    }
}
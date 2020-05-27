package com.undp.fleettracker.viewmodels.notification

import android.util.Log
import androidx.lifecycle.*
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.notifications.NotificationRequestModel
import com.undp.fleettracker.models.notifications.NotificationsResponseModel
import com.undp.fleettracker.network.api.notifications.NotificationsAPI
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationFragmentViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

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
}
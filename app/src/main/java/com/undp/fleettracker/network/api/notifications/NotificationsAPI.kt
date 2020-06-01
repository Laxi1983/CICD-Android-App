package com.undp.fleettracker.network.api.notifications

import com.undp.fleettracker.constants.AUTHORIZATION
import com.undp.fleettracker.constants.GET_GEOFENCE_ALERTS
import com.undp.fleettracker.constants.NOTIFICATION_ACKNOWLEDGE
import com.undp.fleettracker.models.notifications.NotificationAck
import com.undp.fleettracker.models.notifications.NotificationRequestModel
import com.undp.fleettracker.models.notifications.NotificationsResponseModel
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/**
 * @author: << sandip.mahajan >>
 */

public interface NotificationsAPI {

    @POST(GET_GEOFENCE_ALERTS)
    fun getAlertsList(
        @Header(AUTHORIZATION) token: String,
        @Body notificationRequestModel: NotificationRequestModel
    ): Flowable<NotificationsResponseModel>

    @POST(GET_GEOFENCE_ALERTS)
    fun getAlertsListUpdated(
        @Header(AUTHORIZATION) token: String,
        @Body notificationRequestModel: NotificationRequestModel
    ): Call<NotificationsResponseModel>

    @POST(NOTIFICATION_ACKNOWLEDGE)
    fun notificationAcknowledge(
        @Header(AUTHORIZATION) token: String,
        @Body notificationAck: NotificationAck
    ): Call<ResponseBody>
}
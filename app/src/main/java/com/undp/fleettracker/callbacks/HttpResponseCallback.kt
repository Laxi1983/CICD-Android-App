package com.undp.fleettracker.callbacks

import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import retrofit2.Response

interface HttpResponseCallback {
    fun onResponse(
        httpRequests: HttpRequests,
        requestStatus: RequestStatus,
        response: Any?,
        throwable: Throwable?
    )
}
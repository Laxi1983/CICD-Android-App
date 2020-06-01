package com.undp.fleettracker.network.api.auth

import com.undp.fleettracker.constants.AUTHORIZATION
import com.undp.fleettracker.constants.LOGIN
import com.undp.fleettracker.models.auth.AuthRequestModel
import com.undp.fleettracker.models.auth.AuthResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * @author: << sandip.mahajan >>
 */

interface AuthAPI {
    @POST(LOGIN)
    fun login(
        @Header(AUTHORIZATION) token: String,
        @Body authRequestModel: AuthRequestModel
    ): Call<AuthResponseModel>
}
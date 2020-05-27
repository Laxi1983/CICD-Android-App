package com.undp.fleettracker.network.api.auth

import com.microsoft.aad.adal.AuthenticationContext

/**
 * @author: << sandip.mahajan >>
 */

interface AADCallBack {
    fun contextReturn(context: AuthenticationContext?)
    fun tokenReceived(token: String)
    fun onError(error: String)
}
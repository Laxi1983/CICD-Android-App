package com.undp.fleettracker.viewmodels.auth

import androidx.lifecycle.ViewModel
import com.undp.fleettracker.network.SessionManager
import com.undp.fleettracker.network.api.auth.AuthAPI
import javax.inject.Inject

/**
 * @author: << sandip.mahajan >>
 */

class AuthViewModel
@Inject constructor
    (
    private val authApi: AuthAPI?,
    private val sessionManager: SessionManager?
) :
    ViewModel() {

}
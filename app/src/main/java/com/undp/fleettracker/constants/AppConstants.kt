package com.undp.fleettracker.constants

import com.undp.fleettracker.models.auth.UserModel
import com.undp.fleettracker.models.fleet.FleetModel

/**
 * @author: << sandip.mahajan >>
 */
const val AUTHORIZATION = "Authorization"
var TENANT_ID = 0
var userModel:UserModel? = null
var BEARER_TOKEN = ""
var fleetList = arrayListOf<FleetModel>()

const val APP_DATE_FORMAT = "MM/dd/yyyy"
const val REQUEST_DATE_FORMAT = "yyyy-MM-dd"
const val START_TIME = "00:00:00"
const val END_TIME = "23:59:59"
const val SHARED_PREFERENCE_USER_NAME = "USER_NAME"
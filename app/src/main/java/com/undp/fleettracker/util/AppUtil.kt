package com.undp.fleettracker.util

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import com.auth0.android.jwt.JWT
import com.kaopiz.kprogresshud.KProgressHUD
import com.undp.fleettracker.R
import com.undp.fleettracker.constants.APP_DATE_FORMAT
import com.undp.fleettracker.constants.BOOKING_DATE_FORMAT
import com.undp.fleettracker.constants.REQUEST_DATE_FORMAT
import com.undp.fleettracker.models.fleet.FleetModel
import com.undp.fleettracker.models.vehicle.VehicleInfo
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: << sandip.mahajan >>
 */
object AppUtil {

    // Decode MS AAD token
    fun decodeToken(token: String, claim: String): String {
        return JWT(token).getClaim(claim).asString()!!
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTimeStampToTime24Hr(timestamp: Long): String {
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val newDate = Date(timestamp)
            return sdf.format(newDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun convertToRequestedDate(input: String): String {
        try {
            val date = SimpleDateFormat(APP_DATE_FORMAT).parse(input)
            return SimpleDateFormat(REQUEST_DATE_FORMAT).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun convertToRequestedDate(input: Calendar): String {
        try {
            return SimpleDateFormat(REQUEST_DATE_FORMAT).format(input.time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun convertToAppDate(input: Calendar): String {
        try {
            return SimpleDateFormat(APP_DATE_FORMAT).format(input.time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private var progress: KProgressHUD? = null

    fun showProgress(context: Context) {
        if (null != progress)
            return
        progress = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
    }

    fun hideProgress() {
        if (null != progress && progress!!.isShowing()) {
            progress!!.dismiss()
            progress = null
        }
    }


    fun getFleetList(fleetModels: ArrayList<FleetModel>): ArrayList<String> {
        val fleets: ArrayList<String> = ArrayList()
        if (null != fleetModels && fleetModels.size > 0) {
            for (fleetModel in fleetModels) {
                fleets.add(fleetModel.FleetName)
            }
        }
        return fleets
    }

    fun getVehicleList(vehicleList: ArrayList<VehicleInfo>): ArrayList<String> {
        val vehicles: ArrayList<String> = ArrayList()
        if (null != vehicleList && vehicleList.size > 0) {
            for (vehicleInfo in vehicleList) {
                vehicles.add(vehicleInfo.VehicleName)
            }
        }
        return vehicles
    }

    interface OnDateSetListener {
        fun onDateChanged(calendar: Calendar)
    }

    var onDateSetListener: OnDateSetListener? = null

    fun showPickerDialog(context: Context, calendar: Calendar) {
        val dpd = DatePickerDialog(
            context, R.style.DatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val cal = Calendar.getInstance()

                // Display Selected date in textbox
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                onDateSetListener?.onDateChanged(cal)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dpd.datePicker.maxDate = Calendar.getInstance().timeInMillis
        dpd.show()
    }

    fun convertEpochSecondsToTimeString(epochSeconds: Int): String {
        var dateFormat: String = ""
        try {
            var epochMillis: Long = (epochSeconds * 1000).toLong()
            var date: Date = Date(epochMillis)
            dateFormat = SimpleDateFormat(BOOKING_DATE_FORMAT).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateFormat

    }

    fun convertTimeStringToSimpleDate(timeString: String?):String {
        var dateFormat: String = ""
        try {
            val format = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss"
            )
            var date = format.parse(timeString)
            dateFormat = SimpleDateFormat(BOOKING_DATE_FORMAT).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateFormat
    }
}

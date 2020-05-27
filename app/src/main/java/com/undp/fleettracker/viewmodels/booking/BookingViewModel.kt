package com.undp.fleettracker.viewmodels.booking

import androidx.lifecycle.ViewModel
import com.undp.fleettracker.network.api.booking.BookingAPI
import javax.inject.Inject

/**
 * @author: << sandip.mahajan >>
 */

class BookingViewModel
@Inject constructor
    (
    private val bookingAPI: BookingAPI?
) : ViewModel() {

}
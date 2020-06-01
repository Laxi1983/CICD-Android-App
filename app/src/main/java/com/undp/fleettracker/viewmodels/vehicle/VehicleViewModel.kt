package com.undp.fleettracker.viewmodels.vehicle

import androidx.lifecycle.ViewModel
import com.undp.fleettracker.network.api.vehicle.VehicleAPI
import javax.inject.Inject

/**
 * @author: << sandip.mahajan >>
 */

class VehicleViewModel
@Inject constructor
    (
    private val vehicleAPI: VehicleAPI?
) : ViewModel() {

}
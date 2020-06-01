package com.undp.fleettracker.viewmodels.fleet

import androidx.lifecycle.ViewModel
import com.undp.fleettracker.network.api.fleet.FleetAPI
import javax.inject.Inject

/**
 * @author: << sandip.mahajan >>
 */

class FleetViewModel
@Inject constructor
    (
    private val fleetAPI: FleetAPI?
) : ViewModel() {

}
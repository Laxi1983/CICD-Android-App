package com.undp.fleettracker.viewmodels.report

import androidx.lifecycle.ViewModel
import com.undp.fleettracker.network.api.report.ReportAPI
import javax.inject.Inject

/**
 * @author: << sandip.mahajan >>
 */

class ReportViewModel
@Inject constructor
    (
    private val reportAPI: ReportAPI?
) : ViewModel() {

}
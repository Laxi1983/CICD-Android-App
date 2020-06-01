package com.undp.fleettracker.activity.reports

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity

class ReportActivity : BaseActivity(), View.OnClickListener {

    private var rlForUtilizationReport: RelativeLayout? = null
    private var rlForSafetyReport: RelativeLayout? = null
    private var rlForHealthReport: RelativeLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        initBaseActivityViews()
        initViews()
    }

    private fun initViews() {
        rlForUtilizationReport =
            findViewById<RelativeLayout>(R.id.rlForUtilizationReport) as RelativeLayout
        rlForUtilizationReport!!.setOnClickListener(this)

        rlForSafetyReport = findViewById<RelativeLayout>(R.id.rlForSafetyReport) as RelativeLayout
        rlForSafetyReport!!.setOnClickListener(this)

        rlForHealthReport = findViewById<RelativeLayout>(R.id.rlForHealthReport) as RelativeLayout
        rlForHealthReport!!.setOnClickListener(this)
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_reports
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlForUtilizationReport -> startActivity(
                Intent(this, UtilizationReportActivity::class.java)
            )

            R.id.rlForSafetyReport -> startActivity(Intent(this, SafetyReportActivity::class.java))

            R.id.rlForHealthReport -> startActivity(Intent(this, HealthReportActivity::class.java))
        }
    }
}

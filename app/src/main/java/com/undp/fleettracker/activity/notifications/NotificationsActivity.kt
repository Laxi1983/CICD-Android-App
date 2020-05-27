package com.undp.fleettracker.activity.notifications

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity
import com.undp.fleettracker.adapter.SectionsPagerAdapter
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.constants.fleetList
import com.undp.fleettracker.constants.userModel
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetRequestModel
import com.undp.fleettracker.models.notifications.NotificationRequestModel
import com.undp.fleettracker.models.vehicle.VehicleInfo
import com.undp.fleettracker.network.api.fleet.FleetAPI
import com.undp.fleettracker.util.AppUtil
import io.github.rokarpov.backdrop.BackdropController
import kotlinx.android.synthetic.main.activity_notifications.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NotificationsActivity : BaseActivity() {

    val TAG = NotificationsActivity::class.java.simpleName

    private lateinit var backdropController: BackdropController
    private lateinit var filterView: FilterView
    private lateinit var fleetDetailModel: FleetDetailModel
    private var selectedIndex = 0
    private val notificationRequestModel = NotificationRequestModel()
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Notification"
        toolbar.inflateMenu(R.menu.menu_navigation)
        filterView = findViewById(R.id.nav_filter_view)

        initBackdropController(toolbar)
        initBaseActivityViews()
        initPageViewer()
        getFleetDetails()
    }

    private fun initPageViewer() {
        sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                selectedIndex = position
                generateNotificationRequestModel()
                sectionsPagerAdapter.currentlySelectedPage(position, notificationRequestModel)
            }
        })
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_notifications
    }

    private fun initBackdropController(toolbar: Toolbar) {
        backdropController = BackdropController.build(rootLayout, applicationContext) {
            supportToolbar = toolbar
            menuItemRevealSettings(R.id.menu_nav_filter, filterView)
            interactionSettings(filterView) {
                hideHeader = true
                animationProvider = FilterView.AnimatorProvider
            }
            concealedTitleId = R.string.label_notification
            concealedNavigationIconId = R.drawable.ic_back
            revealedNavigationIconId = R.drawable.ic_back
        }



        filterView.onCloseListener = object : FilterView.OnCloseListener {
            override fun onClose() {
                backdropController.conceal()
            }
        }

        filterView.onChangeListeners = object : FilterView.OnChangeListeners {
            override fun dateChange(startDate: Calendar, endDate: Calendar) {
                filterView.startDateCalender = startDate
                filterView.endDateCalender = endDate
                getUpdatedNotificationsList()
            }

            override fun fleetChange() {
            }

            override fun vehicleChange(vehicleInfo: VehicleInfo) {
                filterView.mVehicleInfo = vehicleInfo
                getUpdatedNotificationsList()
            }

        }
    }


    private fun getUpdatedNotificationsList() {
        generateNotificationRequestModel()
        sectionsPagerAdapter.currentlySelectedPage(selectedIndex, notificationRequestModel)
    }

    private fun generateNotificationRequestModel() {
        if (null != filterView.mVehicleInfo) {

            notificationRequestModel.tenantId = userModel?.tenantId
            notificationRequestModel.userId = userModel?.userID
            notificationRequestModel.vehicleId = filterView.mVehicleInfo?.VehicleId
            notificationRequestModel.recordStartDateStr =
                AppUtil.convertToRequestedDate(filterView.startDateCalender) + " 00:00:00"
            notificationRequestModel.recordEndDateStr =
                AppUtil.convertToRequestedDate(filterView.endDateCalender) + " 23:59:59"
            notificationRequestModel.sortColumnName = "index"
            notificationRequestModel.sortType = "asc"
            notificationRequestModel.pageNo = 1
            notificationRequestModel.pageSize = 10
            notificationRequestModel.startRecordNo = 0
            notificationRequestModel.isSearchTextLengthValid = true
            if (selectedIndex == 0) {
                notificationRequestModel.alertType = 4
            } else if (selectedIndex == 1) {
                notificationRequestModel.alertType = 2
            } else if (selectedIndex == 2) {
                notificationRequestModel.alertType = 3
            }

        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        backdropController.syncState()
    }

    override fun onBackPressed() {
        backdropController.conceal()
    }

    /**
     * Get the Fleet details
     */
    private fun getFleetDetails() {
        AppUtil.showProgress(this)
        val service = NetworkModule.provideRetrofitInstance().create(FleetAPI::class.java)

        val fleetRequestModel: FleetRequestModel = FleetRequestModel()

        fleetRequestModel.tenantId = TENANT_ID

        val call = service.getFleetOwnerFleetList(BEARER_TOKEN, fleetRequestModel)
        call.enqueue(object : Callback<FleetDetailModel> {
            override fun onFailure(call: Call<FleetDetailModel>, t: Throwable) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Failure::" + t.message)
            }

            override fun onResponse(
                call: Call<FleetDetailModel>,
                response: Response<FleetDetailModel>
            ) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Success:$response")
                if (response.code() == 200) {
                    val data = response.body()
                    Log.d(TAG, "Response object: $data")
                    fleetDetailModel = response.body()!!
                    runOnUiThread() {
                        fleetList.clear()
                        fleetList.addAll(fleetDetailModel.data)
                        filterView.updateFleetDataList()
                    }
                }
            }
        })
    }
}
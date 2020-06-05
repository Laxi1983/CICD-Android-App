package com.undp.fleettracker.activity.notifications

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import com.undp.fleettracker.R
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.constants.fleetList
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.vehicle.VehicleDetailsResponse
import com.undp.fleettracker.models.vehicle.VehicleInfo
import com.undp.fleettracker.network.VehicleManager
import com.undp.fleettracker.util.AppUtil
import io.github.rokarpov.backdrop.*
import kotlinx.android.synthetic.main.content_filter_back_layer.view.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class FilterView : RelativeLayout, View.OnClickListener {

    val TAG = FilterView::class.java.simpleName
    lateinit var closeButton: AppCompatImageButton
    var onCloseListener: OnCloseListener? = null
    var onChangeListeners: OnChangeListeners? = null
    lateinit var filterLayout: ConstraintLayout
    private lateinit var fleetAdapter: ArrayAdapter<String>
    private lateinit var vehicleAdapter: ArrayAdapter<String>
    private lateinit var fleetDetailModel: FleetDetailModel
    private lateinit var vehicleDetailsResponse: VehicleDetailsResponse
    private var tenantId: Int = TENANT_ID
    private var fleetId = 0
    var mVehicleInfo: VehicleInfo? = null
    var mContext: Context
    private lateinit var vehicleManager: VehicleManager
    lateinit var startDateCalender: Calendar
    lateinit var endDateCalender: Calendar
    var isStartDate: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        mContext = context
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.content_filter_back_layer, this)
        filterLayout = findViewById<ConstraintLayout>(R.id.filter_layout_notification)
        closeButton = findViewById(R.id.filter_view_close_btn)

        closeButton.setOnClickListener { onCloseListener?.onClose() }
        startDateCalender = Calendar.getInstance()
        endDateCalender = Calendar.getInstance()
        txtStartDate.text = AppUtil.convertToAppDate(startDateCalender)
        txtEndDate.text = AppUtil.convertToAppDate(endDateCalender)
        txtStartDate.setOnClickListener(this)
        txtEndDate.setOnClickListener(this)
        initVehicleManager()
        initFleetSpinner()
        initVehicleSpinner()
    }

    private fun initVehicleManager() {
        vehicleManager = VehicleManager()
        vehicleManager.iResponse = object : VehicleManager.IVehicleResponse {
            override fun onResponse(data: VehicleDetailsResponse) {
                if (null != data) {
                    vehicleDetailsResponse = data
                    val vehicleDetails = vehicleDetailsResponse.data

                    if (null != vehicleDetails) {
                        vehicleAdapter.clear()
                        vehicleAdapter.addAll(AppUtil.getVehicleList(vehicleDetails.VehicleInfo))
                        vehicleAdapter.notifyDataSetChanged()

                        startDateCalender.timeInMillis =
                            vehicleDetails.VehicleInfo[0].LastConnectedTimeEpoch * 1000L
                        endDateCalender.timeInMillis =
                            vehicleDetails.VehicleInfo[0].LastConnectedTimeEpoch * 1000L
                        txtStartDate.text = AppUtil.convertToAppDate(startDateCalender)
                        txtEndDate.text = AppUtil.convertToAppDate(endDateCalender)
                        onChangeListeners?.vehicleChange(vehicleDetails.VehicleInfo[0])
                    } else {
                        vehicleAdapter.clear()
                        vehicleAdapter.addAll(arrayListOf())
                        vehicleAdapter.notifyDataSetChanged()
                        mVehicleInfo = null
                    }
                }
            }

            override fun onFailure(data: String) {
                vehicleAdapter.clear()
                vehicleAdapter.addAll(arrayListOf())
                vehicleAdapter.notifyDataSetChanged()
                mVehicleInfo = null
            }
        }
    }

    private fun initFleetSpinner() {
        val spinner = findViewById<AppCompatSpinner>(R.id.spinnerFleet)
        fleetAdapter =
            ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                AppUtil.getFleetList(fleetList)
            )
        fleetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = fleetAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    if (null != parent) {
                        val data = parent.selectedItem
                        if (null != fleetList) {
                            for (fleetModel in fleetList) {
                                if (data === fleetModel.FleetName) {
                                    fleetId = fleetModel.FleetId
                                    vehicleManager.getVehicleListForFleet(fleetId, TENANT_ID)
                                    break
                                }
                            }
                            onChangeListeners?.fleetChange()
                        }
                        Log.d(TAG, "Selected Fleet $data")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (fleetList.size > 0) {
            vehicleManager.getVehicleListForFleet(fleetList[0].FleetId, TENANT_ID)
        }
    }

    private fun initVehicleSpinner() {
        val spinner = findViewById<AppCompatSpinner>(R.id.spinnerVehicles)
        vehicleAdapter =
            ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                AppUtil.getFleetList(ArrayList())
            )
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = vehicleAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    mContext,
                    "No vehicle available",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    if (null != parent) {
                        val data = parent.selectedItem

                        if (null != vehicleDetailsResponse.data) {
                            val vehicleDetails = vehicleDetailsResponse.data
                            for (vehicleInfo in vehicleDetails.VehicleInfo) {
                                if (data === vehicleInfo.VehicleName) {
                                    mVehicleInfo = vehicleInfo
                                    startDateCalender.timeInMillis =
                                        vehicleInfo.LastConnectedTimeEpoch * 1000L
                                    endDateCalender.timeInMillis =
                                        vehicleInfo.LastConnectedTimeEpoch * 1000L
                                    txtStartDate.text = AppUtil.convertToAppDate(startDateCalender)
                                    txtEndDate.text = AppUtil.convertToAppDate(endDateCalender)
                                    onChangeListeners?.vehicleChange(vehicleInfo)
                                }
                            }
                        }
                        Log.d(TAG, "Selected Fleet $data")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    interface OnCloseListener {
        fun onClose()
    }

    interface OnChangeListeners {
        fun fleetChange()
        fun vehicleChange(vehicleInfo: VehicleInfo)
        fun dateChange(startDate: Calendar, endDate: Calendar)
    }

    object AnimatorProvider : BackdropBackLayerInteractionData.ContentAnimatorProvider {
        override fun onPrepare(contentView: View) {
            hideView(contentView)
            if (contentView !is FilterView) return
            hideView(contentView.filterLayout)
        }

        override fun addOnRevealAnimators(
            contentView: View,
            animatorSet: AnimatorSet,
            delay: Long,
            duration: Long
        ): Long {
            if (contentView !is FilterView) return 0
            contentView.closeButton.setImageResource(R.drawable.ic_back)
            showView(contentView)

            addShowAnimator(animatorSet, contentView.filterLayout, delay, duration)

            return duration
        }

        override fun addOnConcealAnimators(
            contentView: View,
            animatorSet: AnimatorSet,
            delay: Long,
            duration: Long
        ): Long {
            if (contentView !is FilterView) return 0
            contentView.closeButton.setImageResource(R.drawable.ic_back)

            addHideAnimator(animatorSet, contentView.filterLayout, delay, duration)

            val weakContentView = WeakReference(contentView)
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    weakContentView.get()?.let { hideView(it) }
                }
            })
            return duration
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txtStartDate -> {
                isStartDate = true
                openDatePickerDialog()

            }
            R.id.txtEndDate -> {
                isStartDate = false
                openDatePickerDialog()
            }
        }
    }

    private fun openDatePickerDialog() {

        AppUtil.onDateSetListener = object : AppUtil.OnDateSetListener {
            override fun onDateChanged(calendar: Calendar) {
                if (isStartDate) {
                    if (calendar.timeInMillis > endDateCalender.timeInMillis) {
                        Toast.makeText(
                            mContext,
                            "Start date is grater than end date. Please select start date again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    txtStartDate.text = AppUtil.convertToAppDate(calendar)
                    startDateCalender = calendar

                    onChangeListeners?.dateChange(
                        startDateCalender,
                        endDateCalender
                    )

                } else {
                    if (calendar.timeInMillis < startDateCalender.timeInMillis) {
                        Toast.makeText(
                            mContext,
                            "End date is less than end date. Please select end date again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    txtEndDate.text = AppUtil.convertToAppDate(calendar)
                    endDateCalender = calendar

                    onChangeListeners?.dateChange(
                        startDateCalender,
                        endDateCalender
                    )
                }
            }
        }

        if (isStartDate) {
            AppUtil.showPickerDialog(mContext, startDateCalender)
        } else {
            AppUtil.showPickerDialog(mContext, endDateCalender)
        }

    }

    fun updateFleetDataList() {
        fleetAdapter.clear()
        fleetAdapter.addAll(AppUtil.getFleetList(fleetList))
        fleetAdapter.notifyDataSetChanged()
        vehicleManager.getVehicleListForFleet(fleetList[0].FleetId, TENANT_ID)
    }


}
package com.undp.fleettracker.activity.booking.customviews

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
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import com.undp.fleettracker.R
import com.undp.fleettracker.constants.APPROVAL_STATUS
import com.undp.fleettracker.constants.BOOKING_TYPE
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.constants.fleetList
import com.undp.fleettracker.util.AppUtil
import io.github.rokarpov.backdrop.*
import java.lang.ref.WeakReference

class BookingFilterView : RelativeLayout, View.OnClickListener {

    val TAG = BookingFilterView::class.java.simpleName
    lateinit var closeButton: AppCompatImageButton
    var onCloseListener: OnCloseListener? = null
    var onChangeListeners: OnChangeListeners? = null
    lateinit var filterLayout: ConstraintLayout
    private lateinit var fleetAdapter: ArrayAdapter<String>
    private lateinit var serviceAdapter: ArrayAdapter<String>
    private lateinit var bookingStatusAdapter: ArrayAdapter<String>
    private var tenantId: Int = TENANT_ID
    private var fleetId = 0
    var mContext: Context

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
        inflate(context, R.layout.content_booking_filter_back_layer, this)
        filterLayout = findViewById<ConstraintLayout>(R.id.filter_layout_notification)
        closeButton = findViewById(R.id.filter_view_close_btn)
        closeButton.setOnClickListener { onCloseListener?.onClose() }
        initServiceSpinner()
        initBookingStatusSpinner()
        initFleetSpinner()
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
                                    break
                                }
                            }
                            initBookingStatusSpinner()
                            initServiceSpinner()
                            onChangeListeners?.fleetChange(fleetId)
                        }
                        Log.d(TAG, "Selected Fleet $data")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initServiceSpinner() {
        val spinner = findViewById<AppCompatSpinner>(R.id.spinnerService)
        serviceAdapter =
            ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                BOOKING_TYPE
            )
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = serviceAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

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
                        var selectedService = "1,2,3"
                        if (position != 0) {
                            selectedService = "$position"
                        }
                        onChangeListeners!!.serviceChange(selectedService)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun initBookingStatusSpinner() {
        val spinner = findViewById<AppCompatSpinner>(R.id.spinnerBookingStatus)
        serviceAdapter =
            ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                APPROVAL_STATUS
            )
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = serviceAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

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
                        var selectedBookingType = "1,2,3,4"
                        if (position != 0) {
                            selectedBookingType = "$position"
                        }
                        onChangeListeners!!.bookingStatusChange(selectedBookingType)
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
        fun fleetChange(fleetID: Int)
        fun serviceChange(service: String)
        fun bookingStatusChange(status: String)
    }

    object AnimatorProvider : BackdropBackLayerInteractionData.ContentAnimatorProvider {
        override fun onPrepare(contentView: View) {
            hideView(contentView)
            if (contentView !is BookingFilterView) return
            hideView(contentView.filterLayout)
        }

        override fun addOnRevealAnimators(
            contentView: View,
            animatorSet: AnimatorSet,
            delay: Long,
            duration: Long
        ): Long {
            if (contentView !is BookingFilterView) return 0
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
            if (contentView !is BookingFilterView) return 0
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

    }
}
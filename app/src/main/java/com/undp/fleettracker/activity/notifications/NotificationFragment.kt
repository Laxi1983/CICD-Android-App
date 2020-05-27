package com.undp.fleettracker.activity.notifications

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.undp.fleettracker.R
import com.undp.fleettracker.adapter.NotificationsViewAdapter
import com.undp.fleettracker.models.notifications.NotificationRequestModel
import com.undp.fleettracker.models.notifications.NotificationsResponseModel
import com.undp.fleettracker.viewmodels.notification.NotificationFragmentViewModel
import kotlinx.android.synthetic.main.fragment_notifications.*

/**
 * A placeholder fragment containing a simple view.
 */
class NotificationFragment : Fragment() {

    private lateinit var pageViewModel: NotificationFragmentViewModel
    private lateinit var notificationsViewAdapter: NotificationsViewAdapter
    private var selectedPage: Int = 0
    private var mNotificationRequestModel: NotificationRequestModel? = null
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedPage = arguments?.getInt(ARG_SECTION_NUMBER)!!
        mNotificationRequestModel =
            arguments?.getSerializable(NOTIFICATION_REQUEST_OBJECT)!! as NotificationRequestModel
        pageViewModel =
            ViewModelProviders.of(this).get(NotificationFragmentViewModel::class.java).apply {
                setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        pageViewModel.text.observe(viewLifecycleOwner, Observer<String> {
            textView.text = it
        })
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        val edtSearch: AppCompatEditText = root.findViewById(R.id.edtSearch)
        activity?.applicationContext?.let {
            notificationsViewAdapter = NotificationsViewAdapter(
                it,
                ArrayList()
            )

            recyclerView.adapter = notificationsViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
        }

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                searchText = editable.toString().trim()
                if (searchText.length >= 3 || !searchText.isEmpty()) {
                    mNotificationRequestModel!!.searchKey = searchText
                    pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
                }
            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        subscribeToNotification()

        return root
    }

    override fun onResume() {
        super.onResume()
//        activity?.applicationContext?.let { AppUtil.showProgress(it) }
//        getUpdatedAlerts()
    }

    private fun getUpdatedAlerts() {

        if (null != mNotificationRequestModel && mNotificationRequestModel!!.vehicleId!! > 0) {
            when (selectedPage) {
                0 -> {
                    mNotificationRequestModel!!.alertType = 4
                }
                1 -> {
                    mNotificationRequestModel!!.alertType = 2
                }
                2 -> {
                    mNotificationRequestModel!!.alertType = 3
                }
            }

            pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
        }
    }

    private fun subscribeToNotification() {
        pageViewModel.observeNotificationData()
            .observe(viewLifecycleOwner,
                Observer<NotificationsResponseModel> { notificationsResponseModel ->
                    if (notificationsResponseModel?.data != null) {
                        recyclerView.visibility = View.VISIBLE
                        txtErrorMessage.visibility = View.GONE
//                        AppUtil.hideProgress()
                        activity?.applicationContext?.let {
                            notificationsViewAdapter = NotificationsViewAdapter(
                                it,
                                notificationsResponseModel.data!!
                            )
                            recyclerView.adapter = notificationsViewAdapter
                        }
                    } else {
                        recyclerView.visibility = View.GONE
                        txtErrorMessage.visibility = View.VISIBLE
                    }
                })
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val NOTIFICATION_REQUEST_OBJECT = "NOTIFICATION_REQUEST_OBJECT"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(
            sectionNumber: Int,
            notificationRequestModel: NotificationRequestModel
        ): NotificationFragment {
            return NotificationFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, sectionNumber)
                        putSerializable(NOTIFICATION_REQUEST_OBJECT, notificationRequestModel)
                    }
                }
        }
    }

    fun setNotificationModel(notificationRequestModel: NotificationRequestModel) {
        mNotificationRequestModel = notificationRequestModel
        if (!TextUtils.isEmpty(searchText)) {
            mNotificationRequestModel!!.searchKey = searchText
        } else {
            mNotificationRequestModel!!.searchKey = ""
        }
        pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
    }
}
package com.undp.fleettracker.activity.notifications

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.undp.fleettracker.R
import com.undp.fleettracker.adapter.NotificationsViewAdapter
import com.undp.fleettracker.models.notifications.NotificationModel
import com.undp.fleettracker.models.notifications.NotificationRequestModel
import com.undp.fleettracker.models.notifications.NotificationsResponseModel
import com.undp.fleettracker.viewmodels.notification.NotificationFragmentViewModel
import kotlinx.android.synthetic.main.fragment_notifications.*

/**
 * A placeholder fragment containing a simple view.
 */
class NotificationFragment : Fragment(), NotificationsViewAdapter.OnAcknowledgeClickListener,
    SwipeRefreshLayout.OnRefreshListener {

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


    private lateinit var pageViewModel: NotificationFragmentViewModel
    private lateinit var notificationsViewAdapter: NotificationsViewAdapter
    private var selectedPage: Int = 0
    private var mNotificationRequestModel: NotificationRequestModel? = null
    private var notificationModels: MutableList<NotificationModel> = ArrayList()
    private var searchText = ""
    private lateinit var mContext: Context
    private var isLoading: Boolean = false
    private var totalRecords = 0
    private var currentPageNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedPage = arguments?.getInt(ARG_SECTION_NUMBER)!!
        mNotificationRequestModel =
            arguments?.getSerializable(NOTIFICATION_REQUEST_OBJECT)!! as NotificationRequestModel
        initViewMode()
    }

    private fun initViewMode() {
        pageViewModel =
            ViewModelProviders.of(this).get(NotificationFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        initUI(root)

        return root
    }

    private fun initUI(root: View) {
//        mContext = context?.applicationContext!!
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        val edtSearch: AppCompatEditText = root.findViewById(R.id.edtSearch)

        val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.app_color)

        initRecyclerView(recyclerView)


        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                searchText = editable.toString().trim()
                if (searchText.length >= 3 || searchText.isEmpty()) {
                    mNotificationRequestModel!!.searchKey = searchText
//                    AppUtil.showProgress(mContext)
                    pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
                }
            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        subscribeToNotification()
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        activity?.applicationContext?.let {
            notificationsViewAdapter = NotificationsViewAdapter(
                it,
                notificationModels,
                this
            )

            recyclerView.adapter = notificationsViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null
                        && linearLayoutManager.findLastCompletelyVisibleItemPosition() == notificationModels.size - 1
                    ) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun loadMore() {


        if (notificationModels.size < totalRecords) {
            currentPageNumber++
            mNotificationRequestModel!!.pageNo = currentPageNumber
            mNotificationRequestModel!!.startRecordNo = notificationModels.size

            notificationModels.add(NotificationModel());
            notificationsViewAdapter.notifyItemInserted(notificationModels.size - 1)

            pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
        }
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
                    mNotificationRequestModel!!.alertType = 3
                }
                2 -> {
                    mNotificationRequestModel!!.alertType = 2
                }
            }
            pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
        }
    }

    private fun subscribeToNotification() {
        pageViewModel.observeNotificationData()
            .observe(viewLifecycleOwner,
                Observer<NotificationsResponseModel> { notificationsResponseModel ->
                    if (notificationsResponseModel?.data != null
                        && notificationsResponseModel.data?.size!! > 0
                    ) {

                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                            notificationModels.clear()
                            totalRecords = 0
                        }

                        if (isLoading) {
                            notificationModels.removeAt(notificationModels.size - 1)
                            notificationsViewAdapter.notifyItemRemoved(notificationModels.size - 1)

                        }
                        isLoading = false
                        totalRecords = notificationsResponseModel.total!!
                        recyclerView.visibility = View.VISIBLE
                        txtErrorMessage.visibility = View.GONE
//                        AppUtil.hideProgress()
//                        notificationModels.clear()
                        notificationModels.addAll(notificationsResponseModel.data!!)
                        notificationsViewAdapter.notifyDataSetChanged()

                    } else {
                        recyclerView.visibility = View.GONE
                        txtErrorMessage.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false
                        if (isLoading) {
                            notificationModels.removeAt(notificationModels.size - 1)
                            notificationsViewAdapter.notifyItemRemoved(notificationModels.size - 1)
                            isLoading = false

                        }
                    }
                })
    }

    fun setNotificationModel(notificationRequestModel: NotificationRequestModel) {
        try {
            clearNotificationData()
            mNotificationRequestModel = notificationRequestModel
            if (!TextUtils.isEmpty(searchText)) {
                mNotificationRequestModel!!.searchKey = searchText
            } else {
                mNotificationRequestModel!!.searchKey = ""
            }
            if (null == pageViewModel) {
                initViewMode()
            }
//            AppUtil.showProgress(mContext)
            pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearNotificationData() {
        try {
            if (null != notificationsViewAdapter) {
                notificationModels.clear()
                totalRecords = 0
                currentPageNumber = 1
                notificationsViewAdapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAcknowledgeClick(position: Int, alert: NotificationModel) {
        if (null == pageViewModel) {
            initViewMode()
        }
        if (null != mNotificationRequestModel) {
            pageViewModel.sendAcknowledgement(
                mNotificationRequestModel!!.userId!!,
                alert.alertId!!
            )
            notificationModels.remove(alert)
            notificationsViewAdapter.notifyItemRemoved(position)
        } else {
            Toast.makeText(
                activity,
                "Something went wrong please try again",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        recyclerView.visibility = View.GONE
        txtErrorMessage.visibility = View.GONE
        currentPageNumber = 1
        mNotificationRequestModel!!.pageNo = currentPageNumber
        mNotificationRequestModel!!.startRecordNo = 0
        pageViewModel.getNotificationListUpdated(mNotificationRequestModel!!)
    }
}
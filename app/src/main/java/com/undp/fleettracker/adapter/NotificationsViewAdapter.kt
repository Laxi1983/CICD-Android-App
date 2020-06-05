package com.undp.fleettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.undp.fleettracker.R
import com.undp.fleettracker.models.notifications.NotificationModel


class NotificationsViewAdapter(
    private val context: Context,
    private var notificationAlerts: List<NotificationModel>,
    private var mOnAcknowledgeClickListener: OnAcknowledgeClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType === VIEW_TYPE_ITEM) {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notifications, parent, false)
            NotificationViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loader, parent, false)
            LoadingViewHolder(view)
        }
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
//        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (notificationAlerts == null) 0 else notificationAlerts.size
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {

        return if (notificationAlerts.get(position).alertId == 0) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }


    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

    private fun populateNotificationView(
        position: Int,
        holder: NotificationViewHolder
    ) {
        val notifications = notificationAlerts[position]
        if (null != notifications) {
            holder.txtDescription.text = notifications.description
            holder.txtDate.text = notifications.date!!
            holder.txtAcknowledge.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (null != mOnAcknowledgeClickListener) {
                        notifications.let {
                            mOnAcknowledgeClickListener.onAcknowledgeClick(
                                position, it
                            )
                        }
                        notifyDataSetChanged()
                    }
                }
            })
        }
    }

//    fun setOnAcknowledgeClickListener(onAcknowledgeClickListener: OnAcknowledgeClickListener) {
//        mOnAcknowledgeClickListener = onAcknowledgeClickListener
//    }

    interface OnAcknowledgeClickListener {
        fun onAcknowledgeClick(position: Int, alert: NotificationModel)
    }

    class NotificationViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val txtDescription: AppCompatTextView =
            view.findViewById(R.id.txtDescription)
        val txtDate: AppCompatTextView = view.findViewById(R.id.txtDate)
        val txtAcknowledge: AppCompatTextView =
            view.findViewById(R.id.txtAcknowledge)

    }

    private class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is NotificationViewHolder) {
            populateNotificationView(position, holder)
        } else if (holder is LoadingViewHolder) {
            showLoadingView((holder as LoadingViewHolder?)!!, position)
        }
    }
}
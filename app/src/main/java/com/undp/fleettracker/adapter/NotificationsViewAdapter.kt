package com.undp.fleettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.undp.fleettracker.R
import com.undp.fleettracker.models.notifications.NotificationModel

class NotificationsViewAdapter(
    private val context: Context,
    private var notificationAlerts: List<NotificationModel>,
    private var mOnAcknowledgeClickListener: OnAcknowledgeClickListener
) :
    RecyclerView.Adapter<NotificationsViewAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationAlerts.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notifications = notificationAlerts[position]
        if (null != notifications) {
            holder.txtDescription.text = notifications.description
            holder.txtDate.text = notifications.date!!
            holder.txtAcknowledge.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (null != mOnAcknowledgeClickListener) {
                        notifications.let {
                            mOnAcknowledgeClickListener.onAcknowledgeClick(
                                it
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
        fun onAcknowledgeClick(alert: NotificationModel)
    }

    class NotificationViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val txtDescription: AppCompatTextView =
            view.findViewById(R.id.txtDescription)
        val txtDate: AppCompatTextView = view.findViewById(R.id.txtDate)
        val txtAcknowledge: AppCompatTextView =
            view.findViewById(R.id.txtAcknowledge)

    }
}
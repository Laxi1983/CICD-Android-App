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
    private var notificationAlerts: ArrayList<NotificationModel>
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
        }
    }

    fun updateDataset(notificationList: ArrayList<NotificationModel>) {
        notificationAlerts.clear()
        notificationAlerts.addAll(notificationList)
        notifyDataSetChanged()
    }

    class NotificationViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val txtDescription = view.findViewById<AppCompatTextView>(R.id.txtDescription)
        val txtDate = view.findViewById<AppCompatTextView>(R.id.txtDate)
        val txtAcknowledge = view.findViewById<AppCompatTextView>(R.id.txtAcknowledge)

    }
}
package com.nathanielmanoj.mobileappfinalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val eventList: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        holder.date.text = "Date: ${currentItem.date}"
        holder.location.text = "Location: ${currentItem.location}"
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.eventTitle)
        val description: TextView = itemView.findViewById(R.id.eventDescription)
        val date: TextView = itemView.findViewById(R.id.eventDate)
        val location: TextView = itemView.findViewById(R.id.eventLocation)
    }
}

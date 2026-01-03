package com.smartbus360.app.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartbus360.app.R
import com.smartbus360.app.data.model.AttendanceItem
import android.util.Log

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private var attendanceList: List<AttendanceItem> = emptyList()

    inner class AttendanceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val note: TextView = view.findViewById(R.id.txtNote)
        val regNumber: TextView = view.findViewById(R.id.txtRegNumber)
        val scanTime: TextView = view.findViewById(R.id.txtScanTime)
        val markedBy: TextView = view.findViewById(R.id.txtMarkedBy)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val item = attendanceList[position]
        holder.regNumber.text = " ${item.registrationNumber}"
        holder.note.text = "Note: ${item.note ?: "None"}"
        holder.scanTime.text = "Attendance: ${item.scan_time}"
        holder.markedBy.text = "Marked By: ${item.marked_by_role ?: "Unknown"}"

    }

    override fun getItemCount(): Int = attendanceList.size

    fun setData(newList: List<AttendanceItem>) {
        Log.d("AttendanceAdapter", "Received ${newList.size} items")
        attendanceList = newList
        notifyDataSetChanged()
    }
}

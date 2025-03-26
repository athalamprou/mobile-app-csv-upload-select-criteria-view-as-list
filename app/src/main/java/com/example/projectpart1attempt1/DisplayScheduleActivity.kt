package com.example.projectpart1attempt1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_schedule)

        // Retrieve the schedule list from the intent extras
        val scheduleList = intent.getSerializableExtra("schedule") as? ArrayList<ScheduleEntry>
            ?: arrayListOf()

        // Find the ListView in the layout
        val listView = findViewById<ListView>(R.id.listViewSchedule)

        // Use a custom adapter to display the schedule entries
        val adapter = ScheduleAdapter(this, scheduleList)
        listView.adapter = adapter
    }

    // Inner class for the custom adapter
    inner class ScheduleAdapter(context: Context, private val scheduleList: List<ScheduleEntry>) :
        ArrayAdapter<ScheduleEntry>(context, 0, scheduleList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val itemView = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.list_item_schedule, parent, false)

            val scheduleEntry = scheduleList[position]

            // Bind data to the views in the custom layout
            val textWeekDay = itemView.findViewById<TextView>(R.id.textWeekDay)
            val textStartTime = itemView.findViewById<TextView>(R.id.textStartTime)
            val textEndTime = itemView.findViewById<TextView>(R.id.textEndTime)
            val textTutors = itemView.findViewById<TextView>(R.id.textTutors)
            val textRoom = itemView.findViewById<TextView>(R.id.textRoom)
            val textClassTitle = itemView.findViewById<TextView>(R.id.textClassTitle)
            val textSemester = itemView.findViewById<TextView>(R.id.textSemester)
            val textAcademicYear = itemView.findViewById<TextView>(R.id.textAcademicYear)
            val textClassCode = itemView.findViewById<TextView>(R.id.textClassCode)

            textWeekDay.text = "Weekday: ${scheduleEntry.weekDay}"
            textStartTime.text = "Start Time: ${scheduleEntry.startTime}"
            textEndTime.text = "End Time: ${scheduleEntry.endTime}"
            textTutors.text = "Tutors: ${scheduleEntry.tutors}"
            textRoom.text = "Room: ${scheduleEntry.room}"
            textClassTitle.text = "Class Title: ${scheduleEntry.classTitle}"
            textSemester.text = "Semester: ${scheduleEntry.semester}"
            textAcademicYear.text = "Academic Year: ${scheduleEntry.academicYear}"
            textClassCode.text = "Class Code: ${scheduleEntry.classCode}"

            return itemView
        }
    }
}
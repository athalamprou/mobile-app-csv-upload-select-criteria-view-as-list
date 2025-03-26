package com.example.projectpart1attempt1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectCriteria : AppCompatActivity() {
    //dhlwsh metavlhths gia klhsh API
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_criteria)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        // vriskei ta views analoga to id tous (koumpi upovolhs kai checkbox)
        val submitButton = findViewById<Button>(R.id.SumbitCriteria)
        val weekDayCheckbox = findViewById<CheckBox>(R.id.weekDayBox)
        val startTimeCheckbox = findViewById<CheckBox>(R.id.startTimeBox)
        val endTimeCheckbox = findViewById<CheckBox>(R.id.endTimeBox)
        val tutorsCheckbox = findViewById<CheckBox>(R.id.tutorBox)
        val roomCheckbox = findViewById<CheckBox>(R.id.roomBox)
        val classTitleCheckbox = findViewById<CheckBox>(R.id.classTitleBox)
        val semesterCheckbox = findViewById<CheckBox>(R.id.semesterBox)
        val academicYearCheckbox = findViewById<CheckBox>(R.id.yearBox)
        // vriskei ta views analoga to id tous (spinners dhladh dropdown list)
        val weekDaySpinner = findViewById<Spinner>(R.id.weekDaySpinner)
        val startTimeSpinner = findViewById<Spinner>(R.id.startTimeSpinner)
        val endTimeSpinner = findViewById<Spinner>(R.id.endTimeSpinner)
        val tutorsSpinner = findViewById<Spinner>(R.id.tutorsSpinner)
        val roomSpinner = findViewById<Spinner>(R.id.roomSpinner)
        val classTitleSpinner = findViewById<Spinner>(R.id.classTitleSpinner)
        val semesterSpinner = findViewById<Spinner>(R.id.semesterSpinner)
        val academicYearSpinner = findViewById<Spinner>(R.id.academicYearSpinner)

        // gemizei ta spinners me oles tis epiloges pou xoume sto csv ana column
        //ftiaxnontas lista gia to kathena
        val tutors = listOf(
            "Skarpetis M. Panagiotis G. Giannarhs G.",
            "Sarakis L. Almpanh S.",
            "Ashmakis N.",
            "Skarpetis M. Panagiotakis G.",
            "Katevas N.",
            "Sofakis L.",
            "Stauropoulou V.",
            "Papadopoulos L - Manasis X - Koutsoumpis I.",
            "Mpithas P. Tsinos Ch.",
            "Asimakis N.",
            "Tzamtzh M.",
            "Papadopoulos K.",
            "Marhs TH. - Manasis X - Koutsoumpis I.",
            "Fragkoulis D.",
            "Papaioanou A.",
            "Tsoukalas M.",
            "Alexandridhs G.",
            "Panagidh K.",
            "Gonhs P.",
            "Makrugiannakhs G.",
            "Koubakas N - Koumpoulhs F.",
            "Maris TH.",
            "Papadopoulos P.",
            "Papadopoulou P.",
            "Basileiadhs L.",
            "Gkonhs P.",
            "Xenakis D.",
            "Skarpetis A.",
            "Katebas N.",
            "Stamoulhs D.",
            "Papaiwannou A."
        )

        val weekDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday")
        val startTimes = listOf(
            "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00",
            "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00"
        )
        val endTimes = listOf(
            "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00",
            "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00",
            "20:00:00"
        )

        val rooms = listOf(
            "Amfitheatro", "B116", "B118", "B105", "G103", "B102/104", "G202", "B202/204",
            "G101", "G107", "B103", "B101", "B110", "B106", "B108", "B108-B207", "B107",
            "F101"
        )
        val semesters = listOf("First", "Third", "5th", "7th")
        val academicYears = listOf("2023-24") // Update this if needed
        val classTitles = listOf(
            "Biomhxanikos elegxos kai aisthitires",
            "Biomhxanikos elegxos kai aisthitires (Erg Tmima 2)",
            "Biomhxanikos elegxos kai aisthitires (Erg Tmima 3)",
            "Eisagogi stin Oikonomiki",
            "Fysiki I (Mixaniki)",
            "Eisagogi ston Programmatismo (Erg. Tmima 1)",
            "Eisagogi ston Programmatismo (Erg. Tmima 2)",
            "Pshfiakh Sxediash",
            "Mathimatika I",
            "Pshfiakh Sxediash (Erg. Tmima 1)",
            "Pshfiakh Sxediash (Erg. Tmima 2)",
            "Eisagogi ston Programmatismo",
            "Eisagogi ston Programmatismo (Erg. Tmima 3)",
            "Eisagogi ston Programmatismo (Erg. Tmima 4)",
            "Genika Agglika",
            "Akadimaiki Grafi kai Texnikes Parousiaseon",
            "Dioikisi Kainotomias",
            "Hlektronika kai hlektrika kuklwmata (Erg Tmhma 1)",
            "Pithanotites kai StatistikI",
            "Seminario MATLAB (Erg. Tmima 1)",
            "Simata kai Systimata",
            "Diakrita Mathimatika",
            "Hlektronika kai hlektrika kuklwmata",
            "Simata kai Systimata (Ergastirio)",
            "Hlektronika kai hlektrika kuklwmata (Erg Tmhma 2)",
            "Dioikisi kai Lipsi Apofaseon",
            "Hlektronika kai hlektrika kuklwmata (Erg Tmhma 3)",
            "Hlektronika kai hlektrika kuklwmata (Erg Tmhma 4)",
            "Hlektronika kai hlektrika kuklwmata (Erg Tmhma 5)",
            "Hlektronika kai hlektrika kuklwmata (Erg Tmhma 6)",
            "Seminario MATLAB (Erg. Tmima 2)",
            "Seminario PYTHON (Erg.)",
            "Domes Dedomenon kai Texnikes Programmatismou YA (Erg. Tmima 1)",
            "Domes Dedomenon kai Texnikes Programmatismou YA (Erg. Tmima 2)",
            "Domes Dedomenon kai Texnikes Programmatismou YA (Erg. Tmima 3)",
            "Domes Dedomenon kai Texnikes Programmatismou YA (Erg. Tmima 4)",
            "Baseis dedomenwn",
            "Baseis dedomenwn (Ergasthrio)",
            "Systimata kinhtwn epikoinwniwn",
            "Xrhmatooikonomikh analush",
            "Biomhxanikh Organwsh",
            "Biomhxanika Hlektronika (Erg Tmima 1)",
            "Biomhxanika Hlektronika(Erg Tmima 2)",
            "Biomhxanika Hlektronika(Erg Tmima 3)",
            "Plhroforiaka Systhmata",
            "Hlektroniko Epixeirhn kai Pshfiakh epixeirhmatikothta",
            "Xrhmatooikonomikh analush - Ependyseis",
            "Epikoinwnies mikrhs embeleias",
            "Ananewsimes phges energeias",
            "Texnhth nohmosynh",
            "Marketing",
            "Systimata Kinhtwn epikoinwniwn",
            "Texnikes eksorukseis dedomenwn",
            "Diadiktyo ton Pragmaton (IoT)",
            "Texnologies Psifiakou Didymou (Digital Twin)",
            "Programmatismos Kiniton Syskeyon (Mobile programming)",
            "Epikoinonia Anthropou Mixanis",
            "Efodiastiki Alisyda",
            "Elegxos kai Programmatismos Robot",
            "Montelopoiisi Systimaton Prosomoiwsi",
            "Montelopoiisi Systimaton-Prosomoiwsi (Ergastirio)",
            "Elegxos kai Programmatismos Robo",
            "Sxediasmos/Paragogi me Ypologistiti (CAD/CAM)",
            "Programamtismos Kinhtwn Suskeuwn",
            "Texnologiki Provlepsi",
            "Plhroforiaka Systhmata Doikhshs kai Epixeirhsiakwn Porwn",
            "Ypologistikh Nefous",
            "Ypologistikh Nefous (Ergastirio)"
        )


        // anathetei adapter sto kathe spinner
        tutorsSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tutors)
        weekDaySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weekDays)
        startTimeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, startTimes)
        endTimeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, endTimes)
        roomSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rooms)
        semesterSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)
        academicYearSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, academicYears)
        classTitleSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classTitles)

        // kwdikas gia na emfanisei/krupsei to spinner analoga me to an paththike to checkbox sto kathena
        tutorsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            tutorsSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        weekDayCheckbox.setOnCheckedChangeListener { _, isChecked ->
            weekDaySpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        startTimeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            startTimeSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        endTimeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            endTimeSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        roomCheckbox.setOnCheckedChangeListener { _, isChecked ->
            roomSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        classTitleCheckbox.setOnCheckedChangeListener { _, isChecked ->
            classTitleSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        semesterCheckbox.setOnCheckedChangeListener { _, isChecked ->
            semesterSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        academicYearCheckbox.setOnCheckedChangeListener { _, isChecked ->
            academicYearSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        //otan patietai to koumpi submit
        submitButton.setOnClickListener {
            val selectedColumns = mutableListOf<String>() //lista kai klhsh gia ta columns
            val selectedValues = mutableMapOf<String, String>() //map kai klhsh gia tis times (values)

            //sullogh epilegmenwn columns kai timwn
            if (weekDayCheckbox.isChecked) {
                selectedColumns.add("weekDay")
                selectedValues["weekDay"] = weekDaySpinner.selectedItem.toString()
            }
            if (startTimeCheckbox.isChecked) {
                selectedColumns.add("startTime")
                selectedValues["startTime"] = startTimeSpinner.selectedItem.toString()
            }
            if (endTimeCheckbox.isChecked) {
                selectedColumns.add("endTime")
                selectedValues["endTime"] = endTimeSpinner.selectedItem.toString()
            }
            if (tutorsCheckbox.isChecked) {
                selectedColumns.add("tutors")
                selectedValues["tutors"] = tutorsSpinner.selectedItem.toString()
            }
            if (roomCheckbox.isChecked) {
                selectedColumns.add("room")
                selectedValues["room"] = roomSpinner.selectedItem.toString()
            }
            if (classTitleCheckbox.isChecked) {
                selectedColumns.add("classTitle")
                selectedValues["classTitle"] = classTitleSpinner.selectedItem.toString()
            }
            if (semesterCheckbox.isChecked) {
                selectedColumns.add("semester")
                selectedValues["semester"] = semesterSpinner.selectedItem.toString()
            }
            if (academicYearCheckbox.isChecked) {
                selectedColumns.add("academicYear")
                selectedValues["academicYear"] = academicYearSpinner.selectedItem.toString()
            }
            //ean den epilexthei kanena column dld kanena checkbox den exei tick vgale antistoixo mnma
            if (selectedColumns.isEmpty()) {
                Log.e("SelectCriteria", "No columns selected")
                return@setOnClickListener
            }

            // metatroph filters se JSON
            val filtersJson = Gson().toJson(selectedValues)

            // klhsh API
            val call = apiService.getSchedule(
                columns = selectedColumns.joinToString(","),
                filters = filtersJson
            )
            call.enqueue(object : Callback<List<ScheduleEntry>> {
                override fun onResponse(call: Call<List<ScheduleEntry>>, response: Response<List<ScheduleEntry>>) {
                    if (response.isSuccessful) { //ean einai epityxhs h epilogh
                        val schedule = response.body()
                        val intent = Intent(this@SelectCriteria, DisplayScheduleActivity::class.java).apply { //phgainei sto displayscheduleactivity
                            putExtra("schedule", ArrayList(schedule)) //kai metaferei th filtrarismenh lista
                        }
                        startActivity(intent)
                    } else {
                        Log.e("SelectCriteria", "Error: ${response.code()}") //alliws vgazei error code sto logcat
                    }
                }

                override fun onFailure(call: Call<List<ScheduleEntry>>, t: Throwable) {
                    Log.e("SelectCriteria", "API call failed: ${t.message}")
                }
            })
        }
    }
}

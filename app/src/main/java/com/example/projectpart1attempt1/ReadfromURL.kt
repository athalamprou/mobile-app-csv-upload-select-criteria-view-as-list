package com.example.projectpart1attempt1

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ReadfromURL : AppCompatActivity() {
//dhlwsh views kai apiservice
    private lateinit var apiService: ApiService
    private lateinit var urlInput: EditText
    private lateinit var loadCsvButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readfrom_url)

        // vriskei views apo to id tous
        urlInput = findViewById(R.id.urlInput)
        loadCsvButton = findViewById(R.id.loadCsvButton)

        // arxikopoihsh Retrofit gia API calls
        val retrofit = Retrofit.Builder()
            .baseUrl("http://46.246.220.117:5000/") // Βάση URL του server (προσαρμογή αν χρειάζεται)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        // otan patietai to koumpi, ksekinaei to download kai h epeksergasia tou html
        loadCsvButton.setOnClickListener {
            val htmlUrl = urlInput.text.toString().trim()
            if (htmlUrl.isNotEmpty()) {
                ProcessHtmlTask().execute(htmlUrl) //an den einai adeio ksekina to background task
            } else {
                Toast.makeText(this, "Εισάγετε ένα έγκυρο URL", Toast.LENGTH_SHORT).show() //an einai, antistoixo mhnuma
            }
        }
    }

   //auto to inner class ekteleitai sto background kai epeksergazetai to html ap to url
    inner class ProcessHtmlTask : AsyncTask<String, Void, List<ScheduleEntry>?>() {
        override fun doInBackground(vararg params: String?): List<ScheduleEntry>? {
            val url = params[0] //pare to url apo tis parametrous
            return try {
                processHtml(url!!) //kalei th sunarthsh processHtml
            } catch (e: IOException) {
                Log.e("ReadfromURL", "Error downloading HTML: ${e.message}") //an den mporei na kanei fetch, antistoixo mnma
                null
            }
        }
        //trexei sto main thread afou ektelestei to background task
        override fun onPostExecute(result: List<ScheduleEntry>?) {
            if (result != null && result.isNotEmpty()) {
                Log.d("ReadfromURL", "Parsed entries: $result") //an den einai adeio perna ta parsed entries

                // kanei upload to schedule mesw API
                val call = apiService.uploadSchedule(result)
                call.enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(
                        call: retrofit2.Call<Void>,
                        response: retrofit2.Response<Void>
                    ) {
                        if (response.isSuccessful) { //an einai epituxhs deikse kai toast kai logcat mnma
                            Toast.makeText(
                                this@ReadfromURL,
                                "Upload success",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("ReadfromURL", "Upload success")
                            val intent = Intent(this@ReadfromURL, SelectCriteria::class.java) //phgaine sto selectcriteria ean epituxes
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText( //an apotuxei kai mnma apotyxias toast kai sto logcat me response code
                                this@ReadfromURL,
                                "Upload failure",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("ReadfromURL", "Upload failure: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                        Toast.makeText(
                            this@ReadfromURL,
                            "Upload failure: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("ReadfromURL", "Upload failure: ${t.message}")
                    }
                })
            } else {
                Toast.makeText(this@ReadfromURL, "Failure configuring HTML", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //kanei fetch kai epeksergasia dedomenwn html ap to url mesw jsoup
    //kanei eksagwgh schedule data apo ena html pinaka kai epistrefei lista me ScheduleEntry antikeimena
    private fun processHtml(url: String): List<ScheduleEntry> {
        val doc = Jsoup.connect(url).get()
        val entries = mutableListOf<ScheduleEntry>()

        // eksagwgh dedomenwn apo ton html pinaka
        val elements = doc.select("table tr") // epilogh olwn twn grammwn tou pinaka
        Log.d("processHtml", "Found elements: ${elements.size}")
        for (element in elements.drop(1)) { //loupa gia kathe grammh, kanei skip to header dld th 1h grammh
            val weekDay   = element.select("td:nth-child(2)").text().trim().take(50)
            val startTime = element.select("td:nth-child(3)").text().trim().take(50)
            val endTime   = element.select("td:nth-child(4)").text().trim().take(50)
            val tutors    = element.select("td:nth-child(5)").text().trim().take(50)
            val room      = element.select("td:nth-child(6)").text().trim().take(50)
            val classTitle= element.select("td:nth-child(7)").text().trim().take(50)
            val semester  = element.select("td:nth-child(8)").text().trim().take(50)
            val academicYear = element.select("td:nth-child(9)").text().trim().take(50)
            val classCode    = element.select("td:nth-child(10)").text().trim().take(50)

            Log.d("processHtml", "Extracted data: $weekDay, $startTime, $endTime, $tutors, $room, $classTitle, $semester, $academicYear, $classCode")
            //dhmiourgia antikeimenoy ScheduleEntry kai prosthiki tou sth lista
            val entry = ScheduleEntry(
                weekDay = weekDay,
                startTime = startTime,
                endTime = endTime,
                tutors = tutors,
                room = room,
                classTitle = classTitle,
                semester = semester,
                academicYear = academicYear,
                classCode = classCode
            )
            entries.add(entry)
        }

        return entries //epistrefei th lista me ta parsed entries
    }

}

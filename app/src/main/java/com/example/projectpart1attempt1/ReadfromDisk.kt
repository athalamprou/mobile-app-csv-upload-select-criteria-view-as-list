package com.example.projectpart1attempt1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import android.app.Activity
import android.content.Intent
import android.net.Uri
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import android.util.Log //Gia debugging ama to xreiastei
import retrofit2.Call

import java.io.Serializable

data class ScheduleEntry(
    val weekDay: String?,
    val startTime: String?,
    val endTime: String?,
    val tutors: String?,
    val room: String?,
    val classTitle: String?,
    val semester: String?,
    val academicYear: String?,
    val classCode: String?
) : Serializable {
    override fun toString(): String {
        val parts = mutableListOf<String>()
        weekDay?.let { parts.add("Week Day: $it") }
        startTime?.let { parts.add("Start Time: $it") }
        endTime?.let { parts.add("End Time: $it") }
        tutors?.let { parts.add("Tutors: $it") }
        room?.let { parts.add("Room: $it") }
        classTitle?.let { parts.add("Class Title: $it") }
        semester?.let { parts.add("Semester: $it") }
        academicYear?.let { parts.add("Academic Year: $it") }
        classCode?.let { parts.add("Class Code: $it") }
        return parts.joinToString(" | ")
    }
}

class ReadfromDisk : AppCompatActivity() {
    private val PICK_FILE_REQUEST_CODE = 1 //Statherh metavlhth me timh 1
    // gia na perastei ws parametros startActivityForResult kai na tautopoihthei

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ReadfromDisk", "onCreate called") // Debugging

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://46.246.220.117:5000/")// base URL tou server
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { //neo intent pou kalei to ACTION_GET_CONTENT gia antlhsh periexomenou
            type = "text/csv" //orizoume ton MIME tupo arxeiwn pou dexetai to app se .csv
            addCategory(Intent.CATEGORY_OPENABLE) //mono open resources dld mh locked arxeia tha einai available gia epilogh
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE) //kalei activity me parametro th stathera pou orisame 1
    }//KLEINEI onCreate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //nea sunarthsh me intent, (gia parapanw ekshghsh des kefalaio 1 docsCNG)
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ReadfromDisk", "onActivityResult called") // Debugging
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) { //an requestCode idio me metavlhth pou orisame kai to result code exei timh RESULT_OK
            data?.data?.let { uri: Uri -> //tote tsekare oti to data den einai null ki an exei URI, an exei ektelese to block me to URI
                contentResolver.openInputStream(uri)?.use { inputStream -> //anoigoume ena InputStream gia anagnwsh dedomenwn apo to arxeio
                    BufferedReader(InputStreamReader(inputStream)).use { reader -> //anoigoume ena BufferedReader gia na diavasoume ti exei to InputStream. //
                        //to use einai gia na kleisoun ta resources meta thn anagnwsh
                        val fileContent = reader.readText()  //apothikeuoume ta periexomena ths anagnwshs sth metavlhth fileContent
                        Log.d("ReadfromDisk", "File content read: $fileContent") // Debugging

                        // EDW PREPEI NA VALOUME TON KWDIKA EPEKSERGASIAS ARXEIOU
                        val entries = parseCsv(fileContent)
                        Log.d("ReadfromDisk", "Parsed entries: $entries") // Add this line to log parsed entries

                        val call = apiService.uploadSchedule(entries) // Sending entire list
                        call.enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                                if (response.isSuccessful) {
                                    Log.d("ReadfromDisk", "Upload successful")
                                    val intent = Intent(this@ReadfromDisk, SelectCriteria::class.java)
                                    startActivity(intent)
                                    finish() // Optional: Finish current activity if you don't want to return to it
                                } else {
                                    Log.e("ReadfromDisk", "Upload failed with status: ${response.code()}, error body: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                                Log.e("ReadfromDisk", "Upload failed: ${t.message}")
                            }
                        })
                    }
                }
            }
        } else {
            // An o xrhsths den epileksei arxeio kleise activity
            Log.d("ReadfromDisk", "File selection canceled or failed") // Debugging
            finish()
        }
    }//KLEINEI onActivityResult

    private fun parseCsv(content: String): List<ScheduleEntry> {
        val entries = mutableListOf<ScheduleEntry>()
        val lines = content.split("\n")
        for (line in lines.drop(1)) { // skip th 1h grammh ta headers
            val tokens = line.split(",")
            if (tokens.size == 9) {
                val weekDay = tokens[0].trim()
                val entry = ScheduleEntry(
                    weekDay = weekDay,
                    startTime = tokens[1],
                    endTime = tokens[2],
                    tutors = tokens[3],
                    room = tokens[4],
                    classTitle = tokens[5],
                    semester = tokens[6],
                    academicYear = tokens[7],
                    classCode = tokens[8]
                )
                entries.add(entry)
            }
        }
        return entries
    }

}//EDW KLEINEI H class ReadfromDisk

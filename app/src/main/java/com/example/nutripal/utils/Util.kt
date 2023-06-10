package com.example.nutripal.utils

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.TextView
import com.example.nutripal.network.response.userpreference.ListUserPreferences
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Util {
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())
    fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }
    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun getPercenCalori(kebKalori: Double, terkonsumsi: Int): Int {
//        rumus = (kalori terkonsumsi/kebuthuan kalori)*100
        return ((terkonsumsi / kebKalori) * 100).toInt()
    }

    fun getCalories(gender:String, weightParam:String,heightParam:String,birthDate:String,activity:String,goal:String):Double{
        val umur = hitungUmur(birthDate)
        var bmr = 0.0
        if (gender.equals("Male",true)){
            bmr= 66.5+(13.7*weightParam.toInt())+(5*heightParam.toInt())-(6.8*umur)
        }else{
            bmr= 655+(9.6*weightParam.toInt())+(1.8*heightParam.toInt()) - (4.7*umur)
        }
        Log.e("UTILS",umur.toString())
        Log.e("UTILS",bmr.toString())
        return bmr*activity.toDouble()+goal.toInt()
//        BMR Pria = 66,5 + (13,7 × berat badan) + (5 × tinggi badan) – (6,8 × usia)
//        BMR Wanita = 655 + (9,6 × berat badan) + (1,8 × tinggi badan) – (4,7 × usia)
    }
    fun getCalories(preference: ListUserPreferences):Double{
        val umur = hitungUmur(preference.birthdate)
        var bmr = 0.0
        if (preference.gender.equals("Male",true)){
            bmr= 66.5+(13.7*preference.weight.toInt())+(5*preference.height.toInt())-(6.8*umur)
        }else{
            bmr= 655+(9.6*preference.weight.toInt())+(1.8*preference.height.toInt()) - (4.7*umur)
        }
        Log.e("UTILS",umur.toString())
        Log.e("UTILS",bmr.toString())
        return bmr*preference.activityLevel.toDouble()+preference.goals.toInt()
    }
    fun hitungUmur(tanggalLahirParam: String): Int {
        val part = tanggalLahirParam.split("-")
        val tanggalLahir = Calendar.getInstance()
        tanggalLahir.set(part[2].toInt(),part[1].toInt(),part[0].toInt())

        val tanggalSekarang = Calendar.getInstance()
        var umur = tanggalSekarang.get(Calendar.YEAR) - tanggalLahir.get(Calendar.YEAR)

        if (tanggalSekarang.get(Calendar.MONTH) < tanggalLahir.get(Calendar.MONTH)
            || (tanggalSekarang.get(Calendar.MONTH) == tanggalLahir.get(Calendar.MONTH)
                    && tanggalSekarang.get(Calendar.DAY_OF_MONTH) < tanggalLahir.get(Calendar.DAY_OF_MONTH))) {
            umur--
        }

        return umur
    }

    fun getDateNow(): String {
        val dateNow = Calendar.getInstance().time
        val mySdf = SimpleDateFormat("dd-MM-yyyy")
        return mySdf.format(dateNow)
    }
     fun setupDatePicker(context: Context,textView: TextView) {
        val cal = Calendar.getInstance()
         val dateNow = Calendar.getInstance().time
         val mySdf = SimpleDateFormat("dd-MM-yyyy")
         val now = mySdf.format(dateNow)
         textView.text = now
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.text = sdf.format(cal.time)
        }

        textView.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}
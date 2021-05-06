package com.example.projekt1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.time.LocalDate

class SecondActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val header: TextView = findViewById(R.id.headerSecondActivity)
        val statusText2: TextView = findViewById(R.id.textView2)

        val extras = intent.extras ?: return
        val year = extras.getInt("Year")
        statusText2.text = year.toString()
        header.text = "Niedziele handlowe w roku $year"
        header.textSize = 20F

        val easterDay = extras.getInt("EasterDay")
        val easterMonth = extras.getInt("EasterMonth")

        printSundays(year, statusText2, easterDay, easterMonth)

    }

    @SuppressLint("SetTextI18n")
    private fun printSundays(year: Int, statusText2: TextView, easterDay: Int, easterMonth: Int) {
        if(year<2020){
            statusText2.text = "Odmawiam obliczeń, wybierz rok >=2020"
        }
        else{
            val (januarySun, januarySunMonth) = calculateLastSunday(year, 1,31)
            val (easterSun, easterSunMonth)  = calculateLastSunday(year, easterMonth, easterDay-1)
            val (aprilSun, aprilSunMonth) = calculateLastSunday(year, 4, 30)
            val (juneSun, juneSunMonth) = calculateLastSunday(year, 6, 30)
            val (augustSun, augustSunMonth) = calculateLastSunday(year, 8, 30)
            val (decemberSunFirst, decemberSunMonth) = calculateLastSunday(year, 12, 24)
            val decemberSunSecond = decemberSunFirst - 7
            statusText2.text = "1 niedziela: $januarySun.0$januarySunMonth.$year\n" +
                    "2 niedziela: $easterSun.0$easterSunMonth.$year\n" +
                    "3 niedziela: $aprilSun.0$aprilSunMonth.$year\n" +
                    "4 niedziela: $juneSun.0$juneSunMonth.$year\n" +
                    "5 niedziela: $augustSun.0$augustSunMonth.$year\n" +
                    "6 niedziela: $decemberSunSecond.$decemberSunMonth.$year\n" +
                    "7 niedziela: $decemberSunFirst.$decemberSunMonth.$year"
        }
    }

    private fun calculateLastSunday(year: Int, month: Int, day: Int): Pair<Int, Int> {
        val dt = LocalDate.of(year, month, day)
        val day = dt.dayOfWeek.value
        val days = day % 7
        val newDt = dt.minusDays(days.toLong())
        return Pair(newDt.dayOfMonth, newDt.monthValue)
    }

    fun showMain(v: View){
        val i = Intent(this, MainActivity::class.java)
        startActivityForResult(i, Activity.RESULT_OK)
    }

    override fun finish() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }
}
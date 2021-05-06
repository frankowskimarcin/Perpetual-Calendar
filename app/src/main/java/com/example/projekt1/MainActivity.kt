package com.example.projekt1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.app.Activity
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.NumberPicker
import android.widget.TextView
import java.lang.reflect.Array.set
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 1000

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusText: TextView = findViewById(R.id.textView)

        val number_picker: NumberPicker = findViewById(R.id.numberPicker)
        number_picker.minValue = 1900
        number_picker.maxValue = 2200
        number_picker.wrapSelectorWheel = false
        number_picker.value = Calendar.getInstance().get(Calendar.YEAR)
        val currentYear = number_picker.value

        val (startDay, startMonth) = getEasterDate(currentYear)
        val (ashDayStart, ashMonthStart) = calculateDate(currentYear, startMonth, startDay, -46)
        val (corpusDayStart, corpusMonthStart) = calculateDate(currentYear, startMonth, startDay, 60)
        val (adventDayStart, adventMonthStart) = getAdventDate(currentYear)
        statusText.text = "Popielec: $ashDayStart.0$ashMonthStart.$currentYear\n" +
                "Wielkanoc: $startDay.$startMonth.0$currentYear\n" +
                "Boże ciało: $corpusDayStart.0$corpusMonthStart.$currentYear\n" +
                "Adwent: $adventDayStart.$adventMonthStart.$currentYear"

        number_picker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            val (easterDay, easterMonth) = getEasterDate(newVal)
            val (ashDay, ashMonth) = calculateDate(newVal, easterMonth, easterDay, -46)
            val (corpusDay, corpusMonth) = calculateDate(newVal, easterMonth, easterDay, 60)
            val (adventDay, adventMonth) = getAdventDate(newVal)
            statusText.text = "Popielec: $ashDay.0$ashMonth.$newVal\n" +
                    "Wielkanoc: $easterDay.0$easterMonth.$newVal\n" +
                    "Boże ciało: $corpusDay.0$corpusMonth.$newVal\n" +
                    "Adwent: $adventDay.$adventMonth.$newVal"
            true
        }

    }

    fun getEasterDate(year: Int): Pair<Int, Int> {
        val a = year % 19
        val b = (year / 100).toInt()
        val c = year % 100
        val d = (b / 4).toInt()
        val e = b % 4
        val f = ((b + 8) / 25).toInt()
        val g = ((b - f + 1) / 3).toInt()
        val h =(19 * a + b - d - g + 15) % 30
        val i = (c / 4).toInt()
        val k = c % 4
        val l  = (32 + 2 * e + 2 * i - h - k) % 7
        val m = ((a + 11 * h + 22 * l)/451).toInt()
        val p = (h + l - 7 * m + 114) % 31
        val day = p + 1
        val month = ((h + l - 7 * m + 114) / 31).toInt()
        return Pair(day, month)
    }

    fun calculateDate(year: Int, month: Int, day: Int, numberOfDays: Int): Pair<Int, Int> {
        val dt = LocalDate.of(year, month, day)
        val newDt = dt.plusDays(numberOfDays.toLong())
        val ashMonth = newDt.monthValue
        val ashDay = newDt.dayOfMonth
        return Pair(ashDay, ashMonth)
    }

    private fun getAdventDate(year: Int): Pair<Int, Int> {
        val dt = LocalDate.of(year, 12,25)
        val day = dt.dayOfWeek.value
        val days = 21 + day
        val newDt = dt.minusDays(days.toLong())
        val adventDay = newDt.dayOfMonth
        val adventMonth = newDt.monthValue
        return Pair(adventDay, adventMonth)
    }

    fun showActivitySundays(v: View){
        val i = Intent(this, SecondActivity::class.java)
        i.putExtra("Year", numberPicker.value)
        i.putExtra("EasterDay", getEasterDate(numberPicker.value).first)
        i.putExtra("EasterMonth", getEasterDate(numberPicker.value).second)
        startActivityForResult(i, REQUEST_CODE)
    }

    fun showActivityWorkdays(v: View){
        val i = Intent(this, ThirdActivity::class.java)
        startActivityForResult(i, REQUEST_CODE)
    }
}

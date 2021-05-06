package com.example.projekt1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_third.*
import java.lang.IllegalArgumentException
import java.time.DayOfWeek
import java.time.LocalDate

class ThirdActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val statusText1: TextView = findViewById(R.id.dataS)
        statusText1.text = "Data początkowa: "
        val statusText2: TextView = findViewById(R.id.dataE)
        statusText2.text = "Data końcowa: "

        val resultText: TextView = findViewById(R.id.textView3)
        resultText.text = "Kliknij przycisk POLICZ"

//        val datePicker1: DatePicker = findViewById(R.id.DatePickerS)
//        val datePicker2: DatePicker = findViewById(R.id.DatePickerE)
//
//        datePicker1.setOnDateChangedListener { datePicker, year, month, day ->
//            handleDatePickers()
//        }
//
//        datePicker2.setOnDateChangedListener { datePicker, i, i2, i3 ->
//            handleDatePickers()
//        }

    }

    @SuppressLint("SetTextI18n")
    fun handleDatePickers(v: View) {
        val dayStart = DatePickerS.dayOfMonth
        val monthStart = DatePickerS.month + 1
        val yearStart = DatePickerS.year
        val dayEnd = DatePickerE.dayOfMonth
        val monthEnd = DatePickerE.month + 1
        val yearEnd = DatePickerE.year

        val holidays: List<LocalDate> = generateOffDays(yearStart, yearEnd)

        val dateStart = LocalDate.of(yearStart, monthStart, dayStart)
        val dateEnd = LocalDate.of(yearEnd, monthEnd, dayEnd)

        if (dateStart.isAfter(dateEnd)){
            Log.i("Async", "Zły przedział", IllegalArgumentException())
            textView3.text = "Wybierz właściwy przedział. \nData początkowa musi być mniejsza od daty końcowej!"
        }
        else{
            val (days, workDays) = countDaysBetween(dateStart, dateEnd, holidays)
            textView3.text = "Dni kalendarzowych: $days\n"+
                    "Dni roboczych: $workDays"
        }
    }

    private fun generateOffDays(yearStart: Int, yearEnd: Int): List<LocalDate> {
        var returnList: MutableList<LocalDate> = arrayListOf()

        for (year in yearStart..yearEnd){
            returnList.add(LocalDate.of(year, 1, 1))
            returnList.add(LocalDate.of(year, 1, 6))
            returnList.add(LocalDate.of(year, 5, 1))
            returnList.add(LocalDate.of(year, 5, 3))
            returnList.add(LocalDate.of(year, 8, 15))
            returnList.add(LocalDate.of(year, 11, 1))
            returnList.add(LocalDate.of(year, 12, 25))
            returnList.add(LocalDate.of(year, 12, 26))
            val (easterDay, easterMonth) = MainActivity().getEasterDate(year)
            returnList.add(LocalDate.of(year, easterMonth, easterDay).plusDays(1))
            val (corpusDay, corpusMonth) = MainActivity().calculateDate(year, easterMonth, easterDay, 60)
            returnList.add(LocalDate.of(year, corpusMonth, corpusDay))
        }

        return returnList
    }

    private fun countDaysBetween(dateStart: LocalDate, dateEnd: LocalDate, holidays: List<LocalDate>): Pair<Int, Int>{
        var days = 0
        var workdays = 0
        var date = dateStart

        while (date.isBefore(dateEnd)){
            val day = date.dayOfWeek
            if (!holidays.contains(date) && day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY){
                workdays++
            }
            days++
            date = date.plusDays(1)
        }

        return Pair(days, workdays)
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
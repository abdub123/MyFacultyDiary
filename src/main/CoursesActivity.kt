package com.maseria.diary

import android.content.Context
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class CoursesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val courses = parseCourses(this,"example.xml")
        // Find the ListView
        val lvCourses = findViewById<ListView>(R.id.lv_courses)

        // Create a list of maps for the SimpleAdapter
        val courseList = courses.map { course ->
            mapOf(
                "courseCode" to course.courseCode,
                "courseTitle" to course.courseTitle,
                "contactHours" to course.contactHours,
                "days" to course.days,
                "time" to course.time
            )
        }

        // Define the keys and the corresponding TextViews in item_course.xml
        val from = arrayOf("courseCode", "courseTitle", "contactHours", "days", "time")
        val to = intArrayOf(R.id.tv_course_code, R.id.tv_course_title, R.id.tv_contact_hours, R.id.tv_days, R.id.tv_time)

        // Create and set the SimpleAdapter
        val adapter = SimpleAdapter(this, courseList, R.layout.item_course, from, to)
        lvCourses.adapter = adapter
    }
    fun parseCourses(context: Context, xmlFileName: String): List<CourseItem> {
        val cours = mutableListOf<CourseItem>()
        try {
            val inputStream: InputStream = context.assets.open(xmlFileName)
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (name.equals("courseItem", ignoreCase = true)) {
                            val courseCode = parser.getAttributeValue(null, "courseCode")
                            val courseTitle = parser.getAttributeValue(null, "courseTitle")
                            val contactHours = parser.getAttributeValue(null, "contactHours")
                            val days = parser.getAttributeValue(null, "days")
                            val time = parser.getAttributeValue(null, "time")
                            cours.add(CourseItem(courseCode, courseTitle, contactHours, days, time))
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cours
    }
}
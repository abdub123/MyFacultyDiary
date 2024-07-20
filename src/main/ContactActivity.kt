package com.maseria.diary

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val contact = parseContact(this,"example.xml")
        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.setText(contact?.name)
        val tvDetails = findViewById<TextView>(R.id.tvDetails)
        tvDetails.setText("Office Addresses : " + contact?.office + "\n" + "Contact Email : " + contact?.email + "\n" + "Office hours : "+ contact?.officeHour)
        val tvCourse = findViewById<TextView>(R.id.tvCourses)
        tvCourse.setText("Courses offered this semester: \n" + contact?.cours?.get(0)?.courseTitle + ": " + contact?.cours?.get(0)?.contactHours)
    }
    fun parseContact(context: Context, xmlFileName: String): Contact? {
        var contact: Contact? = null
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
                        if (name.equals("contact", ignoreCase = true)) {
                            val type = parser.getAttributeValue(null, "type")
                            val name = parser.getAttributeValue(null, "name")
                            val position = parser.getAttributeValue(null, "position")
                            val office = parser.getAttributeValue(null, "office")
                            val email = parser.getAttributeValue(null, "email")
                            val phone = parser.getAttributeValue(null, "phone")
                            val officeHour = parser.getAttributeValue(null, "office_hour")
                            val cours = mutableListOf<CourseItem>()
                            while (parser.next() != XmlPullParser.END_TAG || parser.name != "contact") {
                                if (parser.eventType == XmlPullParser.START_TAG && parser.name == "course") {
                                    val courseName = parser.getAttributeValue(null, "name")
                                    val courseCode = parser.getAttributeValue(null, "courseCode")
                                    cours.add(CourseItem(courseCode, courseName, "", "", ""))
                                }
                            }
                            contact = Contact(type, name, position, office, email, phone, officeHour, cours)
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return contact
    }
}
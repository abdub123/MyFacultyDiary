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

class EventsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_events)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val events = parseEvents(this,"example.xml")
        val lvEvents = findViewById<ListView>(R.id.lvEvents)

        // Create a list of maps for the SimpleAdapter
        val eventList = events.map { event ->
            mapOf(
                "type" to event.type,
                "time" to event.time,
                "day" to event.day,
                "note" to event.note
            )
        }

        // Define the keys and the corresponding TextViews in item_event.xml
        val from = arrayOf("type", "time", "day", "note")
        val to = intArrayOf(R.id.tv_event_type, R.id.tv_event_time, R.id.tv_event_day, R.id.tv_event_note)

        // Create and set the SimpleAdapter
        val adapter = SimpleAdapter(this, eventList, R.layout.event_item, from, to)
        lvEvents.adapter = adapter
    }
    fun parseEvents(context: Context, xmlFileName: String): List<Event> {
        val events = mutableListOf<Event>()
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
                        if (name.equals("event", ignoreCase = true)) {
                            val type = parser.getAttributeValue(null, "type")
                            val time = parser.getAttributeValue(null, "time")
                            val day = parser.getAttributeValue(null, "day")
                            val note = parser.getAttributeValue(null, "note")
                            events.add(Event(type, time, day, note))
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return events
    }
}
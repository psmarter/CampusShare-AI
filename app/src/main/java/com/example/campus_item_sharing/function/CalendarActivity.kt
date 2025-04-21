//package com.example.campus_item_sharing.function
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import biweekly.Biweekly
//import com.alamkanak.weekview.WeekView
//import com.alamkanak.weekview.WeekViewEntity
//import com.example.campus_item_sharing.databinding.ActivityCalendarBinding
//import java.util.Calendar
//import java.util.TimeZone
//import java.util.UUID
//
//class CalendarActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityCalendarBinding
//    private lateinit var eventAdapter: WeekView.SimpleAdapter<EventData>
//    private val allEvents = mutableListOf<EventData>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCalendarBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 1. 初始化 adapter
//        eventAdapter = object : WeekView.SimpleAdapter<EventData>() {
//            override fun onCreateEntity(item: EventData): WeekViewEntity {
//                return WeekViewEntity.Event.Builder(item)
//                    .setId(item.id)
//                    .setTitle(item.title)
//                    .setSubtitle(item.location)
//                    .setStartTime(item.startTime)
//                    .setEndTime(item.endTime)
//                    .build()
//            }
//        }
//
//        // 2. 绑定给 WeekView
//        binding.weekView.adapter = eventAdapter
//
//        // 3. 解析 ICS 并提交列表（没有回调）
//        loadEventsFromICS()
//        eventAdapter.submitList(allEvents)
//    }
//
//    private fun loadEventsFromICS() {
//        // 从 assets 文件夹中读取 calendar.ics
//        val input = assets.open("calendar.ics")
//
//        // 注意：使用 Biweekly.parse(...).first() 而不是 firstOrNull()
//        val calendar = Biweekly.parse(input).first()
//        val tz = TimeZone.getTimeZone("Asia/Shanghai")
//
//        // 遍历所有 VEVENT，构造 EventData
//        for (evt in calendar.events) {
//            val title = evt.summary?.value ?: continue
//            val location = evt.location?.value ?: ""
//            val startDate = evt.dateStart?.value ?: continue
//            val endDate   = evt.dateEnd?.value   ?: continue
//
//            val startCal = Calendar.getInstance(tz).apply { time = startDate }
//            val endCal   = Calendar.getInstance(tz).apply { time = endDate   }
//
//            allEvents += EventData(
//                id        = UUID.randomUUID().mostSignificantBits,
//                title     = title,
//                location  = location,
//                startTime = startCal,
//                endTime   = endCal
//            )
//        }
//    }
//}
package com.example.campus_item_sharing.function

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import biweekly.Biweekly
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity
import com.example.campus_item_sharing.databinding.ActivityCalendarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var eventAdapter: WeekView.SimpleAdapter<EventData>
    private val allEvents = mutableListOf<EventData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 初始化 Adapter，注意签名中多了 Context
        eventAdapter = object : WeekView.SimpleAdapter<EventData>() {
            override fun onCreateEntity(item: EventData): WeekViewEntity {
                return WeekViewEntity.Event.Builder(item)
                    .setId(item.id)
                    .setTitle(item.title)
                    .setSubtitle(item.location)
                    .setStartTime((item.startTime.clone() as Calendar).apply {
                        add(Calendar.HOUR_OF_DAY, 8)
                    })
                    .setEndTime((item.endTime.clone() as Calendar).apply {
                        add(Calendar.HOUR_OF_DAY, 8)
                    })
                    .build()
            }
        }

        // 2. 绑定给 WeekView
        binding.weekView.adapter = eventAdapter

        // 3. 在代码中设置可见时段和“当前时刻”指示器
       // binding.weekView.firstVisibleHour = 8f
       // binding.weekView.lastVisibleHour = 20f
        binding.weekView.showNowLine = true
        binding.weekView.nowLineColor = ContextCompat.getColor(this, android.R.color.holo_red_light)
        // headerPadding 以 px 为单位，这里把 8dp 转为 px
        val paddingPx = (8 * resources.displayMetrics.density).toInt()
        binding.weekView.headerPadding = paddingPx

        // 4. 异步加载 ICS 事件并提交
        loadEvents()
    }

    private fun loadEvents() {
        lifecycleScope.launch(Dispatchers.IO) {
            val events = mutableListOf<EventData>()
            assets.open("calendar.ics").use { input ->
                val calendar = Biweekly.parse(input).first()
                val tz = TimeZone.getTimeZone("Asia/Shanghai")

                for (evt in calendar.events) {
                    val summary = evt.summary?.value ?: continue
                    val locationRaw = evt.location?.value ?: ""
                    // 把 ICS 中的 "\\n" 转为真正的换行
                    val location = locationRaw.replace("\\\\n", "\n")
                    val startDate = evt.dateStart?.value ?: continue
                    val endDate   = evt.dateEnd?.value   ?: continue

                    val startCal = Calendar.getInstance(tz).apply { time = startDate }
                    val endCal   = Calendar.getInstance(tz).apply { time = endDate }

                    // 用 ICS 的 UID 生成稳定的 ID
                    val id = evt.uid?.value.hashCode().toLong()

                    events += EventData(
                        id        = id,
                        title     = summary,
                        location  = location,
                        startTime = startCal,
                        endTime   = endCal
                    )
                }
            }

            withContext(Dispatchers.Main) {
                allEvents.clear()
                allEvents.addAll(events)
                eventAdapter.submitList(allEvents.toList())
            }
        }
    }
}
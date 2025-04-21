package com.example.campus_item_sharing.function

//data class EventData(
//    val id: Long,
//    val title: String,
//    val location: String,
//    val startTime: Calendar,
//    val endTime: Calendar
//)
//
//fun EventData.toWeekEntity(): WeekViewEntity {
//    return WeekViewEntity.Event.Builder(this)
//        .setId(id)
//        .setTitle(title)
//        .setSubtitle(location)
//        .setStartTime(startTime)
//        .setEndTime(endTime)
//        .build()
//}

import java.util.Calendar

/**
 * 只保留业务字段，WeekViewEntity 由 Adapter 负责创建。
 */
data class EventData(
    val id: Long,
    val title: String,
    val location: String,
    val startTime: Calendar,
    val endTime: Calendar
)


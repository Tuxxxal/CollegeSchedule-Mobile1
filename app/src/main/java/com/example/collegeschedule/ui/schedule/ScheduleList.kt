package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collegeschedule.data.dto.ScheduleByDateDto

@Composable
private fun LessonIcon(lessonNumber: Int) {
    val icon = when (lessonNumber) {
        1, 2 -> Icons.Default.Book
        3, 4 -> Icons.Default.Description
        5, 6 -> Icons.Default.Science
        else -> Icons.Default.Schedule
    }
    Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
}

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    // ✅ Цвета корпусов — только внутри @Composable
    val buildingColors = mapOf(
        "Главный корпус" to MaterialTheme.colorScheme.primary,
        "Учебный корпус" to MaterialTheme.colorScheme.secondary,
        "Лабораторный корпус" to MaterialTheme.colorScheme.tertiary,
        "Спортивный комплекс" to Color(0xFF4CAF50),
        "Административный корпус" to Color(0xFF9C27B0)
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(data) { day ->
            // Заголовок дня
            Text(
                text = "${day.lessonDate} (${day.weekday})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (day.lessons.isEmpty()) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(32.dp))
                        Text("Нет занятий", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                day.lessons.forEach { lesson ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            // Строка пары
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                LessonIcon(lesson.lessonNumber)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Пара ${lesson.lessonNumber}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = lesson.time,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    modifier = Modifier.wrapContentWidth(Alignment.End)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Подгруппы
                            lesson.groupParts.forEach { (part, info) ->
                                if (info != null) {
                                    val buildingColor = buildingColors[info.building] ?: MaterialTheme.colorScheme.onSurfaceVariant

                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        // Цветная полоска слева
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .height(48.dp)
                                                .background(buildingColor)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = info.subject,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                            )
                                            Text(
                                                text = "${info.teacher} • ${info.classroom}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                            Text(
                                                text = info.building,
                                                style = MaterialTheme.typography.labelSmall.copy(color = buildingColor),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }

                                        // Подгруппа справа — через Box + wrapContentHeight
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .wrapContentHeight(Alignment.Bottom)
                                        ) {
                                            Text(
                                                text = part.name,
                                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
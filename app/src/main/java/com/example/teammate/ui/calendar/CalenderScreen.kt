package com.example.teammate.ui.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammate.ui.theme.BorderLight
import com.example.teammate.ui.theme.CardWhite
import com.example.teammate.ui.theme.DarkGreen
import com.example.teammate.ui.theme.LightGreenBg
import com.example.teammate.ui.theme.SoftGreenAccent
import com.example.teammate.ui.theme.StatusErrorBg
import com.example.teammate.ui.theme.StatusErrorText
import com.example.teammate.ui.theme.StatusInfoBg
import com.example.teammate.ui.theme.StatusInfoText
import com.example.teammate.ui.theme.StatusWarningBg
import com.example.teammate.ui.theme.StatusWarningText
import com.example.teammate.ui.theme.SurfaceVariantLight
import com.example.teammate.ui.theme.TextPrimary
import com.example.teammate.ui.theme.TextSecondary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

enum class CalendarEventType(
    val label: String,
    val dotColor: Color,
    val chipBg: Color,
    val chipText: Color
) {
    TASK_DEADLINE(
        label = "Task deadlines",
        dotColor = SoftGreenAccent,
        chipBg = LightGreenBg,
        chipText = DarkGreen
    ),
    MEETING(
        label = "Meetings",
        dotColor = StatusInfoText,
        chipBg = StatusInfoBg,
        chipText = StatusInfoText
    ),
    MILESTONE(
        label = "Milestones",
        dotColor = StatusWarningText,
        chipBg = StatusWarningBg,
        chipText = StatusWarningText
    ),
    REMINDER(
        label = "Reminders",
        dotColor = StatusErrorText,
        chipBg = StatusErrorBg,
        chipText = StatusErrorText
    )
}

data class CalendarAgendaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: LocalDate,
    val type: CalendarEventType
)

data class CalendarUiState(
    val visibleMonth: YearMonth = YearMonth.of(2026, Month.OCTOBER),
    val selectedDate: LocalDate = LocalDate.of(2026, Month.OCTOBER, 25),
    val activeFilter: CalendarEventType? = null,
    val events: List<CalendarAgendaItem> = sampleTeamEvents()
)

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun previousMonth() {
        _uiState.update { state ->
            val nextMonth = state.visibleMonth.minusMonths(1)
            val clampedDay = state.selectedDate.dayOfMonth.coerceAtMost(nextMonth.lengthOfMonth())
            state.copy(
                visibleMonth = nextMonth,
                selectedDate = nextMonth.atDay(clampedDay)
            )
        }
    }

    fun nextMonth() {
        _uiState.update { state ->
            val nextMonth = state.visibleMonth.plusMonths(1)
            val clampedDay = state.selectedDate.dayOfMonth.coerceAtMost(nextMonth.lengthOfMonth())
            state.copy(
                visibleMonth = nextMonth,
                selectedDate = nextMonth.atDay(clampedDay)
            )
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                selectedDate = date,
                visibleMonth = YearMonth.from(date)
            )
        }
    }

    fun toggleFilter(type: CalendarEventType) {
        _uiState.update { state ->
            state.copy(activeFilter = if (state.activeFilter == type) null else type)
        }
    }

    fun addEvent(item: CalendarAgendaItem) {
        _uiState.update { state ->
            state.copy(events = state.events + item)
        }
    }
}

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    CalendarScreenContent(
        state = state,
        onPreviousMonth = viewModel::previousMonth,
        onNextMonth = viewModel::nextMonth,
        onSelectDate = viewModel::selectDate,
        onToggleFilter = viewModel::toggleFilter,
        modifier = modifier
    )
}

@Composable
fun CalendarScreenContent(
    state: CalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onToggleFilter: (CalendarEventType) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredEvents by remember(state.events, state.activeFilter) {
        derivedStateOf {
            state.activeFilter?.let { filter ->
                state.events.filter { it.type == filter }
            } ?: state.events
        }
    }

    val eventsByDate by remember(filteredEvents) {
        derivedStateOf {
            filteredEvents.groupBy { it.date }
        }
    }

    val selectedDayEvents = eventsByDate[state.selectedDate].orEmpty()

    val monthTitle = remember(state.visibleMonth) {
        val monthName = state.visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        "$monthName ${state.visibleMonth.year}"
    }

    val selectedDateTitle = remember(state.selectedDate) {
        val formatter = DateTimeFormatter.ofPattern("MMMM d", Locale.ENGLISH)
        state.selectedDate.format(formatter)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceVariantLight),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = monthTitle,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPreviousMonth) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = "Previous month",
                            tint = TextPrimary
                        )
                    }
                    IconButton(onClick = onNextMonth) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Next month",
                            tint = TextPrimary
                        )
                    }
                }
            }
        }

        item {
            CalendarMonthCard(
                visibleMonth = state.visibleMonth,
                selectedDate = state.selectedDate,
                eventsByDate = eventsByDate,
                onSelectDate = onSelectDate
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(CalendarEventType.entries, key = { it.name }) { type ->
                    val isSelected = state.activeFilter == type
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) type.chipText else type.chipBg,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) type.chipText else Color.Transparent
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onToggleFilter(type) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) CardWhite else type.dotColor)
                            )
                            Text(
                                text = type.label,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isSelected) CardWhite else type.chipText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDateTitle,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Text(
                    text = "${selectedDayEvents.size} ${if (selectedDayEvents.size == 1) "item" else "items"}",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        if (selectedDayEvents.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CardWhite,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No scheduled items for $selectedDateTitle.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        } else {
            items(selectedDayEvents, key = { it.id }) { event ->
                AgendaEventCard(event = event)
            }
        }
    }
}

@Composable
private fun CalendarMonthCard(
    visibleMonth: YearMonth,
    selectedDate: LocalDate,
    eventsByDate: Map<LocalDate, List<CalendarAgendaItem>>,
    onSelectDate: (LocalDate) -> Unit
) {
    val weekHeaders = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    val daysInMonth = visibleMonth.lengthOfMonth()
    val firstDayOfWeek = visibleMonth.atDay(1).dayOfWeek
    val leadingBlanks = firstDayOfWeek.value % 7
    val totalCells = leadingBlanks + daysInMonth
    val rowCount = (totalCells + 6) / 7

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = CardWhite,
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                weekHeaders.forEach { dow ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dow.getDisplayName(TextStyle.NARROW, Locale.ENGLISH),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            for (rowIndex in 0 until rowCount) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (colIndex in 0 until 7) {
                        val cellIndex = rowIndex * 7 + colIndex
                        val dayNumber = cellIndex - leadingBlanks + 1

                        if (dayNumber !in 1..daysInMonth) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                        } else {
                            val date = visibleMonth.atDay(dayNumber)
                            val isSelected = date == selectedDate
                            val dayEvents = eventsByDate[date].orEmpty()
                            val primaryDotColor = dayEvents.firstOrNull()?.type?.dotColor

                            DayCell(
                                dayNumber = dayNumber,
                                isSelected = isSelected,
                                dotColor = primaryDotColor,
                                onClick = { onSelectDate(date) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    dayNumber: Int,
    isSelected: Boolean,
    dotColor: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isSelected) SoftGreenAccent else Color.Transparent)
                .then(
                    if (!isSelected && dotColor != null) {
                        Modifier.border(1.dp, dotColor.copy(alpha = 0.25f), CircleShape)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayNumber.toString(),
                fontFamily = FontFamily.SansSerif,
                fontWeight = if (isSelected || dotColor != null) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                color = when {
                    isSelected -> CardWhite
                    dotColor != null -> dotColor
                    else -> TextPrimary
                }
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isSelected -> DarkGreen
                        dotColor != null -> dotColor
                        else -> Color.Transparent
                    }
                )
        )
    }
}

@Composable
private fun AgendaEventCard(event: CalendarAgendaItem) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CardWhite,
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(event.type.dotColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = event.title,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = event.subtitle,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun sampleTeamEvents(): List<CalendarAgendaItem> = listOf(
    CalendarAgendaItem(
        id = "1",
        title = "Work on slides",
        subtitle = "Task · TeamMate deck",
        date = LocalDate.of(2026, Month.OCTOBER, 25),
        type = CalendarEventType.TASK_DEADLINE
    ),
    CalendarAgendaItem(
        id = "2",
        title = "Team sync call",
        subtitle = "Meeting · Sprint check-in",
        date = LocalDate.of(2026, Month.OCTOBER, 25),
        type = CalendarEventType.MEETING
    ),
    CalendarAgendaItem(
        id = "3",
        title = "Submit navigation PR",
        subtitle = "Task · AppNavigation review",
        date = LocalDate.of(2026, Month.OCTOBER, 14),
        type = CalendarEventType.TASK_DEADLINE
    ),
    CalendarAgendaItem(
        id = "4",
        title = "Design token audit",
        subtitle = "Milestone · UI polish",
        date = LocalDate.of(2026, Month.OCTOBER, 22),
        type = CalendarEventType.MILESTONE
    ),
    CalendarAgendaItem(
        id = "5",
        title = "Release candidate freeze",
        subtitle = "Reminder · Merge to develop",
        date = LocalDate.of(2026, Month.OCTOBER, 23),
        type = CalendarEventType.REMINDER
    ),
    CalendarAgendaItem(
        id = "6",
        title = "QA regression pass",
        subtitle = "Task · Pixel 9 emulator check",
        date = LocalDate.of(2026, Month.OCTOBER, 27),
        type = CalendarEventType.TASK_DEADLINE
    )
)

@Preview(showBackground = true, widthDp = 390, heightDp = 820)
@Composable
private fun CalendarScreenPreview() {
    var previewState by remember { mutableStateOf(CalendarUiState()) }

    CalendarScreenContent(
        state = previewState,
        onPreviousMonth = {
            val next = previewState.visibleMonth.minusMonths(1)
            previewState = previewState.copy(
                visibleMonth = next,
                selectedDate = next.atDay(previewState.selectedDate.dayOfMonth.coerceAtMost(next.lengthOfMonth()))
            )
        },
        onNextMonth = {
            val next = previewState.visibleMonth.plusMonths(1)
            previewState = previewState.copy(
                visibleMonth = next,
                selectedDate = next.atDay(previewState.selectedDate.dayOfMonth.coerceAtMost(next.lengthOfMonth()))
            )
        },
        onSelectDate = { date ->
            previewState = previewState.copy(selectedDate = date, visibleMonth = YearMonth.from(date))
        },
        onToggleFilter = { type ->
            previewState = previewState.copy(
                activeFilter = if (previewState.activeFilter == type) null else type
            )
        }
    )
}


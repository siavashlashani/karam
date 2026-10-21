package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.RoutineEntity
import com.example.data.RoutineFrequency
import com.example.data.TaskCategory
import com.example.data.TaskEntity
import com.example.data.pastelCardPalette
import com.example.ui.theme.InkDark
import com.example.ui.theme.SkyBluePrimary
import com.example.util.persianWeekdaysShort
import com.example.util.toPersianDigits
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditTaskBottomSheet(
    initialTask: TaskEntity?,
    defaultCategory: TaskCategory = TaskCategory.DAILY,
    onDismiss: () -> Unit,
    onSave: (TaskEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf(initialTask?.title ?: "") }
    var subtitle by remember { mutableStateOf(initialTask?.subtitle ?: "") }
    var selectedCategory by remember {
        mutableStateOf(initialTask?.category ?: defaultCategory)
    }
    var timeLabel by remember { mutableStateOf(initialTask?.timeLabel ?: "۱۰:۰۰ صبح") }
    var extraInfo by remember { mutableStateOf(initialTask?.extraInfo ?: "") }
    var totalSteps by remember { mutableIntStateOf(initialTask?.totalSteps ?: 1) }
    var selectedColorIndex by remember {
        mutableIntStateOf(initialTask?.colorIndex ?: defaultCategory.defaultCardColorIndex)
    }
    var isStarred by remember { mutableStateOf(initialTask?.isStarred ?: false) }
    var showTitleError by remember { mutableStateOf(false) }

    val quickTimeSuggestions = listOf(
        "۰۸:۰۰ صبح",
        "۱۱:۳۰ صبح",
        "۱۴:۰۰ ظهر",
        "۱۷:۳۰ عصر",
        "۲۱:۰۰ شب",
        "۲۳:۰۰ شب"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (initialTask == null) "افزودن کار جدید ✨" else "ویرایش کار 🎨",
                    style = MaterialTheme.typography.headlineMedium,
                    color = InkDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "بستن")
                }
            }

            // Category selector pills
            Text(
                text = "دسته‌بندی کار:",
                style = MaterialTheme.typography.labelLarge,
                color = InkDark
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskCategory.entries.forEach { category ->
                    val selected = category == selectedCategory
                    FilterChip(
                        selected = selected,
                        onClick = {
                            selectedCategory = category
                            if (initialTask == null) {
                                selectedColorIndex = category.defaultCardColorIndex
                            }
                        },
                        label = {
                            Text(
                                text = category.shortTitle,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = category.iconVector(),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (selected) Color.White else category.primaryColor
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = category.primaryColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    showTitleError = false
                },
                label = { Text("عنوان کار (مثلاً: قرص ویتامین، قبض برق...)") },
                isError = showTitleError,
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_title_input")
            )
            if (showTitleError) {
                Text(
                    text = "لطفاً عنوان کار را وارد کنید.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("توضیحات تکمیلی") },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_subtitle_input")
            )

            // Dynamic category-specific field (Dosage, Amount in Toman, Doctor/Location, Phone, Alarm tone)
            OutlinedTextField(
                value = extraInfo,
                onValueChange = { extraInfo = it },
                label = { Text(selectedCategory.extraFieldLabel) },
                placeholder = { Text(selectedCategory.extraFieldPlaceholder) },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_extra_input")
            )

            // Time selection
            OutlinedTextField(
                value = timeLabel,
                onValueChange = { timeLabel = it },
                label = { Text("ساعت یا زمان یادآوری") },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickTimeSuggestions.forEach { preset ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (timeLabel == preset) SkyBluePrimary else Color(0xFFEEF6FF),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { timeLabel = preset }
                    ) {
                        Text(
                            text = preset,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (timeLabel == preset) Color.White else InkDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Steps count slider
            Column {
                Text(
                    text = "تعداد مراحل انجام: ${totalSteps.toPersianDigits()} مرحله",
                    style = MaterialTheme.typography.labelLarge,
                    color = InkDark
                )
                Slider(
                    value = totalSteps.toFloat(),
                    onValueChange = { totalSteps = it.roundToInt().coerceIn(1, 10) },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = SkyBluePrimary,
                        activeTrackColor = SkyBluePrimary
                    )
                )
            }

            // Pastel card color selector
            Text(
                text = "رنگ کارت:",
                style = MaterialTheme.typography.labelLarge,
                color = InkDark
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pastelCardPalette.forEachIndexed { idx, color ->
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColorIndex == idx) 3.dp else 0.dp,
                                color = InkDark,
                                shape = CircleShape
                            )
                            .clickable { selectedColorIndex = idx },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedColorIndex == idx) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        showTitleError = true
                        return@Button
                    }
                    val updated = TaskEntity(
                        id = initialTask?.id ?: 0,
                        title = title.trim(),
                        subtitle = subtitle.trim().ifEmpty { selectedCategory.description },
                        categoryName = selectedCategory.name,
                        timeLabel = timeLabel.trim().ifEmpty { "امروز" },
                        extraInfo = extraInfo.trim(),
                        totalSteps = totalSteps,
                        completedSteps = initialTask?.completedSteps?.coerceAtMost(totalSteps) ?: 0,
                        isCompleted = initialTask?.isCompleted ?: false,
                        isStarred = isStarred,
                        colorIndex = selectedColorIndex
                    )
                    onSave(updated)
                },
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_task_button")
            ) {
                Text(
                    text = if (initialTask == null) "ثبت و ذخیره کار" else "ذخیره تغییرات",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditRoutineBottomSheet(
    initialRoutine: RoutineEntity?,
    onDismiss: () -> Unit,
    onSave: (RoutineEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf(initialRoutine?.title ?: "") }
    var subtitle by remember { mutableStateOf(initialRoutine?.subtitle ?: "") }
    var selectedCategory by remember {
        mutableStateOf(initialRoutine?.category ?: TaskCategory.DAILY)
    }
    var selectedFrequency by remember {
        mutableStateOf(initialRoutine?.frequency ?: RoutineFrequency.DAILY)
    }
    var timeOfDay by remember { mutableStateOf(initialRoutine?.timeOfDay ?: "۰۸:۰۰ صبح") }
    val selectedDays = remember {
        mutableStateListOf<Int>().apply {
            addAll(initialRoutine?.scheduledDays ?: setOf(0, 1, 2, 3, 4, 5, 6))
        }
    }
    var selectedColorIndex by remember {
        mutableIntStateOf(initialRoutine?.colorIndex ?: 0)
    }
    var showError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (initialRoutine == null) "ساخت روتین تکرارشونده 🔄" else "ویرایش روتین 🔄",
                    style = MaterialTheme.typography.headlineMedium,
                    color = InkDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "بستن")
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    showError = false
                },
                label = { Text("عنوان روتین (مثلاً: ورزش صبحگاهی، مطالعه شبانه)") },
                isError = showError,
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("routine_title_input")
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("هدف یا توضیح کوتاه") },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "نوع تکرار:",
                style = MaterialTheme.typography.labelLarge,
                color = InkDark
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RoutineFrequency.entries.forEach { freq ->
                    val isSelected = freq == selectedFrequency
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFrequency = freq },
                        label = { Text(freq.persianTitle) },
                        shape = RoundedCornerShape(18.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SkyBluePrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text(
                text = "روزهای هفته:",
                style = MaterialTheme.typography.labelLarge,
                color = InkDark
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                persianWeekdaysShort.forEachIndexed { idx, label ->
                    val isChosen = selectedDays.contains(idx)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isChosen) SkyBluePrimary else Color(0xFFEAF3FC))
                            .clickable {
                                if (isChosen && selectedDays.size > 1) {
                                    selectedDays.remove(idx)
                                } else if (!isChosen) {
                                    selectedDays.add(idx)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isChosen) Color.White else InkDark
                        )
                    }
                }
            }

            OutlinedTextField(
                value = timeOfDay,
                onValueChange = { timeOfDay = it },
                label = { Text("ساعت اجرای روتین") },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "رنگ کارت روتین:",
                style = MaterialTheme.typography.labelLarge,
                color = InkDark
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                pastelCardPalette.forEachIndexed { idx, color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColorIndex == idx) 3.dp else 0.dp,
                                color = InkDark,
                                shape = CircleShape
                            )
                            .clickable { selectedColorIndex = idx },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedColorIndex == idx) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (title.isBlank()) {
                        showError = true
                        return@Button
                    }
                    val saved = RoutineEntity(
                        id = initialRoutine?.id ?: 0,
                        title = title.trim(),
                        subtitle = subtitle.trim().ifEmpty { "برنامه منظم و پیوسته" },
                        categoryName = selectedCategory.name,
                        frequencyName = selectedFrequency.name,
                        timeOfDay = timeOfDay.trim(),
                        daysOfWeekCsv = selectedDays.sorted().joinToString(","),
                        completedDaysCsv = initialRoutine?.completedDaysCsv ?: "",
                        streakCount = initialRoutine?.streakCount ?: 1,
                        isDoneToday = initialRoutine?.isDoneToday ?: false,
                        colorIndex = selectedColorIndex
                    )
                    onSave(saved)
                },
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_routine_button")
            ) {
                Text(
                    text = if (initialRoutine == null) "ایجاد روتین" else "ذخیره تغییرات روتین",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProfileEditDialog(
    currentName: String,
    currentAvatarIndex: Int,
    onDismiss: () -> Unit,
    onSave: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var selectedAvatar by remember { mutableIntStateOf(currentAvatarIndex) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "پروفایل کارام من 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = InkDark
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (0..3).forEach { idx ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(
                                    width = if (selectedAvatar == idx) 3.dp else 0.dp,
                                    color = SkyBluePrimary,
                                    shape = CircleShape
                                )
                                .clickable { selectedAvatar = idx }
                                .padding(3.dp)
                        ) {
                            CuteUserAvatar(avatarIndex = idx, size = 56.dp)
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("نام شما") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("انصراف")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(name.trim().ifEmpty { "دوست خوبم" }, selectedAvatar)
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Text(
                            text = "ذخیره",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

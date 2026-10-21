package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.EventRepeat
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TaskCategory
import com.example.data.TaskEntity
import com.example.data.TaskStatusFilter
import com.example.ui.KaramUiState
import com.example.ui.components.CategoryCharacterIllustration
import com.example.ui.components.CategoryPillButton
import com.example.ui.components.CuteUserAvatar
import com.example.ui.components.DailyHeroBanner
import com.example.ui.components.HorizontalIllustratedTaskCard
import com.example.ui.components.StatusFilterPillRow
import com.example.ui.components.VerticalCategoryShowcaseCard
import com.example.ui.components.iconVector
import com.example.ui.theme.CardPeriwinkle
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkLight
import com.example.ui.theme.InkMedium
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SoftIceBackground
import com.example.util.getTodayPersianDateString
import com.example.util.toPersianDigits

@Composable
fun HomeTabContent(
    state: KaramUiState,
    onSearchChange: (String) -> Unit,
    onSelectCategory: (TaskCategory?) -> Unit,
    onOpenCategoryScreen: (TaskCategory) -> Unit,
    onOpenAllTasks: () -> Unit,
    onOpenRoutines: () -> Unit,
    onQuickAddTask: (TaskCategory) -> Unit,
    onToggleTaskComplete: (TaskEntity) -> Unit,
    onIncrementTaskStep: (TaskEntity) -> Unit,
    onToggleTaskStar: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onEditProfile: () -> Unit
) {
    val todayPersianDate = remember { getTodayPersianDateString() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 22.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "سلام، وقتت بخیر ✨ • $todayPersianDate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = state.userName,
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                                color = InkDark
                            )
                            Text(
                                text = "👋",
                                fontSize = 26.sp
                            )
                        }
                    }

                    CuteUserAvatar(
                        avatarIndex = state.avatarIndex,
                        size = 56.dp,
                        modifier = Modifier
                            .clickable { onEditProfile() }
                            .testTag("profile_avatar_button")
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BasicTextField(
                            value = state.searchQuery,
                            onValueChange = onSearchChange,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = InkDark),
                            cursorBrush = SolidColor(SkyBluePrimary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_search_input"),
                            decorationBox = { innerTextField ->
                                if (state.searchQuery.isEmpty()) {
                                    Text(
                                        text = "جستجو در کارها، داروها، پرداختی‌ها...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = InkLight
                                    )
                                }
                                innerTextField()
                            }
                        )
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchChange("") },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "پاک کردن جستجو",
                                    tint = InkMedium
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "جستجو",
                                tint = InkDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenAllTasks() }
                        .testTag("home_filter_button"),
                    shape = RoundedCornerShape(16.dp),
                    color = SkyBluePrimary,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Tune,
                            contentDescription = "فیلتر و مشاهده همه",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "دسته‌بندی کارها",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = InkDark
                    )
                    Text(
                        text = "پیشرفت امروز: ${state.dailyProgressPercent.toPersianDigits()}٪",
                        style = MaterialTheme.typography.labelLarge,
                        color = SkyBluePrimary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryPillButton(
                        title = "همه موضوعات",
                        count = state.allTasks.size,
                        icon = Icons.Rounded.AutoAwesome,
                        badgeColor = SkyBluePrimary,
                        isSelected = state.selectedCategory == null,
                        onClick = { onSelectCategory(null) },
                        modifier = Modifier.weight(1f),
                        testTag = "pill_all_categories"
                    )
                    CategoryPillButton(
                        title = TaskCategory.MEDICINE.shortTitle,
                        count = state.allTasks.count { it.category == TaskCategory.MEDICINE },
                        icon = TaskCategory.MEDICINE.iconVector(),
                        badgeColor = TaskCategory.MEDICINE.primaryColor,
                        isSelected = state.selectedCategory == TaskCategory.MEDICINE,
                        onClick = { onSelectCategory(TaskCategory.MEDICINE) },
                        modifier = Modifier.weight(1f),
                        testTag = "pill_medicine"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryPillButton(
                        title = TaskCategory.PAYMENT.shortTitle,
                        count = state.allTasks.count { it.category == TaskCategory.PAYMENT },
                        icon = TaskCategory.PAYMENT.iconVector(),
                        badgeColor = TaskCategory.PAYMENT.primaryColor,
                        isSelected = state.selectedCategory == TaskCategory.PAYMENT,
                        onClick = { onSelectCategory(TaskCategory.PAYMENT) },
                        modifier = Modifier.weight(1f),
                        testTag = "pill_payment"
                    )
                    CategoryPillButton(
                        title = TaskCategory.APPOINTMENT.shortTitle,
                        count = state.allTasks.count { it.category == TaskCategory.APPOINTMENT },
                        icon = TaskCategory.APPOINTMENT.iconVector(),
                        badgeColor = TaskCategory.APPOINTMENT.primaryColor,
                        isSelected = state.selectedCategory == TaskCategory.APPOINTMENT,
                        onClick = { onSelectCategory(TaskCategory.APPOINTMENT) },
                        modifier = Modifier.weight(1f),
                        testTag = "pill_appointment"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryPillButton(
                        title = TaskCategory.CALL.shortTitle,
                        count = state.allTasks.count { it.category == TaskCategory.CALL },
                        icon = TaskCategory.CALL.iconVector(),
                        badgeColor = TaskCategory.CALL.primaryColor,
                        isSelected = state.selectedCategory == TaskCategory.CALL,
                        onClick = { onSelectCategory(TaskCategory.CALL) },
                        modifier = Modifier.weight(1f),
                        testTag = "pill_call"
                    )
                    CategoryPillButton(
                        title = TaskCategory.ALARM.shortTitle,
                        count = state.allTasks.count { it.category == TaskCategory.ALARM },
                        icon = TaskCategory.ALARM.iconVector(),
                        badgeColor = TaskCategory.ALARM.primaryColor,
                        isSelected = state.selectedCategory == TaskCategory.ALARM,
                        onClick = { onSelectCategory(TaskCategory.ALARM) },
                        modifier = Modifier.weight(1f),
                        testTag = "pill_alarm"
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ویترین بخش‌های کارام 🎨",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = InkDark
                    )
                    Text(
                        text = "مشاهده همه",
                        style = MaterialTheme.typography.labelLarge,
                        color = SkyBluePrimary,
                        modifier = Modifier.clickable { onOpenAllTasks() }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(TaskCategory.entries) { category ->
                        val catTasks = state.allTasks.filter { it.category == category }
                        val pending = catTasks.count { !it.isCompleted }
                        val nextTime = catTasks.firstOrNull { !it.isCompleted }?.timeLabel ?: "تکمیل!"
                        VerticalCategoryShowcaseCard(
                            category = category,
                            taskCount = catTasks.size,
                            pendingCount = pending,
                            nextTimeLabel = nextTime,
                            onClick = { onOpenCategoryScreen(category) }
                        )
                    }
                }
            }
        }

        item {
            DailyHeroBanner(
                completedCount = state.completedTasksCount,
                totalCount = state.allTasks.size,
                onQuickAddClick = {
                    onQuickAddTask(state.selectedCategory ?: TaskCategory.DAILY)
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.selectedCategory == null) {
                        "کارهای امروز من (${state.filteredTasks.size.toPersianDigits()})"
                    } else {
                        "کارهای دسته ${state.selectedCategory.persianTitle} (${state.filteredTasks.size.toPersianDigits()})"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = InkDark
                )
                if (state.selectedCategory != null) {
                    Text(
                        text = "نمایش همه دسته‌ها",
                        style = MaterialTheme.typography.labelLarge,
                        color = SkyBluePrimary,
                        modifier = Modifier.clickable { onSelectCategory(null) }
                    )
                }
            }
        }

        if (state.filteredTasks.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "هیچ کاری در این بخش پیدا نشد! 🌸",
                    subtitle = "با زدن دکمه + یک کار تازه و رنگی به برنامه‌هات اضافه کن.",
                    buttonText = "افزودن اولین کار",
                    onButtonClick = {
                        onQuickAddTask(state.selectedCategory ?: TaskCategory.DAILY)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else {
            items(state.filteredTasks, key = { it.id }) { task ->
                HorizontalIllustratedTaskCard(
                    task = task,
                    onToggleComplete = { onToggleTaskComplete(task) },
                    onIncrementStep = { onIncrementTaskStep(task) },
                    onToggleStar = { onToggleTaskStar(task) },
                    onEdit = { onEditTask(task) },
                    onDelete = { onDeleteTask(task) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onOpenRoutines() },
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(CardPeriwinkle.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EventRepeat,
                                contentDescription = null,
                                tint = CardPeriwinkle,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "روتین‌های روزانه و هفتگی من 🔄",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = InkDark
                            )
                            Text(
                                text = "${state.allRoutines.count { it.isDoneToday }.toPersianDigits()} از ${state.allRoutines.size.toPersianDigits()} روتین امروز انجام شده",
                                style = MaterialTheme.typography.bodySmall,
                                color = InkMedium
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SkyBluePrimary
                    ) {
                        Text(
                            text = "مشاهده",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TasksTabContent(
    state: KaramUiState,
    onBackToHome: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelectStatus: (TaskStatusFilter) -> Unit,
    onSelectCategory: (TaskCategory?) -> Unit,
    onQuickAddClick: () -> Unit,
    onToggleTaskComplete: (TaskEntity) -> Unit,
    onIncrementTaskStep: (TaskEntity) -> Unit,
    onToggleTaskStar: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit
) {
    var showSearchBar by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackToHome) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "بازگشت",
                                tint = InkDark
                            )
                        }
                        Text(
                            text = if (state.selectedCategory == null) "کارهای من" else state.selectedCategory.persianTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = InkDark
                        )
                        IconButton(onClick = { showSearchBar = !showSearchBar }) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "جستجو",
                                tint = InkDark
                            )
                        }
                    }

                    if (showSearchBar || state.searchQuery.isNotEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = SoftIceBackground
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null,
                                    tint = InkMedium,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                BasicTextField(
                                    value = state.searchQuery,
                                    onValueChange = onSearchChange,
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = InkDark),
                                    modifier = Modifier.weight(1f),
                                    decorationBox = { inner ->
                                        if (state.searchQuery.isEmpty()) {
                                            Text(
                                                text = "جستجوی عنوان یا جزئیات کار...",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = InkLight
                                            )
                                        }
                                        inner()
                                    }
                                )
                                if (state.searchQuery.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "پاک کردن",
                                        tint = InkMedium,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable { onSearchChange("") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            StatusFilterPillRow(
                selectedFilter = state.statusFilter,
                allCount = state.allTasks.size,
                ongoingCount = state.ongoingTasksCount,
                completedCount = state.completedTasksCount,
                onSelectFilter = onSelectStatus,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAll = state.selectedCategory == null
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isAll) SkyBluePrimary else Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onSelectCategory(null) }
                    ) {
                        Text(
                            text = "همه دسته‌ها",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isAll) Color.White else InkDark,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
                items(TaskCategory.entries) { cat ->
                    val selected = state.selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) cat.primaryColor else Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onSelectCategory(cat) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = cat.iconVector(),
                                contentDescription = null,
                                tint = if (selected) Color.White else cat.primaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = cat.shortTitle,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) Color.White else InkDark
                            )
                        }
                    }
                }
            }
        }

        item {
            DailyHeroBanner(
                completedCount = state.completedTasksCount,
                totalCount = state.allTasks.size,
                onQuickAddClick = onQuickAddClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        if (state.filteredTasks.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "در این فیلتر کاری وجود ندارد 🌱",
                    subtitle = "می‌توانی فیلتر را تغییر بدهی یا همین الان یک کار جدید بسازی.",
                    buttonText = "افزودن کار جدید",
                    onButtonClick = onQuickAddClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else {
            items(state.filteredTasks, key = { it.id }) { task ->
                HorizontalIllustratedTaskCard(
                    task = task,
                    onToggleComplete = { onToggleTaskComplete(task) },
                    onIncrementStep = { onIncrementTaskStep(task) },
                    onToggleStar = { onToggleTaskStar(task) },
                    onEdit = { onEditTask(task) },
                    onDelete = { onDeleteTask(task) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    subtitle: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CategoryCharacterIllustration(
                category = TaskCategory.DAILY,
                modifier = Modifier.size(110.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = InkDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = InkMedium,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onButtonClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(buttonText)
            }
        }
    }
}

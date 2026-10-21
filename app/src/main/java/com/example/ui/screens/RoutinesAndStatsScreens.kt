package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.RoutineEntity
import com.example.data.RoutineFrequency
import com.example.data.TaskCategory
import com.example.data.TaskEntity
import com.example.ui.KaramUiState
import com.example.ui.KaramViewModel
import com.example.ui.components.AddEditRoutineBottomSheet
import com.example.ui.components.AddEditTaskBottomSheet
import com.example.ui.components.CategoryCharacterIllustration
import com.example.ui.components.CategoryCircularIcon
import com.example.ui.components.CuteUserAvatar
import com.example.ui.components.KaramBottomBar
import com.example.ui.components.KaramTab
import com.example.ui.components.ProfileEditDialog
import com.example.ui.components.RoutineIllustratedCard
import com.example.ui.theme.CardCoralPink
import com.example.ui.theme.CardMintGreen
import com.example.ui.theme.CardPeriwinkle
import com.example.ui.theme.CardWarmAmber
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMedium
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SoftIceBackground
import com.example.util.toPersianDigits

@Composable
fun KaramRootScreen(
    viewModel: KaramViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var showTaskSheet by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<TaskEntity?>(null) }
    var defaultCategoryForNewTask by remember { mutableStateOf(TaskCategory.DAILY) }

    var showRoutineSheet by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<RoutineEntity?>(null) }

    var showProfileDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = state.currentTab != KaramTab.HOME) {
        viewModel.selectTab(KaramTab.HOME)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftIceBackground)
    ) {
        val isWideScreen = maxWidth >= 640.dp

        Scaffold(
            containerColor = SoftIceBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (!isWideScreen) {
                    KaramBottomBar(
                        currentTab = state.currentTab,
                        pendingCount = state.ongoingTasksCount,
                        onSelectTab = { viewModel.selectTab(it) }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (state.currentTab == KaramTab.ROUTINES) {
                            editingRoutine = null
                            showRoutineSheet = true
                        } else {
                            editingTask = null
                            defaultCategoryForNewTask = state.selectedCategory ?: TaskCategory.DAILY
                            showTaskSheet = true
                        }
                    },
                    containerColor = SkyBluePrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("fab_add_item")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "افزودن مورد جدید",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = Color.White,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        KaramTab.entries.forEach { tab ->
                            val selected = state.currentTab == tab
                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title
                                    )
                                },
                                label = { Text(tab.title) }
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AnimatedContent(
                        targetState = state.currentTab,
                        label = "tabTransition",
                        modifier = Modifier.widthIn(max = 760.dp)
                    ) { tab ->
                        when (tab) {
                            KaramTab.HOME -> HomeTabContent(
                                state = state,
                                onSearchChange = viewModel::updateSearchQuery,
                                onSelectCategory = viewModel::selectCategory,
                                onOpenCategoryScreen = viewModel::openCategoryTasks,
                                onOpenAllTasks = { viewModel.selectTab(KaramTab.TASKS) },
                                onOpenRoutines = { viewModel.selectTab(KaramTab.ROUTINES) },
                                onQuickAddTask = { cat ->
                                    editingTask = null
                                    defaultCategoryForNewTask = cat
                                    showTaskSheet = true
                                },
                                onToggleTaskComplete = viewModel::toggleTaskCompletion,
                                onIncrementTaskStep = viewModel::incrementTaskStep,
                                onToggleTaskStar = viewModel::toggleTaskStar,
                                onEditTask = { task ->
                                    editingTask = task
                                    showTaskSheet = true
                                },
                                onDeleteTask = viewModel::deleteTask,
                                onEditProfile = { showProfileDialog = true }
                            )

                            KaramTab.TASKS -> TasksTabContent(
                                state = state,
                                onBackToHome = { viewModel.selectTab(KaramTab.HOME) },
                                onSearchChange = viewModel::updateSearchQuery,
                                onSelectStatus = viewModel::selectStatusFilter,
                                onSelectCategory = viewModel::selectCategory,
                                onQuickAddClick = {
                                    editingTask = null
                                    defaultCategoryForNewTask = state.selectedCategory ?: TaskCategory.DAILY
                                    showTaskSheet = true
                                },
                                onToggleTaskComplete = viewModel::toggleTaskCompletion,
                                onIncrementTaskStep = viewModel::incrementTaskStep,
                                onToggleTaskStar = viewModel::toggleTaskStar,
                                onEditTask = { task ->
                                    editingTask = task
                                    showTaskSheet = true
                                },
                                onDeleteTask = viewModel::deleteTask
                            )

                            KaramTab.ROUTINES -> RoutinesTabContent(
                                state = state,
                                onSelectFrequency = viewModel::selectRoutineFrequency,
                                onAddRoutineClick = {
                                    editingRoutine = null
                                    showRoutineSheet = true
                                },
                                onToggleRoutineToday = viewModel::toggleRoutineToday,
                                onToggleRoutineWeekday = viewModel::toggleRoutineWeekday,
                                onEditRoutine = { routine ->
                                    editingRoutine = routine
                                    showRoutineSheet = true
                                },
                                onDeleteRoutine = viewModel::deleteRoutine
                            )

                            KaramTab.CATEGORIES -> CategoriesHubTabContent(
                                state = state,
                                onSelectCategory = { cat ->
                                    viewModel.openCategoryTasks(cat)
                                },
                                onAddForCategory = { cat ->
                                    editingTask = null
                                    defaultCategoryForNewTask = cat
                                    showTaskSheet = true
                                }
                            )

                            KaramTab.STATS -> StatsTabContent(
                                state = state,
                                onEditProfile = { showProfileDialog = true },
                                onResetSampleData = viewModel::resetSampleData
                            )
                        }
                    }
                }
            }
        }
    }

    if (showTaskSheet) {
        AddEditTaskBottomSheet(
            initialTask = editingTask,
            defaultCategory = defaultCategoryForNewTask,
            onDismiss = { showTaskSheet = false },
            onSave = { task ->
                viewModel.saveTask(task)
                showTaskSheet = false
            }
        )
    }

    if (showRoutineSheet) {
        AddEditRoutineBottomSheet(
            initialRoutine = editingRoutine,
            onDismiss = { showRoutineSheet = false },
            onSave = { routine ->
                viewModel.saveRoutine(routine)
                showRoutineSheet = false
            }
        )
    }

    if (showProfileDialog) {
        ProfileEditDialog(
            currentName = state.userName,
            currentAvatarIndex = state.avatarIndex,
            onDismiss = { showProfileDialog = false },
            onSave = { newName, newAvatar ->
                viewModel.updateProfile(newName, newAvatar)
                showProfileDialog = false
            }
        )
    }
}

@Composable
fun RoutinesTabContent(
    state: KaramUiState,
    onSelectFrequency: (RoutineFrequency?) -> Unit,
    onAddRoutineClick: () -> Unit,
    onToggleRoutineToday: (RoutineEntity) -> Unit,
    onToggleRoutineWeekday: (RoutineEntity, Int) -> Unit,
    onEditRoutine: (RoutineEntity) -> Unit,
    onDeleteRoutine: (RoutineEntity) -> Unit
) {
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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "روتین‌های من 🔄",
                                style = MaterialTheme.typography.headlineLarge,
                                color = InkDark
                            )
                            Text(
                                text = "برنامه‌های تکرارشونده روزانه و هفتگی برای ساخت عادت‌های طلایی",
                                style = MaterialTheme.typography.bodySmall,
                                color = InkMedium
                            )
                        }
                        Button(
                            onClick = onAddRoutineClick,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                            modifier = Modifier.testTag("add_routine_header_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("روتین جدید")
                        }
                    }
                }
            }
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAll = state.selectedRoutineFrequency == null
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isAll) SkyBluePrimary else Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onSelectFrequency(null) }
                    ) {
                        Text(
                            text = "همه روتین‌ها (${state.allRoutines.size.toPersianDigits()})",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isAll) Color.White else InkDark,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
                items(RoutineFrequency.entries) { freq ->
                    val isSelected = state.selectedRoutineFrequency == freq
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) SkyBluePrimary else Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onSelectFrequency(freq) }
                    ) {
                        Text(
                            text = freq.persianTitle,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else InkDark,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        item {
            val totalDoneToday = state.allRoutines.count { it.isDoneToday }
            val bestStreak = state.allRoutines.maxOfOrNull { it.streakCount } ?: 0
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardPeriwinkle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "رکورد پیوستگی تو: ${bestStreak.toPersianDigits()} روز پیاپی 🔥",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "امروز ${totalDoneToday.toPersianDigits()} از ${state.allRoutines.size.toPersianDigits()} روتین تکمیل شده است",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD54F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFD84315),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        if (state.filteredRoutines.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "هنوز روتینی در این بخش ثبت نشده 🌈",
                    subtitle = "برنامه‌های روزانه و هفتگی مثل نوشیدن آب، ورزش یا مصرف ویتامین‌ها را اینجا اضافه کن.",
                    buttonText = "ساخت روتین جدید",
                    onButtonClick = onAddRoutineClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else {
            items(state.filteredRoutines, key = { it.id }) { routine ->
                RoutineIllustratedCard(
                    routine = routine,
                    onToggleToday = { onToggleRoutineToday(routine) },
                    onToggleWeekday = { dayIdx -> onToggleRoutineWeekday(routine, dayIdx) },
                    onEdit = { onEditRoutine(routine) },
                    onDelete = { onDeleteRoutine(routine) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun CategoriesHubTabContent(
    state: KaramUiState,
    onSelectCategory: (TaskCategory) -> Unit,
    onAddForCategory: (TaskCategory) -> Unit
) {
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
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "دسته‌بندی‌های اختصاصی کارام 🎨",
                        style = MaterialTheme.typography.headlineLarge,
                        color = InkDark
                    )
                    Text(
                        text = "داروها، پرداختی‌ها، نوبت‌ها، تماس‌ها، زنگ‌ها و کارهای روزمره",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMedium
                    )
                }
            }
        }

        items(TaskCategory.entries) { category ->
            val tasksInCategory = state.allTasks.filter { it.category == category }
            val doneCount = tasksInCategory.count { it.isCompleted }
            val pendingCount = tasksInCategory.size - doneCount

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .clickable { onSelectCategory(category) }
                    .testTag("category_hub_${category.name.lowercase()}"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = category.primaryColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryCharacterIllustration(
                            category = category,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = category.persianTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = category.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "${tasksInCategory.size.toPersianDigits()} کل • ${pendingCount.toPersianDigits()} مانده",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onAddForCategory(category) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = null,
                                        tint = category.primaryColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "افزودن ${category.shortTitle}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = category.primaryColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatsTabContent(
    state: KaramUiState,
    onEditProfile: () -> Unit,
    onResetSampleData: () -> Unit
) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CuteUserAvatar(
                            avatarIndex = state.avatarIndex,
                            size = 56.dp,
                            modifier = Modifier.clickable { onEditProfile() }
                        )
                        Column {
                            Text(
                                text = "گزارش پیشرفت ${state.userName}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = InkDark
                            )
                            Text(
                                text = "برای ویرایش نام یا آواتار روی تصویر بزنید",
                                style = MaterialTheme.typography.bodySmall,
                                color = InkMedium
                            )
                        }
                    }

                    IconButton(onClick = onResetSampleData) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "بازگردانی داده‌های نمونه",
                            tint = SkyBluePrimary
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SkyBluePrimary, CardPeriwinkle)
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "میزان پیشرفت کلی امروز 🌟",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Text(
                                    text = "${state.dailyProgressPercent.toPersianDigits()}٪",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.EmojiEvents,
                                    contentDescription = null,
                                    tint = Color(0xFFFFEA79),
                                    modifier = Modifier.size(38.dp)
                                )
                            }
                        }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatMiniBadge(
                                label = "کارهای انجام‌شده",
                                value = state.completedTasksCount.toPersianDigits(),
                                color = CardMintGreen
                            )
                            StatMiniBadge(
                                label = "کارهای در جریان",
                                value = state.ongoingTasksCount.toPersianDigits(),
                                color = CardWarmAmber
                            )
                            StatMiniBadge(
                                label = "روتین‌های امروز",
                                value = "${state.allRoutines.count { it.isDoneToday }.toPersianDigits()}/${state.allRoutines.size.toPersianDigits()}",
                                color = CardCoralPink
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "وضعیت به تفکیک دسته‌بندی 📊",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = InkDark
                    )

                    TaskCategory.entries.forEach { category ->
                        val catTasks = state.allTasks.filter { it.category == category }
                        val done = catTasks.count { it.isCompleted }
                        val total = catTasks.size
                        val ratio = if (total == 0) 0f else done.toFloat() / total.toFloat()
                        val animatedRatio by animateFloatAsState(
                            targetValue = ratio,
                            animationSpec = spring(stiffness = Spring.StiffnessLow),
                            label = "catRatio"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CategoryCircularIcon(category = category, size = 28.dp)
                                    Text(
                                        text = category.persianTitle,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = InkDark
                                    )
                                }
                                Text(
                                    text = "${done.toPersianDigits()} از ${total.toPersianDigits()} انجام شده",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkMedium
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(category.badgeBgColor)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedRatio)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(category.primaryColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMiniBadge(
    label: String,
    value: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.20f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = "$label: $value",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
}

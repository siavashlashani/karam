package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.AutoAwesomeMosaic
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EventRepeat
import androidx.compose.material.icons.rounded.FolderSpecial
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PendingActions
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.TaskAlt
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RoutineEntity
import com.example.data.TaskCategory
import com.example.data.TaskEntity
import com.example.data.TaskStatusFilter
import com.example.data.getCardColorByIndex
import com.example.ui.theme.CardPeriwinkle
import com.example.ui.theme.CardWarmAmber
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMedium
import com.example.ui.theme.SkyBlueDark
import com.example.ui.theme.SkyBluePrimary
import com.example.util.persianWeekdaysShort
import com.example.util.toPersianDigits

enum class KaramTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("خانه", Icons.Rounded.Home, Icons.Outlined.Home, "nav_tab_home"),
    TASKS("کارهای من", Icons.Rounded.CheckCircle, Icons.Outlined.CheckCircle, "nav_tab_tasks"),
    ROUTINES("روتین‌ها", Icons.Rounded.EventRepeat, Icons.Outlined.EventRepeat, "nav_tab_routines"),
    CATEGORIES("دسته‌بندی", Icons.Rounded.AutoAwesomeMosaic, Icons.Outlined.AutoAwesomeMosaic, "nav_tab_categories"),
    STATS("گزارش", Icons.Rounded.Analytics, Icons.Outlined.Analytics, "nav_tab_stats")
}

/**
 * Bottom navigation bar styled directly after the reference mockup:
 * White rounded container where the active tab is a sky-blue pill with icon + text,
 * and inactive tabs are soft pastel blue-grey icons.
 */
@Composable
fun KaramBottomBar(
    currentTab: KaramTab,
    pendingCount: Int,
    onSelectTab: (KaramTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                ambientColor = SkyBluePrimary.copy(alpha = 0.25f),
                spotColor = SkyBluePrimary.copy(alpha = 0.25f)
            ),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KaramTab.entries.forEach { tab ->
                val isSelected = tab == currentTab
                val pillColor by animateColorAsState(
                    targetValue = if (isSelected) SkyBluePrimary else Color.Transparent,
                    label = "navPillColor"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color(0xFF90B8DC),
                    label = "navContentColor"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(pillColor)
                        .clickable { onSelectTab(tab) }
                        .padding(
                            horizontal = if (isSelected) 16.dp else 12.dp,
                            vertical = 10.dp
                        )
                        .testTag(tab.testTag),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                tint = contentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            if (!isSelected && tab == KaramTab.TASKS && pendingCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .align(Alignment.TopEnd)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF5C77))
                                )
                            }
                        }
                        AnimatedVisibility(visible = isSelected) {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive category pill matching the 2-column pills on the left screen of the reference image:
 * Circular colored icon badge on the right (RTL start) + title + task count.
 */
@Composable
fun CategoryPillButton(
    title: String,
    count: Int,
    icon: ImageVector,
    badgeColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) SkyBluePrimary else Color.White,
        label = "pillContainer"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else InkDark,
        label = "pillText"
    )
    val subTextColor by animateColorAsState(
        targetValue = if (isSelected) Color.White.copy(alpha = 0.85f) else InkMedium,
        label = "pillSubText"
    )

    Surface(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
        tonalElevation = if (isSelected) 6.dp else 1.dp,
        shadowElevation = if (isSelected) 6.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White else badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) SkyBluePrimary else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${count.toPersianDigits()} مورد",
                    style = MaterialTheme.typography.labelSmall,
                    color = subTextColor,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Status filter pills matching the right screen of the reference image:
 * [همه کارها (All)] | [در حال انجام (Ongoing)] | [انجام شده (Completed)]
 */
@Composable
fun StatusFilterPillRow(
    selectedFilter: TaskStatusFilter,
    allCount: Int,
    ongoingCount: Int,
    completedCount: Int,
    onSelectFilter: (TaskStatusFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        val itemsList = listOf(
            Triple(TaskStatusFilter.ALL, allCount, SkyBluePrimary to Icons.Rounded.FolderSpecial),
            Triple(TaskStatusFilter.ONGOING, ongoingCount, CardWarmAmber to Icons.Rounded.PendingActions),
            Triple(TaskStatusFilter.COMPLETED, completedCount, CardPeriwinkle to Icons.Rounded.TaskAlt)
        )
        items(itemsList) { (filter, count, colorAndIcon) ->
            val (badgeColor, iconVec) = colorAndIcon
            val isSelected = selectedFilter == filter
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) SkyBluePrimary else Color.White,
                label = "statusFilterBg"
            )
            val txtColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else InkDark,
                label = "statusFilterTxt"
            )

            Surface(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onSelectFilter(filter) }
                    .testTag("filter_${filter.name.lowercase()}"),
                shape = RoundedCornerShape(24.dp),
                color = bgColor,
                shadowElevation = if (isSelected) 5.dp else 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White else badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVec,
                            contentDescription = filter.persianLabel,
                            tint = if (isSelected) badgeColor else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "${filter.persianLabel} (${count.toPersianDigits()})",
                        style = MaterialTheme.typography.labelLarge,
                        color = txtColor
                    )
                }
            }
        }
    }
}

/**
 * Hero Banner matching the sky-blue banner on the right screen of the reference image:
 * "Find a course you want to learn ! [Check Now]" -> "برنامه امروزت رو بچین و بدرخش! [+ افزودن کار]"
 */
@Composable
fun DailyHeroBanner(
    completedCount: Int,
    totalCount: Int,
    onQuickAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percent = if (totalCount == 0) 100 else ((completedCount * 100) / totalCount)
    val animatedProgress by animateFloatAsState(
        targetValue = (percent / 100f).coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "heroProgress"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(SkyBluePrimary, Color(0xFF62C0FF))
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1.3f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "برنامه امروزت رو\nبا عشق بچین! ✨",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "${completedCount.toPersianDigits()} از ${totalCount.toPersianDigits()} کار انجام شده (${percent.toPersianDigits()}٪)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.30f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFD54F))
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Button(
                        onClick = onQuickAddClick,
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = SkyBlueDark
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("hero_add_task_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "افزودن کار جدید",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                HeroPhoneBannerIllustration(
                    modifier = Modifier
                        .size(width = 120.dp, height = 132.dp)
                )
            }
        }
    }
}

/**
 * Large vertical colorful card matching the left screen of the reference image
 * ("Digital Design Thinking" & "Web Development" cards).
 */
@Composable
fun VerticalCategoryShowcaseCard(
    category: TaskCategory,
    taskCount: Int,
    pendingCount: Int,
    nextTimeLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(205.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .testTag("showcase_card_${category.name.lowercase()}"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = category.primaryColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentAlignment = Alignment.Center
            ) {
                CategoryCharacterIllustration(
                    category = category,
                    modifier = Modifier.size(135.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = category.persianTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.86f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom badges row just like [17 Files] [40 min] in the reference image
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(CardWarmAmber)
                        )
                    }
                    Text(
                        text = "${taskCount.toPersianDigits()} کار",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = category.primaryColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = if (pendingCount > 0) "${pendingCount.toPersianDigits()} مانده" else nextTimeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Full-width colorful horizontal card matching the right screen of the reference image
 * ("Creative Art Design" / "Quilting Courses" / "Powerpoint").
 */
@Composable
fun HorizontalIllustratedTaskCard(
    task: TaskEntity,
    onToggleComplete: () -> Unit,
    onIncrementStep: () -> Unit,
    onToggleStar: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val baseColor = getCardColorByIndex(task.colorIndex)
    val cardColor = if (task.isCompleted) baseColor.copy(alpha = 0.78f) else baseColor
    val progressFraction by animateFloatAsState(
        targetValue = (task.progressPercent / 100f).coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "taskProgress"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable { onIncrementStep() }
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Illustration on one side (matching reference cards)
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryCharacterIllustration(
                        category = task.category,
                        modifier = Modifier.size(86.dp)
                    )
                }

                // Task details & progress on the other side
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category + Time pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.24f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = task.category.iconVector(),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "${task.category.shortTitle} • ${task.timeLabel.toPersianDigits()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                        }

                        // Check / Complete button
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (task.isCompleted) Color.White else Color.White.copy(alpha = 0.25f))
                                .border(2.dp, Color.White, CircleShape)
                                .clickable { onToggleComplete() }
                                .testTag("task_check_${task.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (task.isCompleted) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "تیک زدن کار",
                                    tint = baseColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (task.subtitle.isNotBlank()) {
                        Text(
                            text = task.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.90f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (task.extraInfo.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Black.copy(alpha = 0.14f)
                        ) {
                            Text(
                                text = "${task.category.extraFieldLabel}: ${task.extraInfo.toPersianDigits()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Progress label + bar matching "Completed 70%" in reference image
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (task.isCompleted) {
                                "تکمیل شده ۱۰۰٪ 🎉"
                            } else if (task.totalSteps > 1) {
                                "پیشرفت ${task.progressPercent.toPersianDigits()}٪ (${task.completedSteps.toPersianDigits()}/${task.totalSteps.toPersianDigits()})"
                            } else {
                                "در انتظار انجام"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.30f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressFraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color.White)
                        )
                    }
                }
            }

            // Action bar at bottom of card (Star, Step +, Edit, Delete)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onToggleStar,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = if (task.isStarred) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                        contentDescription = "نشان‌دار کردن",
                        tint = if (task.isStarred) Color(0xFFFFEA79) else Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("task_edit_${task.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "ویرایش کار",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("task_delete_${task.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "حذف کار",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

/**
 * Interactive card for Daily & Weekly Routines with Persian weekday pills (ش، ی، د، س، چ، پ، ج),
 * streak counter, and one-tap today check-in.
 */
@Composable
fun RoutineIllustratedCard(
    routine: RoutineEntity,
    onToggleToday: () -> Unit,
    onToggleWeekday: (Int) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = getCardColorByIndex(routine.colorIndex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("routine_card_${routine.id}"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryCharacterIllustration(
                        category = routine.category,
                        modifier = Modifier.size(68.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "${routine.frequency.persianTitle} • ${routine.timeOfDay.toPersianDigits()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFD54F)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color(0xFFD84315),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${routine.streakCount.toPersianDigits()} روز",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkDark
                                )
                            }
                        }
                    }

                    Text(
                        text = routine.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    if (routine.subtitle.isNotBlank()) {
                        Text(
                            text = routine.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Today Check-in Pill
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onToggleToday() }
                        .testTag("routine_check_today_${routine.id}"),
                    shape = RoundedCornerShape(18.dp),
                    color = if (routine.isDoneToday) Color.White else Color.White.copy(alpha = 0.24f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "تیک امروز",
                            tint = if (routine.isDoneToday) cardColor else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (routine.isDoneToday) "انجام شد" else "ثبت امروز",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (routine.isDoneToday) cardColor else Color.White
                        )
                    }
                }
            }

            // Interactive 7-day Persian weekday tracker (ش تا ج)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    persianWeekdaysShort.forEachIndexed { dayIndex, dayLetter ->
                        val isScheduled = routine.scheduledDays.contains(dayIndex)
                        val isDone = routine.completedDays.contains(dayIndex)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDone -> Color.White
                                        isScheduled -> Color.White.copy(alpha = 0.25f)
                                        else -> Color.Black.copy(alpha = 0.12f)
                                    }
                                )
                                .border(
                                    width = if (isScheduled) 1.5.dp else 0.dp,
                                    color = Color.White,
                                    shape = CircleShape
                                )
                                .clickable { onToggleWeekday(dayIndex) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayLetter,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isDone) cardColor else Color.White
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "ویرایش روتین",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "حذف روتین",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

package com.example.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ui.theme.BadgeAmberBg
import com.example.ui.theme.BadgeBlueBg
import com.example.ui.theme.BadgeGreenBg
import com.example.ui.theme.BadgePinkBg
import com.example.ui.theme.BadgePurpleBg
import com.example.ui.theme.BadgeTealBg
import com.example.ui.theme.CardCoralPink
import com.example.ui.theme.CardMintGreen
import com.example.ui.theme.CardPeriwinkle
import com.example.ui.theme.CardSkyBlue
import com.example.ui.theme.CardTurquoise
import com.example.ui.theme.CardWarmAmber

enum class TaskCategory(
    val persianTitle: String,
    val shortTitle: String,
    val description: String,
    val extraFieldLabel: String,
    val extraFieldPlaceholder: String,
    val primaryColor: Color,
    val badgeBgColor: Color,
    val defaultCardColorIndex: Int
) {
    DAILY(
        persianTitle = "کارهای روزمره",
        shortTitle = "روزمره",
        description = "برنامه‌های شخصی، خرید و کارهای روزانه",
        extraFieldLabel = "یادداشت کوتاه / مکان",
        extraFieldPlaceholder = "مثلاً: اولویت بالا، منزل",
        primaryColor = CardSkyBlue,
        badgeBgColor = BadgeBlueBg,
        defaultCardColorIndex = 0
    ),
    MEDICINE(
        persianTitle = "داروها و سلامت",
        shortTitle = "داروها",
        description = "قرص‌ها، شربت‌ها، ویتامین‌ها و ساعت مصرف",
        extraFieldLabel = "دوز مصرفی و نحوه مصرف",
        extraFieldPlaceholder = "مثلاً: ۱ عدد همراه با ناهار",
        primaryColor = CardTurquoise,
        badgeBgColor = BadgeTealBg,
        defaultCardColorIndex = 2
    ),
    PAYMENT(
        persianTitle = "پرداختی‌ها و قبوض",
        shortTitle = "پرداختی‌ها",
        description = "اقساط، قبوض، اجاره و خریدهای مالی",
        extraFieldLabel = "مبلغ (تومان)",
        extraFieldPlaceholder = "مثلاً: ۸۵۰٬۰۰۰ تومان",
        primaryColor = CardWarmAmber,
        badgeBgColor = BadgeAmberBg,
        defaultCardColorIndex = 3
    ),
    APPOINTMENT(
        persianTitle = "نوبت‌ها و قرارها",
        shortTitle = "نوبت‌ها",
        description = "پزشک، دندان‌پزشکی، آرایشگاه و جلسات",
        extraFieldLabel = "نام پزشک / مکان مراجعه",
        extraFieldPlaceholder = "مثلاً: کلینیک مهر، دکتر رضایی",
        primaryColor = CardPeriwinkle,
        badgeBgColor = BadgePurpleBg,
        defaultCardColorIndex = 1
    ),
    CALL(
        persianTitle = "تماس‌ها و پیام‌ها",
        shortTitle = "تماس‌ها",
        description = "تماس‌های ضروری کاری، خانوادگی و پیگیری‌ها",
        extraFieldLabel = "نام مخاطب یا شماره تماس",
        extraFieldPlaceholder = "مثلاً: مامان / ۰۹۱۲...",
        primaryColor = CardMintGreen,
        badgeBgColor = BadgeGreenBg,
        defaultCardColorIndex = 5
    ),
    ALARM(
        persianTitle = "زنگ‌ها و هشدارها",
        shortTitle = "زنگ‌ها",
        description = "بیدارباش، تایمرها و یادآورهای ساعتی",
        extraFieldLabel = "تکرار و صدای زنگ",
        extraFieldPlaceholder = "مثلاً: هر روز، زنگ ملایم",
        primaryColor = CardCoralPink,
        badgeBgColor = BadgePinkBg,
        defaultCardColorIndex = 4
    );

    companion object {
        fun fromName(name: String): TaskCategory =
            entries.find { it.name == name } ?: DAILY
    }
}

enum class RoutineFrequency(
    val persianTitle: String,
    val badgeText: String
) {
    DAILY("روزانه", "هر روز"),
    WEEKLY("هفتگی", "روزهای منتخب هفته"),
    MORNING("روتین صبحگاهی", "هر صبح"),
    NIGHT("روتین شبانه", "هر شب");

    companion object {
        fun fromName(name: String): RoutineFrequency =
            entries.find { it.name == name } ?: DAILY
    }
}

enum class TaskStatusFilter(val persianLabel: String) {
    ALL("همه کارها"),
    ONGOING("در حال انجام"),
    COMPLETED("انجام شده")
}

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String,
    val categoryName: String = TaskCategory.DAILY.name,
    val timeLabel: String = "۱۰:۳۰ صبح",
    val extraInfo: String = "",
    val totalSteps: Int = 1,
    val completedSteps: Int = 0,
    val isCompleted: Boolean = false,
    val isStarred: Boolean = false,
    val colorIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    val category: TaskCategory
        get() = TaskCategory.fromName(categoryName)

    val progressPercent: Int
        get() = when {
            isCompleted -> 100
            totalSteps <= 1 -> 0
            else -> ((completedSteps.toFloat() / totalSteps.toFloat()) * 100).toInt().coerceIn(0, 100)
        }
}

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String,
    val categoryName: String = TaskCategory.DAILY.name,
    val frequencyName: String = RoutineFrequency.DAILY.name,
    val timeOfDay: String = "۰۸:۰۰ صبح",
    // Comma-separated Persian weekday indices: 0=Shanbeh .. 6=Jomeh
    val daysOfWeekCsv: String = "0,1,2,3,4,5,6",
    // Comma-separated completed weekday indices for the current week
    val completedDaysCsv: String = "0,1,2",
    val streakCount: Int = 5,
    val isDoneToday: Boolean = false,
    val colorIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    val category: TaskCategory
        get() = TaskCategory.fromName(categoryName)

    val frequency: RoutineFrequency
        get() = RoutineFrequency.fromName(frequencyName)

    val scheduledDays: Set<Int>
        get() = daysOfWeekCsv.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()

    val completedDays: Set<Int>
        get() = completedDaysCsv.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()

    val weeklyProgressPercent: Int
        get() {
            val total = scheduledDays.size.coerceAtLeast(1)
            val done = completedDays.intersect(scheduledDays).size
            return ((done.toFloat() / total.toFloat()) * 100).toInt().coerceIn(0, 100)
        }
}

val pastelCardPalette = listOf(
    CardSkyBlue,       // 0 - Sky Blue
    CardPeriwinkle,    // 1 - Periwinkle Purple
    CardTurquoise,     // 2 - Mint Turquoise
    CardWarmAmber,     // 3 - Warm Amber
    CardCoralPink,     // 4 - Coral Pink
    CardMintGreen      // 5 - Mint Green
)

fun getCardColorByIndex(index: Int): Color =
    pastelCardPalette[index.mod(pastelCardPalette.size)]

package com.example.data

import kotlinx.coroutines.flow.Flow

class KaramRepository(private val dao: KaramDao) {
    val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()
    val allRoutines: Flow<List<RoutineEntity>> = dao.getAllRoutines()

    suspend fun ensureSeedDataIfNeeded(alreadyInitialized: Boolean, onInitialized: () -> Unit) {
        if (!alreadyInitialized && dao.getTaskCount() == 0 && dao.getRoutineCount() == 0) {
            dao.insertAllTasks(defaultStarterTasks())
            dao.insertAllRoutines(defaultStarterRoutines())
            onInitialized()
        }
    }

    suspend fun resetToSampleData() {
        dao.insertAllTasks(defaultStarterTasks())
        dao.insertAllRoutines(defaultStarterRoutines())
    }

    suspend fun insertTask(task: TaskEntity) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        dao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        dao.deleteTask(task)
    }

    suspend fun toggleTaskCompletion(task: TaskEntity) {
        val nowCompleted = !task.isCompleted
        val updatedSteps = if (nowCompleted) task.totalSteps else 0
        dao.updateTask(
            task.copy(
                isCompleted = nowCompleted,
                completedSteps = updatedSteps
            )
        )
    }

    suspend fun incrementTaskStep(task: TaskEntity) {
        if (task.isCompleted) {
            dao.updateTask(task.copy(isCompleted = false, completedSteps = 0))
        } else {
            val nextStep = (task.completedSteps + 1).coerceAtMost(task.totalSteps)
            val done = nextStep >= task.totalSteps
            dao.updateTask(
                task.copy(
                    completedSteps = nextStep,
                    isCompleted = done
                )
            )
        }
    }

    suspend fun toggleTaskStar(task: TaskEntity) {
        dao.updateTask(task.copy(isStarred = !task.isStarred))
    }

    suspend fun insertRoutine(routine: RoutineEntity) {
        dao.insertRoutine(routine)
    }

    suspend fun updateRoutine(routine: RoutineEntity) {
        dao.updateRoutine(routine)
    }

    suspend fun deleteRoutine(routine: RoutineEntity) {
        dao.deleteRoutine(routine)
    }

    suspend fun toggleRoutineToday(routine: RoutineEntity, todayIndex: Int) {
        val nowDone = !routine.isDoneToday
        val currentCompleted = routine.completedDays.toMutableSet()
        if (nowDone) {
            currentCompleted.add(todayIndex)
        } else {
            currentCompleted.remove(todayIndex)
        }
        val newStreak = if (nowDone) routine.streakCount + 1 else (routine.streakCount - 1).coerceAtLeast(0)
        dao.updateRoutine(
            routine.copy(
                isDoneToday = nowDone,
                completedDaysCsv = currentCompleted.sorted().joinToString(","),
                streakCount = newStreak
            )
        )
    }

    suspend fun toggleRoutineWeekday(routine: RoutineEntity, dayIndex: Int) {
        val currentCompleted = routine.completedDays.toMutableSet()
        if (currentCompleted.contains(dayIndex)) {
            currentCompleted.remove(dayIndex)
        } else {
            currentCompleted.add(dayIndex)
        }
        dao.updateRoutine(
            routine.copy(
                completedDaysCsv = currentCompleted.sorted().joinToString(",")
            )
        )
    }

    private fun defaultStarterTasks(): List<TaskEntity> = listOf(
        TaskEntity(
            title = "قرص امگا ۳ و ویتامین دی",
            subtitle = "همراه با یک لیوان آب بعد از وعده ناهار",
            categoryName = TaskCategory.MEDICINE.name,
            timeLabel = "۱۴:۰۰ ظهر",
            extraInfo = "۲ عدد کپسول ژله‌ای",
            totalSteps = 2,
            completedSteps = 1,
            isCompleted = false,
            isStarred = true,
            colorIndex = 2
        ),
        TaskEntity(
            title = "پرداخت قبض اینترنت و موبایل",
            subtitle = "تمدید بسته ماهانه فیبر نوری و خط همراه",
            categoryName = TaskCategory.PAYMENT.name,
            timeLabel = "۱۸:۳۰ عصر",
            extraInfo = "۴۸۰٬۰۰۰ تومان",
            totalSteps = 2,
            completedSteps = 0,
            isCompleted = false,
            isStarred = true,
            colorIndex = 3
        ),
        TaskEntity(
            title = "نوبت چکاپ دندان‌پزشکی",
            subtitle = "بررسی دوره‌ای و جرم‌گیری دندان‌ها",
            categoryName = TaskCategory.APPOINTMENT.name,
            timeLabel = "۱۷:۱۵ عصر",
            extraInfo = "کلینیک لبخند، دکتر نادری",
            totalSteps = 1,
            completedSteps = 0,
            isCompleted = false,
            isStarred = true,
            colorIndex = 1
        ),
        TaskEntity(
            title = "تماس تصویری با مامان و بابا",
            subtitle = "احوال‌پرسی و هماهنگی مهمانی آخر هفته",
            categoryName = TaskCategory.CALL.name,
            timeLabel = "۲۰:۰۰ شب",
            extraInfo = "خانواده عزیز ❤️",
            totalSteps = 1,
            completedSteps = 0,
            isCompleted = false,
            isStarred = false,
            colorIndex = 5
        ),
        TaskEntity(
            title = "تنظیم زنگ بیدارباش فردا صبح",
            subtitle = "بیدار شدن با انرژی برای ورزش صبحگاهی",
            categoryName = TaskCategory.ALARM.name,
            timeLabel = "۰۶:۴۵ صبح",
            extraInfo = "زنگ ملایم پرندگان",
            totalSteps = 1,
            completedSteps = 1,
            isCompleted = true,
            isStarred = false,
            colorIndex = 4
        ),
        TaskEntity(
            title = "طراحی و مرور اهداف هفته جدید",
            subtitle = "یادداشت کارهای مهم در دفترچه برنامه‌ریزی",
            categoryName = TaskCategory.DAILY.name,
            timeLabel = "۱۱:۰۰ صبح",
            extraInfo = "۳ اولویت اصلی",
            totalSteps = 4,
            completedSteps = 3,
            isCompleted = false,
            isStarred = true,
            colorIndex = 0
        )
    )

    private fun defaultStarterRoutines(): List<RoutineEntity> = listOf(
        RoutineEntity(
            title = "نوشیدن ۸ لیوان آب در روز",
            subtitle = "حفظ شادابی پوست و انرژی روزانه بدن",
            categoryName = TaskCategory.MEDICINE.name,
            frequencyName = RoutineFrequency.DAILY.name,
            timeOfDay = "در طول روز",
            daysOfWeekCsv = "0,1,2,3,4,5,6",
            completedDaysCsv = "0,1,2,3,4",
            streakCount = 12,
            isDoneToday = false,
            colorIndex = 0
        ),
        RoutineEntity(
            title = "۲۰ دقیقه مطالعه کتاب قبل خواب",
            subtitle = "آرامش ذهن و دوری از صفحه‌نمایش موبایل",
            categoryName = TaskCategory.DAILY.name,
            frequencyName = RoutineFrequency.NIGHT.name,
            timeOfDay = "۲۲:۳۰ شب",
            daysOfWeekCsv = "0,1,2,3,4,5,6",
            completedDaysCsv = "0,1,2,4,5",
            streakCount = 9,
            isDoneToday = true,
            colorIndex = 1
        ),
        RoutineEntity(
            title = "پیاده‌روی و نرمش صبحگاهی",
            subtitle = "شروع روز با هوای تازه و حرکات کششی",
            categoryName = TaskCategory.MEDICINE.name,
            frequencyName = RoutineFrequency.MORNING.name,
            timeOfDay = "۰۷:۱۵ صبح",
            daysOfWeekCsv = "0,1,2,3,4",
            completedDaysCsv = "0,1,2,3",
            streakCount = 7,
            isDoneToday = false,
            colorIndex = 2
        ),
        RoutineEntity(
            title = "بررسی خرج‌کرد و بودجه هفتگی",
            subtitle = "ثبت پرداختی‌ها، پس‌انداز و قبوض خانه",
            categoryName = TaskCategory.PAYMENT.name,
            frequencyName = RoutineFrequency.WEEKLY.name,
            timeOfDay = "پنج‌شنبه‌ها ۱۹:۰۰",
            daysOfWeekCsv = "5",
            completedDaysCsv = "5",
            streakCount = 4,
            isDoneToday = false,
            colorIndex = 3
        )
    )
}

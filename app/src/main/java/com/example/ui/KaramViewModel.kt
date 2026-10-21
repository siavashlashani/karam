package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.KaramDatabase
import com.example.data.KaramRepository
import com.example.data.RoutineEntity
import com.example.data.RoutineFrequency
import com.example.data.TaskCategory
import com.example.data.TaskEntity
import com.example.data.TaskStatusFilter
import com.example.ui.components.KaramTab
import com.example.util.getCurrentPersianWeekdayIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class KaramUiState(
    val allTasks: List<TaskEntity> = emptyList(),
    val filteredTasks: List<TaskEntity> = emptyList(),
    val allRoutines: List<RoutineEntity> = emptyList(),
    val filteredRoutines: List<RoutineEntity> = emptyList(),
    val currentTab: KaramTab = KaramTab.HOME,
    val searchQuery: String = "",
    val selectedCategory: TaskCategory? = null,
    val statusFilter: TaskStatusFilter = TaskStatusFilter.ALL,
    val selectedRoutineFrequency: RoutineFrequency? = null,
    val userName: String = "نگار عزیز",
    val avatarIndex: Int = 0
) {
    val completedTasksCount: Int
        get() = allTasks.count { it.isCompleted }

    val ongoingTasksCount: Int
        get() = allTasks.count { !it.isCompleted }

    val dailyProgressPercent: Int
        get() {
            val total = allTasks.size + allRoutines.size
            if (total == 0) return 100
            val done = completedTasksCount + allRoutines.count { it.isDoneToday }
            return ((done * 100) / total).coerceIn(0, 100)
        }
}

class KaramViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("karam_prefs", Context.MODE_PRIVATE)
    private val repository: KaramRepository

    private val _currentTab = MutableStateFlow(KaramTab.HOME)
    val currentTab: StateFlow<KaramTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)
    private val _statusFilter = MutableStateFlow(TaskStatusFilter.ALL)
    private val _selectedRoutineFrequency = MutableStateFlow<RoutineFrequency?>(null)
    private val _userName = MutableStateFlow(prefs.getString("user_name", "نگار عزیز") ?: "نگار عزیز")
    private val _avatarIndex = MutableStateFlow(prefs.getInt("avatar_index", 0))

    val uiState: StateFlow<KaramUiState>

    init {
        val dao = KaramDatabase.getDatabase(application).karamDao()
        repository = KaramRepository(dao)

        viewModelScope.launch {
            val seeded = prefs.getBoolean("initial_seeded", false)
            repository.ensureSeedDataIfNeeded(alreadyInitialized = seeded) {
                prefs.edit().putBoolean("initial_seeded", true).apply()
            }
        }

        val filterFlow = combine(
            _currentTab,
            _searchQuery,
            _selectedCategory,
            _statusFilter,
            _selectedRoutineFrequency
        ) { tab, query, category, status, routineFreq ->
            FilterParams(tab, query, category, status, routineFreq)
        }

        val profileFlow = combine(_userName, _avatarIndex) { name, avatar ->
            name to avatar
        }

        uiState = combine(
            repository.allTasks,
            repository.allRoutines,
            filterFlow,
            profileFlow
        ) { tasks, routines, filters, profile ->
            val q = filters.query.trim()
            val filteredTasks = tasks.filter { task ->
                val matchesCategory = filters.category == null || task.category == filters.category
                val matchesStatus = when (filters.status) {
                    TaskStatusFilter.ALL -> true
                    TaskStatusFilter.ONGOING -> !task.isCompleted
                    TaskStatusFilter.COMPLETED -> task.isCompleted
                }
                val matchesQuery = q.isEmpty() ||
                    task.title.contains(q, ignoreCase = true) ||
                    task.subtitle.contains(q, ignoreCase = true) ||
                    task.extraInfo.contains(q, ignoreCase = true) ||
                    task.category.persianTitle.contains(q, ignoreCase = true)
                matchesCategory && matchesStatus && matchesQuery
            }

            val filteredRoutines = routines.filter { routine ->
                val matchesFreq = filters.routineFreq == null || routine.frequency == filters.routineFreq
                val matchesQuery = q.isEmpty() ||
                    routine.title.contains(q, ignoreCase = true) ||
                    routine.subtitle.contains(q, ignoreCase = true)
                matchesFreq && matchesQuery
            }

            KaramUiState(
                allTasks = tasks,
                filteredTasks = filteredTasks,
                allRoutines = routines,
                filteredRoutines = filteredRoutines,
                currentTab = filters.tab,
                searchQuery = filters.query,
                selectedCategory = filters.category,
                statusFilter = filters.status,
                selectedRoutineFrequency = filters.routineFreq,
                userName = profile.first,
                avatarIndex = profile.second
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = KaramUiState()
        )
    }

    fun selectTab(tab: KaramTab) {
        _currentTab.value = tab
    }

    fun openCategoryTasks(category: TaskCategory?) {
        _selectedCategory.value = category
        _currentTab.value = KaramTab.TASKS
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: TaskCategory?) {
        _selectedCategory.value = category
    }

    fun selectStatusFilter(filter: TaskStatusFilter) {
        _statusFilter.value = filter
    }

    fun selectRoutineFrequency(frequency: RoutineFrequency?) {
        _selectedRoutineFrequency.value = frequency
    }

    fun saveTask(task: TaskEntity) {
        viewModelScope.launch {
            if (task.id == 0) {
                repository.insertTask(task)
            } else {
                repository.updateTask(task)
            }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task)
        }
    }

    fun incrementTaskStep(task: TaskEntity) {
        viewModelScope.launch {
            repository.incrementTaskStep(task)
        }
    }

    fun toggleTaskStar(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskStar(task)
        }
    }

    fun saveRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            if (routine.id == 0) {
                repository.insertRoutine(routine)
            } else {
                repository.updateRoutine(routine)
            }
        }
    }

    fun deleteRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun toggleRoutineToday(routine: RoutineEntity) {
        viewModelScope.launch {
            val todayIdx = getCurrentPersianWeekdayIndex()
            repository.toggleRoutineToday(routine, todayIdx)
        }
    }

    fun toggleRoutineWeekday(routine: RoutineEntity, dayIndex: Int) {
        viewModelScope.launch {
            repository.toggleRoutineWeekday(routine, dayIndex)
        }
    }

    fun updateProfile(name: String, avatarIndex: Int) {
        _userName.value = name
        _avatarIndex.value = avatarIndex
        prefs.edit()
            .putString("user_name", name)
            .putInt("avatar_index", avatarIndex)
            .apply()
    }

    fun resetSampleData() {
        viewModelScope.launch {
            repository.resetToSampleData()
        }
    }

    private data class FilterParams(
        val tab: KaramTab,
        val query: String,
        val category: TaskCategory?,
        val status: TaskStatusFilter,
        val routineFreq: RoutineFrequency?
    )
}

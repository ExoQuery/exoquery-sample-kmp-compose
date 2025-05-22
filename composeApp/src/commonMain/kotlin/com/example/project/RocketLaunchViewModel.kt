package com.example.project

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.project.RocketLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

data class RocketLaunchScreenState(
    val isLoading: Boolean = false,
    val launches: List<RocketLaunch> = emptyList()
)

class CommonRocketLaunchViewModel(private val sdk: SpaceXSDK) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _state = mutableStateOf(RocketLaunchScreenState())
    val state: State<RocketLaunchScreenState> = _state

    init {
        loadLaunches()
    }

    fun loadLaunches(forceReload: Boolean = false) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, launches = emptyList())
            try {
                val launches = sdk.getLaunches(forceReload = forceReload)
                _state.value = _state.value.copy(isLoading = false, launches = launches)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, launches = emptyList())
            }
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}

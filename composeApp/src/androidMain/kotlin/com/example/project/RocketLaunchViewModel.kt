package com.example.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RocketLaunchViewModel(sdk: SpaceXSDK) : ViewModel() {
  val commonViewModel = CommonRocketLaunchViewModel(sdk)
  val state = commonViewModel.state

  override fun onCleared() {
    commonViewModel.onCleared()
    super.onCleared()
  }

  fun loadLaunches() {
    commonViewModel.loadLaunches(false)
  }
}

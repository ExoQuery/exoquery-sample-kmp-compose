package com.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.compose.koinInject

@Composable
fun App() {
  val sdk = koinInject<SpaceXSDK>()
  val viewModel = remember { CommonRocketLaunchViewModel(sdk) }
  CommonApp(viewModel = viewModel)
}

package com.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.compose.koinInject
import org.koin.androidx.compose.koinViewModel
import com.example.project.SpaceXSDK
import com.example.project.CommonRocketLaunchViewModel
import com.example.project.CommonApp
import com.example.project.RocketLaunchViewModel

@Composable
fun App() {
  val viewModel = koinViewModel<RocketLaunchViewModel>()
  CommonApp(viewModel = viewModel.commonViewModel)
}

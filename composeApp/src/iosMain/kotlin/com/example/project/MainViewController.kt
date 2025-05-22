package com.example.project

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.compose.koinInject

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin in the composeApp module before using it
    initComposeKoin()

    val sdk = koinInject<SpaceXSDK>()
    val viewModel = CommonRocketLaunchViewModel(sdk)
    CommonApp(viewModel = viewModel)
}

package com.example.project

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.project.theme.AppTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(
  ExperimentalMaterial3Api::class
)
@Composable
fun CommonApp(viewModel: CommonRocketLaunchViewModel) {
  val state by remember { viewModel.state }
  val pullToRefreshState = rememberPullToRefreshState()
  if (pullToRefreshState.isRefreshing) {
    viewModel.loadLaunches()
    pullToRefreshState.endRefresh()
  }

  AppTheme {
    Scaffold(
      topBar = {
        TopAppBar(title = {
          Text(
            "SpaceX Launches",
            style = MaterialTheme.typography.headlineLarge
          )
        })
      }
    ) { padding ->
      Box(
        modifier = Modifier
          .nestedScroll(pullToRefreshState.nestedScrollConnection)
          .fillMaxSize()
          .padding(padding)
      ) {
        if (state.isLoading) {
          Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
          ) {
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
          }
        } else {
          LazyColumn {
            items(state.launches) { launch: RocketLaunch ->
              Column(modifier = Modifier.padding(all = 16.dp)) {
                Text(
                  text = "${launch.missionName} - ${launch.launchYear}",
                  style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(8.dp))
                Text(
                  text = if (launch.launchSuccess == true) "Successful" else "Unsuccessful",
                  color = if (launch.launchSuccess == true) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                Spacer(Modifier.height(8.dp))
                val details = launch.details
                if (details?.isNotBlank() == true) {
                  Text(
                    text = details
                  )
                }
              }
              HorizontalDivider()
            }
          }
        }

        PullToRefreshContainer(
          state = pullToRefreshState,
          modifier = Modifier.align(Alignment.TopCenter)
        )
      }
    }
  }
}

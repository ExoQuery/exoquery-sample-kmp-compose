package com.example.project

import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

/**
 * Initializes Koin for the Compose UI in iOS.
 * This ensures that Koin is properly initialized in the composeApp module
 * before it's used by the Compose UI.
 * 
 * If Koin is already started by the shared module, this will load additional modules
 * needed by the composeApp module.
 */
fun initComposeKoin() {
    // Define the modules needed by the composeApp module
    val composeModules = module {
        // We don't need to redefine SpaceXSDK if it's already defined in the shared module
        // This is just a fallback in case it's not
        single<SpaceXSDK> { 
            SpaceXSDK(
                databaseDriverFactory = com.example.project.cache.IOSDatabaseDriverFactory(),
                api = com.example.project.network.SpaceXApi()
            )
        }
    }

    try {
        // Try to load modules into existing Koin context
        loadKoinModules(composeModules)
        println("Successfully loaded composeApp modules into existing Koin context")
    } catch (e: Exception) {
        // If loading modules fails, Koin might not be started yet
        println("Could not load modules, trying to start Koin: ${e.message}")
        try {
            startKoin {
                modules(composeModules)
            }
            println("Successfully started Koin with composeApp modules")
        } catch (e2: Exception) {
            // Check if the error message indicates Koin is already started
            if (e2.message?.contains("already started") == true) {
                // Koin is already started, which is fine
                println("Koin already started in another module, reusing existing context")
            } else {
                // Some other error occurred, try to stop and restart Koin
                println("Error initializing Koin in ComposeKoinHelper: ${e2.message}")
                try {
                    stopKoin()
                    startKoin {
                        modules(composeModules)
                    }
                    println("Successfully restarted Koin with composeApp modules")
                } catch (e3: Exception) {
                    println("Failed to restart Koin: ${e3.message}")
                }
            }
        }
    }
}

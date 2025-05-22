import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.exoquery)
}

// Workaround for https://youtrack.jetbrains.com/issue/KT-51970
//afterEvaluate {
//    afterEvaluate {
//        tasks.configureEach {
//            if (
//                name.startsWith("compile")
//                && name.endsWith("KotlinMetadata")
//            ) {
//                println("disabling ${this}:$name")
//                enabled = false
//            }
//        }
//    }
//}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        //iosX64(),
        iosArm64(),
        //iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        iosTarget.binaries.configureEach {
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.android.driver)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.koin.androidx.compose)
            implementation(libs.exoquery.runner.android)
            // Android-specific dependencies
            implementation(libs.androidx.lifecycle.viewmodel.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)
            implementation(libs.koin.compose)
            // Use Jetbrains Compose Material3 for multiplatform
            implementation(compose.material3)
        }
        iosMain.dependencies {
            implementation(libs.exoquery.runner.native)
            implementation(libs.jetbrains.annotations.kmp)
        }
    }
}

configurations.all {
  exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
}

fun MinimalExternalModuleDependency.simpleString() =
    this.let { "${it.module}:${it.versionConstraint.requiredVersion}" }

android {
    compileSdk = 34 // for example

    namespace = "com.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 30
        applicationId = "com.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
  // kotlinx-coroutines-debug and bytebuddy both include win32-x86-64/attach_hotspot_windows.dll, tell package to ignore
  packaging {
    resources {
      excludes += "**/attach_hotspot_windows.dll"
    }
  }
}
dependencies {
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.sqlite.framework)
}

compose.desktop {
    application {
        mainClass = "com.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.project"
            packageVersion = "1.0.0"
        }
    }
}


repositories {
    google {
        mavenContent {
            includeGroupAndSubgroups("androidx")
            includeGroupAndSubgroups("com.android")
            includeGroupAndSubgroups("com.google")
        }
    }
    mavenCentral()
    maven("https://s01.oss.sonatype.org/service/local/repositories/releases/content/")
    mavenLocal()
}

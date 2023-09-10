plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.kalex.bookyouu_notesapp.permission"
    compileSdk = 34

    defaultConfig {

        minSdkVersion((rootProject.extra["generalProjectVersions"] as Map<*, *>)["minSdk"] as Int)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ((rootProject.extra["generalProjectVersions"] as Map<*, *>)["kotlinCompilerExtensionVersion"] as String)
    }
}

dependencies {

    implementation(libs.androidx.ktx)

    // COMPOSE SECTION
    implementation(libs.androidx.navigation.compose)
    implementation(platform("androidx.compose:compose-bom:2023.06.00"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha01")
    implementation(libs.androidx.material)

    // TO REQUEST PERMISSIONS
    implementation(libs.accompanist.permissions)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kalex.bookyouu_notesapp.widget"
    compileSdk = com.android.ide.common.symbols.valueStringToInt(libs.versions.compileSdk.get())

    defaultConfig {
        minSdk = com.android.ide.common.symbols.valueStringToInt(libs.versions.minSdk.get())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    // GLANCE SECTION
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.datastore.preferences)

    // COMPOSE SECTION (Glance depends on some Compose runtime)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    // KOIN
    implementation(libs.koin.android)

    implementation(project(":payments"))
    implementation(project(":db"))
    implementation(project(":core"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

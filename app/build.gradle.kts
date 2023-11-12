import com.android.ide.common.symbols.valueStringToInt
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.kalex.bookyouu_notesapp"
    compileSdk = valueStringToInt(libs.versions.compileSdk.get())

    defaultConfig {
        applicationId = "com.kalex.bookyouu_notesapp"

        minSdk = valueStringToInt(libs.versions.minSdk.get())
        targetSdk = valueStringToInt(libs.versions.targetSdk.get())
        versionCode = valueStringToInt(libs.versions.versionCode.get())
        versionName = libs.versions.versionName.get()

        setProperty("archivesBaseName", applicationId + "-v" + versionCode + "(" + versionName + ")")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        register("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
            isDebuggable = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            proguardFiles("baseline-profile-rules.pro")
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
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    //Paging
    //implementation "androidx.paging:paging-compose:3.2.0-rc01"

    //COMPOSE SECTION
    implementation(libs.androidx.navigation.compose)
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha01")
    implementation(libs.androidx.material)

    //coil
    implementation(libs.coil.compose)

    //navigation accompanist
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.androidx.animation)

    //TO REQUEST PERMISSIONS
    implementation(libs.accompanist.permissions)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //BIOMETRICS
    implementation (libs.androidx.biometric)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //CAMERA SECTION
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.androidx.camera.extensions)

    //ADBMOD
    //implementation (libs.play.services.ads)

    //Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    //ROOM SECTION
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // To use Kotlin annotation processing tool (kapt)
    ksp(libs.androidx.room.compiler)
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    //MODULES SECTIONmapOf("path" to
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":permission")))
    implementation(project(mapOf("path" to ":db")))
    implementation(project(mapOf("path" to ":notification")))
    implementation(project(mapOf("path" to ":moreMenu")))

    //TESTING SECTION
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // base line profile

}
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.odensala.asr"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.odensala.asr"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val secretsPropertiesFile = rootProject.file("secrets.properties")
        val secretsProperties = Properties()
        if (secretsPropertiesFile.exists()) {
            secretsProperties.load(secretsPropertiesFile.inputStream())
        }

        buildConfigField(
            "String",
            "MIMI_CLIENT_ID",
            "\"${secretsProperties.getProperty("MIMI_APP_ID", "")}:${secretsProperties.getProperty("MIMI_APP_CLIENT_ID", "")}\""
        )

        buildConfigField(
            "String",
            "MIMI_CLIENT_SECRET",
            "\"${secretsProperties.getProperty("MIMI_CLIENT_SECRET", "")}\""
        )

        buildConfigField(
            "String",
            "MIMI_SCOPE",
            "\"${secretsProperties.getProperty("MIMI_SCOPE", "https://apis.mimi.fd.ai/auth/asr/websocket-api-service")}\""
        )

        buildConfigField(
            "String",
            "MIMI_AUTH_BASE_URL",
            "\"https://auth.mimi.fd.ai/\""
        )

        buildConfigField(
            "String",
            "MIMI_ASR_ENDPOINT",
            "\"wss://service.mimi.fd.ai\""
        )

        buildConfigField(
            "String",
            "MIMI_APP_NAME",
            "\"${secretsProperties.getProperty("MIMI_APP_NAME", "")}\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.moshi)
    implementation (libs.moshi.kotlin)
    ksp (libs.moshi.kotlin.codegen)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
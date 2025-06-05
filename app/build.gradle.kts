plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.frontcapstone2025"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.frontcapstone2025"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"https://capstone2025backend.onrender.com\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        compose = true
        buildConfig = true
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
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //viewModel : "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2"
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //navigate : androidx.navigation:navigation-compose:2.8.3
    implementation(libs.androidx.navigation.compose)

    //google icon "androidx.compose.material:material-icons-extended:1.6.6"
    implementation("androidx.compose.material:material-icons-extended:1.6.6")

    implementation("androidx.compose.foundation:foundation:1.6.6")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") //"com.squareup.retrofit2:retrofit:2.9.0"
// Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0") //"com.squareup.retrofit2:converter-scalars:2.9.0"
    implementation("com.google.code.gson:gson:2.11.0") // "com.google.code.gson:gson:2.11.0"
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") //"com.squareup.retrofit2:converter-gson:2.11.0"
}
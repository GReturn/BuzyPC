plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "io.buzypc.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.buzypc.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging {
        resources {
            // Exclude all META-INF/INDEX.LIST files
            excludes.add("META-INF/INDEX.LIST")
        }
    }
    packagingOptions {
        resources {
            exclude("META-INF/**")
        }
    }
}

dependencies {
    // for environment variables
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")

    // for BuzyAI
    implementation("com.azure:azure-ai-inference:1.0.0-beta.4") {
        exclude(group = "io.netty")
    }

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Fragment
    implementation(libs.androidx.fragment.ktx)

    // Card View
    implementation(libs.androidx.cardview)

    // Radar Chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // JSON Serialization
    implementation("com.google.code.gson:gson:2.13.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.animation.core.android)
    testImplementation(libs.junit) {
        exclude("io.netty:netty-all:4.1.67.Final")
    }
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
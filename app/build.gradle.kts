plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)



    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.gson)

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    implementation("com.google.android.gms:play-services-location:21.1.0")

    implementation ("org.osmdroid:osmdroid-android:6.1.11")

    implementation ("com.google.android.material:material:1.9.0")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")



    // Dependencies for local unit tests
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.hamcrest:hamcrest-all:1.3")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.robolectric:robolectric:4.5.1")

    // AndroidX Test - JVM testing
    testImplementation ("androidx.test:core-ktx:1.4.0")

    //Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    // hamcrest
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")

    // AndroidX and Robolectric
    testImplementation( "androidx.test.ext:junit-ktx:1.1.3")
    testImplementation ("androidx.test:core-ktx:1.4.0")
    testImplementation ("org.robolectric:robolectric:4.8")

    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")

    //kotlinx-coroutines
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Same version as coroutines-core


    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Latest stable version as of 2024


    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")

    testImplementation ("io.mockk:mockk:1.13.5")

    testImplementation ("org.mockito:mockito-core:5.0.0")

    testImplementation ("app.cash.turbine:turbine:1.0.0")

        //lottie
    implementation ("com.airbnb.android:lottie:6.0.1")




}
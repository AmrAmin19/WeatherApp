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


//    // JUnit testing framework
//    testImplementation("junit:junit:4.13.2")
//
//    // Coroutines test for handling suspend functions
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
//
//    // Hilt for dependency injection in testing
//    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
//    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44")
//
//    // Mockito for mocking objects
//    testImplementation("org.mockito:mockito-core:3.12.4")
//
//    // MockK for mocking Kotlin classes
//    testImplementation("io.mockk:mockk:1.12.0")
//
//    // Truth for better assertions
//    testImplementation("com.google.truth:truth:1.1.3")
//
//    // Robolectric for testing
//    testImplementation ("org.robolectric:robolectric:4.9")
//    implementation ("com.jakewharton.timber:timber:5.0.1")
//    testImplementation ("org.mockito:mockito-inline:4.6.1")








}
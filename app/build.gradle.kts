plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.playlist_maker2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.playlist_maker2"
        minSdk = 24
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //RxJava
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")

    //Jetpack Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    implementation ("androidx.fragment:fragment-ktx:<latest_version>")
    implementation ("androidx.fragment:fragment:<latest_version>")

    implementation ("androidx.activity:activity:<latest_version>")
    implementation ("androidx.activity:activity-ktx:<latest_version>")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    //Gson
    implementation ("com.google.code.gson:gson:2.10")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Koin
    implementation ("io.insert-koin:koin-android:3.3.0")

    //viewpager2
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //TabLayout
    implementation ("com.google.android.material:material:1.8.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    //ViewModel
    implementation ("androidx.core:core-ktx:1.9.0")

    //Room"2.5.1-error: Not sure how to convert a Cursor to this method's return type (java.lang.Object)"
    //текущая стабильная версия 2.7.0
    implementation("androidx.room:room-runtime:2.7.0") // библиотека Room
    kapt("androidx.room:room-compiler:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0") // поддержка корутин

    //coroutine permissions
    implementation ("com.markodevcic:peko:3.0.5")
}
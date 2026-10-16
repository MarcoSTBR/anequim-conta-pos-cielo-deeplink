plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.jetbrains.kotlin)
}

apply(from = "../positivo/positivo-signing-config.gradle")

android {
    namespace = "br.com.st.anequimconta_stand_dl1"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.st.anequimconta_stand_dl1"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "CREDENTIALS_CLIENT_ID", "\"b5e6445bced0432b964220bac7c51da7\"")
        buildConfigField("String", "CREDENTIALS_ACCESS_TOKEN", "\"C55Ca088E1324f84bD515db8Bbb33551\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("positivo")
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

    buildFeatures {
        viewBinding = true
        aidl = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.gson)
    implementation(libs.jakewharton)
    implementation(libs.legacy)
    implementation(libs.zxing)
    implementation(libs.journeyapps)
    implementation(libs.okhttp3)
    implementation(libs.jwtdecode)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(fileTree(mapOf(
        "dir" to "libs",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(kotlin("script-runtime"))
}
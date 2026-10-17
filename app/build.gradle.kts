plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

fun getSigningProperty(name: String, androidPrefixName: String): String? {
    return project.findProperty(name)?.toString()
        ?: System.getenv(name)
        ?: project.findProperty(androidPrefixName)?.toString()
        ?: System.getenv(androidPrefixName)
}

android {
    namespace = "com.ankh.sutrasaga"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ankh.sutrasaga"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystorePath = getSigningProperty("KEYSTORE_PATH", "ANDROID_KEYSTORE_PATH")
            val keystorePassword = getSigningProperty("KEYSTORE_PASSWORD", "ANDROID_KEYSTORE_PASSWORD")
            val keyAliasVal = getSigningProperty("KEY_ALIAS", "ANDROID_KEY_ALIAS")
            val keyPasswordVal = getSigningProperty("KEY_PASSWORD", "ANDROID_KEY_PASSWORD")

            if (!keystorePath.isNullOrBlank()) {
                val keystoreFile = file(keystorePath)
                if (keystoreFile.exists() && keystoreFile.isFile && keystoreFile.canRead()) {
                    storeFile = keystoreFile
                    storePassword = keystorePassword ?: ""
                    keyAlias = keyAliasVal ?: ""
                    keyPassword = keyPasswordVal ?: ""
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Authoritative: release builds ALWAYS require and use release signing.
            // Under no circumstances may a release artifact fall back to debug signing.
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = true
        warningsAsErrors = false
    }
}

gradle.taskGraph.whenReady {
    val releaseArtifactTaskNames = setOf(
        "bundleRelease",
        "assembleRelease",
        "packageReleaseBundle",
        "signReleaseBundle",
        "validateSigningRelease"
    )
    val hasReleaseArtifactTask = allTasks.any { task ->
        releaseArtifactTaskNames.contains(task.name)
    }
    if (hasReleaseArtifactTask) {
        val keystorePath = getSigningProperty("KEYSTORE_PATH", "ANDROID_KEYSTORE_PATH")
        val keystorePassword = getSigningProperty("KEYSTORE_PASSWORD", "ANDROID_KEYSTORE_PASSWORD")
        val keyAliasVal = getSigningProperty("KEY_ALIAS", "ANDROID_KEY_ALIAS")
        val keyPasswordVal = getSigningProperty("KEY_PASSWORD", "ANDROID_KEY_PASSWORD")

        val missing = mutableListOf<String>()
        if (keystorePath.isNullOrBlank()) missing.add("KEYSTORE_PATH / ANDROID_KEYSTORE_PATH")
        if (keystorePassword.isNullOrBlank()) missing.add("KEYSTORE_PASSWORD / ANDROID_KEYSTORE_PASSWORD")
        if (keyAliasVal.isNullOrBlank()) missing.add("KEY_ALIAS / ANDROID_KEY_ALIAS")
        if (keyPasswordVal.isNullOrBlank()) missing.add("KEY_PASSWORD / ANDROID_KEY_PASSWORD")

        if (missing.isNotEmpty()) {
            val missingList = missing.joinToString("\n") { "|  - $it" }
            throw GradleException(
                """
                |================================================================================
                |RELEASE SIGNING ERROR:
                |Release signing configuration is required to build a release artifact.
                |Silent fallback to debug signing is strictly disallowed for production safety.
                |
                |Missing signing parameter(s):
                $missingList
                |
                |Please configure the required environment variables or Gradle properties:
                |  - KEYSTORE_PATH (or ANDROID_KEYSTORE_PATH): Path to upload/release keystore (.jks/.keystore)
                |  - KEYSTORE_PASSWORD (or ANDROID_KEYSTORE_PASSWORD): Keystore password
                |  - KEY_ALIAS (or ANDROID_KEY_ALIAS): Key alias
                |  - KEY_PASSWORD (or ANDROID_KEY_PASSWORD): Key password
                |================================================================================
                """.trimMargin()
            )
        }

        val keystoreFile = file(keystorePath!!)
        if (!keystoreFile.exists()) {
            throw GradleException("Release signing keystore file does not exist: ${keystoreFile.absolutePath}")
        }
        if (!keystoreFile.isFile || !keystoreFile.canRead()) {
            throw GradleException("Release signing keystore file is not readable: ${keystoreFile.absolutePath}")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose BOM & UI
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Animation Engine
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Unit & Local Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("androidx.room:room-testing:2.6.1")
    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    testImplementation("androidx.compose.ui:ui-test-junit4")

    // Android Instrumentation & UI Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.room:room-testing:2.6.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

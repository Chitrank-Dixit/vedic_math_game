# ==============================================================================
# Ankh: The Sutra Saga — Production ProGuard & R8 Configuration
# ==============================================================================

# 1. Jetpack Compose Keep Rules
-keepattributes *Annotation*,InnerClasses,EnclosingMethod
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# 2. Room Database Keep Rules
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# 3. Domain Models & Enums Keep Rules
-keep class com.ankh.sutrasaga.domain.models.** { *; }
-keep enum com.ankh.sutrasaga.domain.models.** { *; }
-keep class com.ankh.sutrasaga.data.db.** { *; }

# 4. Lottie Animation Engine Keep Rules
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# 5. Kotlin Coroutines & Flow
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# 6. Transitive Annotations & Utilities
-dontwarn javax.annotation.**
-dontwarn okio.**

# 7. Optimization & Stripping Options
-repackageclasses ''
-allowaccessmodification
-dontusemixedcaseclassnames

buildscript {
    extra.apply {
        set("room_version", "2.6.0")
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

//    alias(libs.plugins.google.devtools.ksp) apply false
//    id("com.android.application") version "8.6.0" apply false
//    id("com.android.library") version "8.6.0" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
//    id("com.google.gms.google-services") version "4.4.2" apply false

}
//
//tasks.register("clean", Delete:: class){
//    delete(rootProject.buildDir)
//}
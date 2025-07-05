# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Keep SQLCipher database classes and native methods
# Keep SQLCipher classes and native fields/methods
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.SQLiteDatabase {
    long mNativeHandle;
    native <methods>;
}
-dontwarn net.sqlcipher.**

# If using Room with SQLCipher
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.** <methods>;
}

# SQLite compatibility
-keep class androidx.sqlite.** { *; }

# AndroidX lifecycle & core (safe side)
-keep class androidx.lifecycle.** { *; }
-keep class androidx.core.** { *; }

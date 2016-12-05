# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in d:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#forece reload of config files
-forceprocessing

-optimizations !class/merging/*,!field/removal/writeonly
#anything more than 3 is waste of time...each pass takes about 1 minute
-optimizationpasses 3

-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**

-dontwarn okhttp3.**
-dontwarn okio.**

-keep class com.bluelinelabs.logansquare.** { *; }
-keep @com.bluelinelabs.logansquare.annotation.JsonObject class *
-keep class **$$JsonObjectMapper { *; }

-assumenosideeffects class android.util.Log {
   public static boolean isLoggable(java.lang.String, int);
   public static int v(...);
   public static int i(...);
   public static int w(...);
   public static int d(...);
   public static int e(...);
   public static java.lang.String getStackTraceString(java.lang.Throwable);
}

-assumenosideeffects class java.lang.Exception {
    public void printStackTrace();
}

-printseeds release-seeds.txt
-printusage release-unused.txt
-printmapping release-mapping.txt
-printconfiguration release-out_dexconfig.txt
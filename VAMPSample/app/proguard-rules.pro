# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/XXXXXXXX/Library/Android/sdk/tools/proguard/proguard-android.txt
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
#AdGeneration(VAMP)
-keepclasseswithmembers class jp.supership.vamp.** { *; }
#ADGPlayer(RTB)
-keep class jp.supership.adgplayer.** { *; }
#AppLovin
-keep class com.applovin.** { *; }
#AppVador
-keep class com.appvador.** { *; }
#maio
-keep class jp.maio.sdk.android.** { *; }
#UnityAds
-keep class com.unity3d.ads.android.** { *; }

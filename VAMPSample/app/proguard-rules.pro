# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
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
#AdMob
-keep public class com.google.android.gms.ads.** { public *; }
-keep public class com.google.ads.** { public *; }
#AppLovin
-keep class com.applovin.** { *; }
#maio
-keep class jp.maio.sdk.android.** { *; }
#UnityAds
-keep class com.unity3d.ads.android.** { *; }
#FAN
-keep class com.facebook.ads.** { *; }
#Vungle
-keep class jp.supership.vamp.extra.** { *; }
-dontwarn com.vungle.**
-dontnote com.vungle.**
-keep class com.vungle.** { *; }
-keep class javax.inject.*
# GreenRobot(Vungle)
-dontwarn de.greenrobot.event.util.**
# RxJava(Vungle)
-dontwarn rx.internal.util.unsafe.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep class rx.schedulers.Schedulers { public static *; }
-keep class rx.schedulers.ImmediateScheduler { public *; }
-keep class rx.schedulers.TestScheduler { public *; }
-keep class rx.schedulers.Schedulers { public static ** test(); }
# MOAT(Vungle、Tapjoy)
-dontwarn com.moat.**
-keep class com.moat.** {
   public protected private *;
}
# Retrofit(Vungle)
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
#Tapjoy
-keep class com.tapjoy.** { *; }
#-keep class com.moat.** { *; }     Vungleでも設定してるのでこっちはコメントアウト
-keepattributes JavascriptInterface
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.tapjoy.**


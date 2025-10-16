# Keep Hilt generated components
-keep class dagger.hilt.internal.GeneratedComponent { *; }
-keep class dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class dagger.hilt.internal.UnsafeCasts { *; }

# Keep Room models
-keep class com.comunidadedevspace.imc.core.database.entity.** { *; }

# Keep AppAuth classes used via reflection
-keep class net.openid.appauth.** { *; }

# Keep Retrofit/Kotlin Serialization adapters
-keep class kotlinx.serialization.** { *; }

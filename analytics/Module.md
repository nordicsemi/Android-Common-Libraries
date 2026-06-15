# Module analytics

Set of classes related to Firebase Analytics.

## Configuration

Use of this module requires the following plugins to be applied in the app:
```kotlin
if (gradle.startParameter.taskRequests.toString().contains("Release")) {
    apply("com.google.gms.google-services")
    apply("com.google.firebase.crashlytics")
}
```
and the _google-services.json_ file to be present in the app module.

Read [Firebase Setup](https://firebase.google.com/docs/android/setup) for more.

> **Note:**
> 
> This package requires Hilt, as it's using Dependency Injection to provide the 
> `NordicAnalytics` class.

# Package no.nordicsemi.android.common.analytics

Main API for Nordic analytics. Contains set to methods to log events.

# Package no.nordicsemi.android.common.analytics.view

Set of common views used for enabling analytics in Nordic apps.

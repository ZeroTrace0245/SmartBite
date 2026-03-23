# SmartBit Android Client

Native Android client for SmartBit built with Kotlin, Gradle, Jetpack Compose, and MVVM.

This mobile app is a standalone client and consumes your existing backend API endpoints:
- GET/POST /meals
- GET/POST /water
- GET/POST /shoppinglist

## Tech Stack

- Kotlin
- Gradle (Kotlin DSL)
- Jetpack Compose + Material 3
- Navigation Compose (bottom navigation)
- Retrofit + OkHttp (API)
- Room (offline cache)
- Hilt (dependency injection)
- WorkManager (hydration reminders and offline sync)
- Optional integrations included as dependencies:
  - Google Pay
  - Stripe Android SDK
  - CameraX

## Project Structure

- app/src/main/java/com/smartbit/mobile/ui: Compose screens, theme, navigation, viewmodels
- app/src/main/java/com/smartbit/mobile/data: models, Room, Retrofit, repository
- app/src/main/java/com/smartbit/mobile/di: Hilt modules
- app/src/main/java/com/smartbit/mobile/work: background workers and scheduling

## Backend URL

The app currently points to emulator-local backend:
- http://10.0.2.2:5000/

Update this in app/build.gradle.kts via BuildConfig field:
- API_BASE_URL

## Build and Run

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Run on an Android emulator or physical device.

If needed, generate a Gradle wrapper once in Android Studio terminal:
- gradle wrapper

Then CLI builds are available:
- .\\gradlew.bat assembleDebug

## Notes

- Offline-first behavior is implemented for meals, water, and shopping list using Room plus periodic sync.
- Hydration reminders run with WorkManager every 2 hours.
- Offline sync runs every 15 minutes when network is available.
- Notification permission must be granted on Android 13+ for reminder notifications.

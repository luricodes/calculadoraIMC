# Architecture Overview

## Module Map
```
:app                  # Compose shell, app lifecycle, notifications, in-app updates
:core                 # Shared config, networking, persistence, sync, auth, logging
:features:imc         # BMI feature (UI, domain, data adapters)
```

## Layered Flow
```
UI (Compose screens, :features:imc)
   ↓ (StateFlow)
ViewModel (Hilt injected)
   ↓ (Use cases)
Domain (Use cases orchestrate data access)
   ↓
Data (Repositories)
   ↓
Core services (Room, Retrofit, WorkManager, Auth)
```

## Key Components
- **Hilt**: DI across modules. `ImcApp` seeds singletons.
- **Navigation**: Compose navigation graph built in `MainNavGraph`, feature destinations live in modules.
- **Networking**: Retrofit + OkHttp with retry, circuit breaker, auth interceptors. JSON via Kotlinx serialization.
- **Persistence**: Room database `AppDatabase` with migrations (1→2). Entities exposed as domain models in the feature.
- **Auth**: AppAuth-based `AuthManager`, secure storage via `EncryptedSharedPreferences`.
- **Offline Sync**: `ImcRepositoryImpl` writes to Room, `SyncScheduler` triggers `SyncWorker` to process pending rows.
- **Uploads**: `FileUploadRepository` + `UploadWorker` for presigned URL uploads.
- **Feature Flags**: `FeatureFlagManager` (SharedPreferences) + optional Remote Config stub.
- **Logging & Observability**: Timber with `CrashReportingTree`, placeholders for Crashlytics/Sentry and performance tracing hooks.
- **Theming**: `ThemePreferences` + feature toggle controlling persisted dark mode.

## Data Flow Example (BMI Entry)
1. User enters weight/height → `ImcViewModel.onCalculate`.
2. `CalculateImcUseCase` returns BMI + classification.
3. `PersistImcRecordUseCase` saves to Room via `ImcRepositoryImpl`.
4. `SyncScheduler.enqueueImmediateSync()` schedules background sync when online.
5. UI observes history via `ObserveImcHistoryUseCase` and updates Compose list.

## Error Handling
- Network calls wrapped via `safeApiCall`, results bubbled up as `Result`.
- Global coroutine errors logged by Timber (see `LoggingInitializer`).
- WorkManager jobs auto-retry with exponential backoff.

## Extensibility Notes
- Add new features under `:features:<name>` following same pattern.
- Additional remote config providers can be plugged into `RemoteConfigManager`.
- Replace crash reporting by providing a custom Timber tree in `LoggingInitializer`.

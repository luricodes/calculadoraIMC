# Runbook

## Build & Signing
1. Populate `google-services.json` with environment-specific Firebase credentials (placeholder checked in).
2. Export signing secrets (never commit):
   ```bash
   export RELEASE_STORE_FILE=/secure/keystore.jks
   export RELEASE_STORE_PASSWORD=...
   export RELEASE_KEY_ALIAS=...
   export RELEASE_KEY_PASSWORD=...
   ```
3. Run `./RELEASE/build_release.sh`. Bundle copied to `release/app-release.aab`.
4. Verify bundle with `bundletool validate --bundle release/app-release.aab`.

## Configuration Overrides
- **Feature flags**: Use `FeatureFlagManager` debug menu (`adb shell setprop imc.flag.<key> true`) or populate via Remote Config once backend ready.
- **API base URL**: Update via Firebase Remote Config key `api_base_url`; fallback `https://api.example.com`.
- **Auth endpoints**: Configure `OidcConfig` values inside secure remote config before release.

## Secrets Handling
- Keystores stored outside repo; path injected via env var.
- Tokens stored using `EncryptedSharedPreferences` (`TokenStorage`).
- Crashlytics/Sentry DSNs supplied via gradle properties or remote config (TODO placeholders in code comments).

## Database Maintenance
- Migrations live in `core/database/migration`. Current schema version = 2.
- To reset locally: `adb shell run-as com.comunidadedevspace.imc rm databases/imc.db`.
- For rollback: restore `imc.db` backup then disable sync via feature flag to avoid replays.

## Offline & Sync
- Offline writes queue in Room. WorkManager (`SyncWorker`) retries with exponential backoff.
- Force sync via `adb shell cmd jobscheduler run -f com.comunidadedevspace.imc 0`.
- Upload worker expects presigned URL + file URI via `SyncScheduler.enqueueUpload`.

## Observability
- Timber logs accessible via `adb logcat -s IMC` (use filters).
- Crash reporting: integrate Crashlytics/Sentry by supplying SDK initialization in `CrashReportingTree`.
- Performance tracing hooks ready via `Timber` instrumentation; extend as needed.

## Emergency Rollback
1. Disable risky features via Remote Config / feature flags (`FeatureFlagKeys`).
2. If migrations cause issues: ship hotfix build with reverted schema or instruct users to clear app data.
3. Upload previous stable AAB and promote to production.

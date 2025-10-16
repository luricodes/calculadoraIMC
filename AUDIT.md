# One-Shot Production Hardening Audit

## Overview
Baseline project is a single-module, XML-based IMC calculator with minimal architecture. No security, persistence, or production readiness present. Below is the prioritized remediation backlog executed in this overhaul.

## Prioritized Remediation Backlog

### P0 – Critical blockers
1. **Lack of production-grade architecture & modularization**  
   *Problem*: Single `:app` module mixes UI, domain, and data in Activities without testable abstractions.  
   *Change*: Introduce layered, Hilt-enabled modular architecture with `:core` and `:features:imc`, Compose UI, repositories, use cases, and centralized config/logging.  
   *Commit*: `chore(release): one-shot android overhaul`.  
   *Risk*: Medium – large refactor touches most files.  
   *Rollback*: Revert to pre-overhaul commit/branch.

2. **No secure auth/token handling**  
   *Problem*: App lacks authentication; production release requires OIDC/OAuth2.  
   *Change*: Integrate AppAuth-based `AuthManager`, encrypted storage, token refresh pipeline, and authorization interceptors.  
   *Commit*: `chore(release): one-shot android overhaul`.  
   *Risk*: Medium – depends on backend configuration.  
   *Rollback*: Disable auth flows via feature flag and revert dependencies.

3. **Networking stack missing**  
   *Problem*: No HTTP client, resilience, TLS hardening, or serialization.  
   *Change*: Add Retrofit/OkHttp stack with TLS 1.2+, retries, caching, circuit breaker placeholder, and strict serialization.  
   *Risk*: Low.  
   *Rollback*: Remove networking module and interceptors.

4. **Release packaging insecure**  
   *Problem*: Release build lacks minification, signing instructions, and secure gradle properties.  
   *Change*: Enable R8/shrinkResources, add signing placeholders, release script, release documentation.  
   *Risk*: Low.  
   *Rollback*: Disable shrink in `build.gradle.kts` and revert script.

### P1 – High priority
1. **No persistence/offline support**  
   *Change*: Add Room database, migrations, offline-first sync queue, WorkManager workers.  
   *Risk*: Medium due to migration errors.  
   *Rollback*: Clear app data and disable workers.

2. **No feature flags / remote config**  
   *Change*: Implement local feature flag store with optional Firebase Remote Config adapter.  
   *Risk*: Low.  
   *Rollback*: Set all flags to default `false`.

3. **Observability missing**  
   *Change*: Introduce Timber logging, Crashlytics/Sentry hooks, performance tracing, structured logs.  
   *Risk*: Low.  
   *Rollback*: Remove observers/logging modules.

### P2 – Medium priority
1. **UI modernization & accessibility gaps**  
   *Change*: Migrate to Compose Material3, add dynamic theming, i18n (EN/DE), accessibility labels, navigation with deep links.  
   *Risk*: Low.  
   *Rollback*: Restore XML UI module.

2. **Notifications & in-app updates absent**  
   *Change*: Add FCM client, notification channels, Play Core in-app updates integration.  
   *Risk*: Low.  
   *Rollback*: Disable modules and unregister services.

### P3 – Nice-to-have
1. **Performance targets undefined**  
   *Change*: Document targets (<2s cold start), add measurement hooks.  
   *Risk*: None.  
   *Rollback*: Remove docs if unneeded.

2. **Security checklist lacking**  
   *Change*: Provide checklist coverage in documentation.  
   *Risk*: None.  
   *Rollback*: N/A.

## Static Analysis Suppressions
Detekt configuration suppresses:
- `TooManyFunctions` for `FeatureFlagManager` due to centralized flag APIs.  
- `MagicNumber` for Room migration IDs (Room requirement).  
- `LongParameterList` for Retrofit service constructors (DI-friendly).  
Justifications recorded inline within `detekt.yml`.


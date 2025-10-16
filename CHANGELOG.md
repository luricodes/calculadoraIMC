# Changelog

## 1.1.0 – One-shot Production Overhaul
- Migrated UI to Jetpack Compose with Material 3, dynamic theming, dark-mode toggle, and EN/DE localization.
- Introduced modular architecture (`:core`, `:features:imc`) with Hilt DI, Room persistence, and Retrofit networking.
- Added secure OAuth2/OIDC scaffolding using AppAuth and encrypted token storage.
- Implemented offline-first flow with WorkManager sync, retry, and presigned file upload support.
- Integrated Firebase messaging placeholders, notification channels, in-app updates, and centralized logging.
- Hardened build: minify/shrink for release, TLS-enforced OkHttp, feature flags, Remote Config hooks, and release scripting.

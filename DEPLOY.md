# Deployment Guide

## Pre-flight Checklist
- [ ] Release notes updated in `CHANGELOG.md`.
- [ ] `RUNBOOK.md` reviewed for latest secrets/process changes.
- [ ] Crashlytics/Firebase credentials verified in `google-services.json`.
- [ ] Manual QA passed (see Acceptance Criteria list below).

## Play Console Flow
1. Build release bundle (`./RELEASE/build_release.sh`).
2. In Play Console → `Release` → `Production` → `Create new release`.
3. Upload `release/app-release.aab`.
4. Attach release notes (copy from `CHANGELOG.md`).
5. Enable Play App Signing + Play Integrity (if not already on).
6. Targeted staged rollout:
   - Start at **10%** of users.
   - Monitor for 24h (Crashlytics, Play Console vitals, custom logs).
   - Increase to 50% if stable, then 100% after another 24h.
7. Post-launch, verify metrics (ANR/crash rate, retention, conversion).

## Rollback Options
- Halt rollout or decrease percentage in Play Console.
- Upload previous stable AAB and promote.
- Use Remote Config `kill_switch` (configure in backend) to disable risky flows.
- For DB regressions, instruct support to clear app data or ship hotfix migration.

## Monitoring
- **Crashlytics/Sentry**: ensure DSNs configured; monitor crash-free sessions.
- **Play Console**: ANR/crash dashboards, device vitals.
- **Custom Logs**: `adb logcat` filters and backend dashboards.

## Acceptance Criteria (Manual)
- App boots in < 2s on Pixel 4 (documented target).
- OIDC login + refresh (manual via staging identity provider).
- Offline BMI entry syncs once connectivity resumes.
- Presigned upload completes successfully (use staging endpoint).
- FCM push displayed in foreground/background.
- Language toggle surfaces EN/DE strings.
- Dark mode toggle persists preference.

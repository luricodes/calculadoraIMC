# Release Instructions

1. Ensure `google-services.json` contains production Firebase credentials (see `RUNBOOK.md`).
2. Export signing secrets:
   ```bash
   export RELEASE_STORE_FILE=/secure/path/keystore.jks
   export RELEASE_STORE_PASSWORD=***
   export RELEASE_KEY_ALIAS=***
   export RELEASE_KEY_PASSWORD=***
   ```
3. Run the release script:
   ```bash
   ./RELEASE/build_release.sh
   ```
4. Upload `release/app-release.aab` to the Play Console as described in `DEPLOY.md`.
5. Tag the release and archive the generated artifacts securely.

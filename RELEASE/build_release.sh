#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${RELEASE_STORE_FILE:-}" || -z "${RELEASE_STORE_PASSWORD:-}" || -z "${RELEASE_KEY_ALIAS:-}" || -z "${RELEASE_KEY_PASSWORD:-}" ]]; then
  echo "Missing signing environment variables." >&2
  echo "Required: RELEASE_STORE_FILE, RELEASE_STORE_PASSWORD, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD" >&2
  exit 1
fi

export ORG_GRADLE_PROJECT_RELEASE_STORE_FILE="$RELEASE_STORE_FILE"
export ORG_GRADLE_PROJECT_RELEASE_STORE_PASSWORD="$RELEASE_STORE_PASSWORD"
export ORG_GRADLE_PROJECT_RELEASE_KEY_ALIAS="$RELEASE_KEY_ALIAS"
export ORG_GRADLE_PROJECT_RELEASE_KEY_PASSWORD="$RELEASE_KEY_PASSWORD"

./gradlew --no-daemon clean bundleRelease

mkdir -p release
cp app/build/outputs/bundle/release/app-release.aab release/app-release.aab

echo "Release bundle available at release/app-release.aab"

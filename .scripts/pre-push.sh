#!/bin/sh

printf "
----------------------------
Pre-Push Check(s):
----------------------------
"

# Use the MVN wrapper if available
MVN="./mvnw"
[ -x "$MVN" ] || MVN="mvn"
# ========================================
# Staging Check
# ========================================
printf "[\033[0;33m%s\033[0m] Checking... " "Staging"
if ! git diff --quiet; then
  printf '\033[0;31m%s\033[0m' "Failed!"
  printf '%s\n' "	- The working directory has unstaged file changes."
  printf '\n\033[0;31m'
  git diff --shortstat
  printf '\033[0m\n'
  printf '%s\n' "This may lead to a false positive during pre-push validation.
Checks may pass because of changes present in your local repository that will not be included in the push.

Stash or stage the changes."
  printf '\nHint:\t\033[0;36m%s\033[0m' "git add ." "git stash --keep-index"
  exit 1
fi
printf '\033[0;32m%s\033[0m\n' "Passed."
# ========================================
# Smoke-Test Check
# ========================================
printf "[\033[0;33m%s\033[0m] Checking... " "Smoke-Test"
STDOUT=$($MVN test -Dgroups="smoke")
EXIT_CODE=$?
if [ "$EXIT_CODE" -ne 0 ]; then
  printf '\033[0;31m%s\033[0m' "Failed!"
  printf '%s\n' " - There are test failures. Resolve and restage."
  printf '\n\033[0;31m'
  printf '%s\n' "$STDOUT" | grep "\[ERROR\]"
  printf '\033[0m\n'
  exit 1
fi
printf '\033[0;32m%s\033[0m\n' "Passed."
# ========================================
# Integration-Test Check
# ========================================
printf "[\033[0;33m%s\033[0m] Checking... " "Integration-Test"
STDOUT=$($MVN verify)
EXIT_CODE=$?
if [ "$EXIT_CODE" -ne 0 ]; then
  printf '\033[0;31m%s\033[0m' "Failed!"
  printf '%s\n' " - There are test failures. Resolve and restage."
  printf '\n\033[0;31m'
  printf '%s\n' "$STDOUT" | grep "\[ERROR\]"
  printf '\033[0m\n'
  exit 1
fi
printf '\033[0;32m%s\033[0m\n' "Passed."
# ========================================
# ANSI Color Escape Codes
# ========================================
# YELLOW='\033[0;33m'
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# CYAN='\033[0;36m'
# BLUE='\033[0;34m'
# NONE='\033[0m'
# ========================================
printf '%s\n' "----------------------------"

#!/bin/sh
# Note: Run from repository root, not the ./scripts dir.

# Use the MVN wrapper if available
MVN="./mvnw"
[ -x "$MVN" ] || MVN="mvn"

# ANSI Color Escape Codes
# YELLOW='\033[0;33m'
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# NONE='\033[0m'

unstaged_source_files=$(git diff --name-only | grep '\.java$')
if [ -n "$unstaged_source_files" ]; then
  printf '\n\n\033[0;31m%s\033[0m\n\n' "[Halted] Unstaged Changes!

  The working directory has unstaged source file changes.
  This may lead to a false positive during pre-push validation.
  Test cases may pass because of changes present in your local repository that will not be pushed to the remote.

  Stage the changes, or stash them."
  exit 1
fi

printf  '\n\n\033[0;33m%s\033[0m\n' "Running integration tests..."
if ! $MVN clean verify; then
  printf '\n\n\033[0;31m%s\033[0m\n\n' "[Fail] Integration Test(s) failed!"
  exit 1
fi

printf  '\n\n\033[0;32m%s\033[0m\n\n' "[Verified] Push to remote allowed."
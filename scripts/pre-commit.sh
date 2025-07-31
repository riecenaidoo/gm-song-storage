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
  This may lead to a false positive during pre-commit validation.
  Test cases may pass because of changes present in your local repository that will not be included in the commit.

  Stage the changes, or stash them."
  exit 1
fi

printf  '\n\n\033[0;33m%s\033[0m\n' "Checking formatting..."
if ! $MVN spotless:check; then
  printf '\n\n\033[0;31m%s\033[0m\n\n' "[Dirty] Resolve and restage files.

  (Hint: mvn spotless:apply)
  "
  exit 1
fi
printf  '\n\n\033[0;32m%s\033[0m\n\n' "[Clean] Lint OK."

staged_src_files=$(git diff --cached --name-only | grep '\.java$')
if [ -n "$staged_src_files" ]; then
  printf  '\n\n\033[0;33m%s\033[0m\n' "Running unit tests..."
  if ! $MVN clean test; then
    printf '\n\n\033[0;31m%s\033[0m\n\n' "[Fail] Unit test(s) failed!"
    exit 1
  fi
else
  printf  '\n\n\033[0;33m%s\033[0m\n' "No staged source file changed. Skipped unit tests."
fi
printf  '\n\n\033[0;32m%s\033[0m\n\n' "[OK] Looks good to me.

(Hint: --no-verify for quick amending)"

unstaged_files=$(git diff --name-only)
if [ -n "$unstaged_files" ]; then
  (printf '%s\n\n' "Unstaged Changes: (Hint: Q to exit)"; printf '%s\n' "$unstaged_files") | less
fi
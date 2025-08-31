#!/bin/sh

printf "
----------------------------
Pre-Commit Check(s):
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
  printf '%s\n' "This may lead to a false positive during pre-commit validation.
Checks may pass because of changes present in your local repository that will not be included in the commit.

Stash or stage the changes."
  printf '\nHint:\t\033[0;36m%s\033[0m' "git add ." "git stash --keep-index"
  exit 1
fi
printf '\033[0;32m%s\033[0m\n' "Passed."
# ========================================
# Formatting Check
# ========================================
printf "[\033[0;33m%s\033[0m] Checking... " "Format"
# ... 2>&1 > /dev/null should work, but Maven [ERROR] is just formatted text on STDOUT
STDOUT=$($MVN spotless:check)
EXIT_CODE=$?
if [ "$EXIT_CODE" -ne 0 ]; then
	printf '\033[0;31m%s\033[0m' "Failed!"
	printf '%s\n' "	- There are files with dirty format. Run formatting and restage."
	printf '\n\033[0;31m'
	printf '%s\n' "$STDOUT" | grep "\[ERROR\]"
	printf '\033[0m\n'

	printf 'Hint:\t\033[0;36m%s\033[0m\n' "mvn spotless:apply" "make format"
  exit 1
fi
printf '\033[0;32m%s\033[0m\n' "Passed."
# ========================================
# Unit-Test Check
# ========================================
staged_src_files=$(git diff --cached --name-only | grep '\.java$')
if [ -n "$staged_src_files" ]; then
  printf "[\033[0;33m%s\033[0m] Checking... " "Unit-Test"
  STDOUT=$($MVN test -Dgroups="unit")
  EXIT_CODE=$?
  if [ "$EXIT_CODE" -ne 0 ]; then
    printf '\033[0;31m%s\033[0m' "Failed!"
    printf '%s\n' " - There are Smoke test failures; the build is unstable. Resolve and restage."
    printf '\n\033[0;31m'
    printf '%s\n' "$STDOUT" | grep "\[ERROR\]"
    printf '\033[0m\n'
    exit 1
  fi
  printf '\033[0;32m%s\033[0m\n' "Passed."
fi
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

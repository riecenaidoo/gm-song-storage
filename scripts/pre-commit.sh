#!/bin/sh
# Note: Run from repository root, not the ./scripts dir.

# or use the MVN wrapper.
MVN=mvn

# ANSI Color Escape Codes
# YELLOW='\033[0;33m'
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# NONE='\033[0m'

printf  '\n\n\033[0;33m%s\033[0m\n' "Linting..."
if ! $MVN spotless:check; then
  printf '\n\n\033[0;31m%s\033[0m\n\n' "Lint check failed! Resolve and restage files."
  exit 1
fi

printf  '\n\n\033[0;33m%s\033[0m\n' "Unittesting..."
if ! $MVN clean test; then
  printf '\n\n\033[0;31m%s\033[0m\n\n' "Unittest(s) failed! Check code."
  exit 1
fi
printf  '\n\n\033[0;32m%s\033[0m\n\n' "OK - looks good to me. (Hint: --no-verify for quick amending)"

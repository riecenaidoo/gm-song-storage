#!/bin/sh
# Note: Run from repository root, not the ./scripts dir.

# or use the MVN wrapper.
MVN=mvn

# ANSI Color Escape Codes
# YELLOW='\033[0;33m'
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# NONE='\033[0m'

printf  '\n\n\033[0;33m%s\033[0m\n' "Verifying..."
if ! $MVN clean verify; then
  printf '\n\n\033[0;31m%s\033[0m\n\n' "[Unstable] Integration Test(s) failed!"
  exit 1
fi

printf  '\n\n\033[0;32m%s\033[0m\n\n' "[Stable] Push to remote allowed."
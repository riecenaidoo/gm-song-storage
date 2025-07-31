# ========================================
# ANSI Color Escape Codes
# YELLOW='\033[0;33m'
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# NONE='\033[0m'
# ========================================

SRC_FILES := $(shell find . -type f -name "*.java")

.PHONY: all images artifacts git-hooks

all: images artifacts git-hooks

# ========================================
# Images
# ========================================

images: .made/gm-song-storage-api

.made/gm-song-storage-api: application/target/application-1.0-SNAPSHOT.jar
	docker build -t gm-song-storage-api:dev -f Dockerfile-targetonly .
	mkdir -p ./.made	# Ensure existence
	touch ./.made/gm-song-storage-api	# Timestamp file
	docker compose down api	# Teardown out of date containers

# ========================================
# Artifacts
# ========================================

artifacts: application/target/application-1.0-SNAPSHOT.jar

application/target/application-1.0-SNAPSHOT.jar: $(SRC_FILES)
	mvn spotless:apply
	mvn package

# ========================================
# Git Hooks
# ========================================

git-hooks: .git/hooks/pre-commit .git/hooks/pre-push	## update all Git hooks in the local repository

.git/hooks/pre-commit: scripts/pre-commit.sh	## updates the pre-commit hook in the local repository
# Formatting and unit-testing should be performed before each commit to the remote repository.
# [Git Hook](https://git-scm.com/book/ms/v2/Customizing-Git-Git-Hooks)
	@if [ -f .git/hooks/pre-commit ]; then \
		printf '\n\033[0;31m%s\033[0m\n\n' 'Pre-existing Pre-Commit Hook:'; \
		cat .git/hooks/pre-commit; \
	fi
	cat scripts/pre-commit.sh > .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit	# Ensure the script is executable.
	@printf '\n\033[0;33m%s\033[0m\n\n' "Pre-Commit Hook added:"
	@cat .git/hooks/pre-commit
	@printf '\n\033[0;33m%s\033[0m\n\n' "The Pre-Commit Hook can be removed with 'rm .git/hooks/pre-commit'"

.git/hooks/pre-push: scripts/pre-push.sh	## updates the pre-push hook in the local repository
# Build must be stable before pushing to the remote
# [Git Hook](https://git-scm.com/book/ms/v2/Customizing-Git-Git-Hooks)
	@if [ -f .git/hooks/pre-push ]; then \
		printf '\n\033[0;31m%s\033[0m\n\n' 'Pre-existing Pre-Push Hook:'; \
		cat .git/hooks/pre-push; \
	fi
	cat scripts/pre-push.sh > .git/hooks/pre-push
	chmod +x .git/hooks/pre-push	# Ensure the script is executable.
	@printf '\n\033[0;33m%s\033[0m\n\n' "Pre-Push Hook added:"
	@cat .git/hooks/pre-push
	@printf '\n\033[0;33m%s\033[0m\n\n' "The Pre-Push Hook can be removed with 'rm .git/hooks/pre-push'"

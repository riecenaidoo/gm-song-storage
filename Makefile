# See [7.2.1 General Conventions for Makefiles](https://www.gnu.org/prep/standards/html_node/Makefile-Basics.html)
SHELL = /bin/sh

init: project git	## default (no-arg) target to initialise the project and local repository

# See [7.2.6 Standard Targets for Users > 'all'](https://www.gnu.org/prep/standards/html_node/Standard-Targets.html)
all: init java docker	## primary target for creating all Project artifacts

.PHONY: init all
# ========================================
# Environment Variables
# ========================================
# [6.2.4 Conditional Variable Assignment](https://www.gnu.org/software/make/manual/html_node/Conditional-Assignment.html)
# [6.10 Variables from the Environment](https://www.gnu.org/software/make/manual/html_node/Environment.html)
MVN ?= mvn
DOCKER ?= docker
GIT ?= git
# ========================================
# Project Initialisation
# ========================================
project: .made/project-version	## alias for initialising the Project

MADE := ./.made

$(MADE):
	mkdir $(MADE)

# [Get Maven Project Version onto CMD](https://medium.com/@haroldfinch01/how-to-get-maven-project-version-to-the-bash-command-line-7c32ca78eeb8)
# The command is slow, so we 'cache' it into a file.
$(MADE)/project-version: $(MADE) pom.xml	## cache the project version for use in script
	$(MVN) help:evaluate -Dexpression=project.version -q -DforceStdout > $(MADE)/project-version

PROJECT_VERSION := $(shell cat $(MADE)/project-version)

rm-project:	## remove all Project initialisation artifacts
	rm -f $(MADE)/project-version

.PHONY: project
# ========================================
# Java Artifacts
# ========================================
java: application/target/application-$(PROJECT_VERSION).jar	## alias for creating all Java artifacts

# [6.2.2 Simply Expanded Variable Assignment](https://www.gnu.org/software/make/manual/html_node/Simple-Assignment.html)
SRC_FILES := $(shell find . -type f -name "*.java")

application/target/application-%.jar: $(SRC_FILES)
	$(MVN) package -DskipTests

rm-java:	## remove all Java artifacts produced by this script
	$(MVN) clean

.PHONY: java rm-java
# ========================================
# Docker Artifacts
# ========================================
docker: .made/gm-song-storage-api	## alias for creating all Docker artifacts

.made/gm-song-storage-api: $(MADE) application/target/application-$(PROJECT_VERSION).jar
	$(DOCKER) build -t gm-song-storage-api:dev -f Dockerfile-targetonly .
	touch $(MADE)/gm-song-storage-api	# Timestamp file

rm-docker:	## remove all Docker artifacts produced by this script
	$(DOCKER) compose down -v
	@if ! $(DOCKER) ps -a | grep -F "gm-song-storage-api:dev"; then \
  		$(DOCKER) rmi gm-song-storage-api:dev; \
  	else \
		printf '\033[0;31m[%s]\t\033[0m' "WARN"; \
		printf '\033[0;31m%s\033[0m' "Removal aborted: \"gm-song-storage-api:dev\" is still in use."; \
	fi
	rm -f $(MADE)/gm-song-storage-api

.PHONY: docker rm-docker
# ========================================
# Git Artifacts
# - [Git Hooks](https://git-scm.com/book/ms/v2/Customizing-Git-Git-Hooks)
# ========================================
git: .git/hooks/pre-commit .git/hooks/pre-push	## alias for initialising the local repository; creates Git artifacts

.git/hooks/pre-commit: .scripts	## updates the pre-commit hook in the local repository
	@if [ -f .git/hooks/pre-commit ]; then \
		printf '\n\033[0;31m%s\033[0m\n\n' 'Pre-existing Pre-Commit Hook:'; \
		cat .git/hooks/pre-commit; \
	fi
	cat .scripts/pre-commit.sh > .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit	# Ensure the script is executable.
	@printf '\n\033[0;33m%s\033[0m\n\n' "Pre-Commit Hook added:"
	@cat .git/hooks/pre-commit
	@printf '\n\033[0;33m%s\033[0m\n\n' "The Pre-Commit Hook can be removed with 'rm .git/hooks/pre-commit'"

.git/hooks/pre-push: .scripts	## updates the pre-push hook in the local repository
	@if [ -f .git/hooks/pre-push ]; then \
		printf '\n\033[0;31m%s\033[0m\n\n' 'Pre-existing Pre-Push Hook:'; \
		cat .git/hooks/pre-push; \
	fi
	cat .scripts/pre-push.sh > .git/hooks/pre-push
	chmod +x .git/hooks/pre-push	# Ensure the script is executable.
	@printf '\n\033[0;33m%s\033[0m\n\n' "Pre-Push Hook added:"
	@cat .git/hooks/pre-push
	@printf '\n\033[0;33m%s\033[0m\n\n' "The Pre-Push Hook can be removed with 'rm .git/hooks/pre-push'"

rm-git:	## remove all Git artifacts produced by this script
	rm -f .git/hooks/pre-commit
	rm -f .git/hooks/pre-push

.PHONY: git rm-git
# ========================================
# Utilities
# ========================================
# See [7.2.6 Standard Targets for Users > 'clean'](https://www.gnu.org/prep/standards/html_node/Standard-Targets.html)
clean: rm-project rm-java rm-docker rm-git	## alias for cleaning up all artifacts produced by this script

format:	## run formatting
	$(MVN) spotless:apply
	$(GIT) status -s

help:  ## show available targets
	@printf "%s\n" \
	"------------------" \
	"Available Commands" \
	"------------------"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) \
		| awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'
	@printf "%s\n" \
	"------------------"

.PHONY: clean format help
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

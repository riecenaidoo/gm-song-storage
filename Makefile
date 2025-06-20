# ANSI Color Escape Codes
# YELLOW='\033[0;33m'
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# NONE='\033[0m'

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

# Skill Registry — proyecto_progra-03

This file defines the **local** project-specific skills and auto-invoke rules for **proyecto_progra-03**. It is used by AI agents to resolve and inject compact rules during code editing, reviews, and SDD phases.

---

## Compact Rules

### 1. netbeans-form-safe-edit
**Trigger**: Editing `.form` files or Swing UI components in NetBeans.
**Context**: Java Swing, NetBeans IDE, `.form` files.

```rules
- NEVER edit `.form` files manually. Use NetBeans designer only.
- NEVER mix hand-written code with NetBeans-generated code in `.java` files linked to `.form` files.
- ALWAYS use the NetBeans designer for UI changes.
- ALWAYS separate logic from UI. Keep business logic in Controllers, not in Views.
- ALWAYS validate UI changes in the NetBeans preview before committing.
```

### 2. branch-pr
**Trigger**: Creating, opening, or preparing pull requests for review.
**Context**: Git, GitHub, pull requests.

```rules
- ALWAYS link PRs to GitHub issues.
- ALWAYS follow conventional commits in PR titles.
- ALWAYS include a summary of changes in the PR description.
- ALWAYS verify that the PR does not break existing functionality.
- NEVER include sensitive data (credentials, API keys) in PRs.
```

### 3. chained-pr
**Trigger**: PRs exceeding 400 lines, stacked PRs, or review slices.
**Context**: Git, GitHub, large changes.

```rules
- ALWAYS split large changes into smaller, focused PRs.
- ALWAYS ensure each PR is reviewable in isolation.
- ALWAYS use `work-unit-commits` to plan commits for chained PRs.
- NEVER merge a PR that exceeds 400 lines without approval.
```

### 4. work-unit-commits
**Trigger**: Implementing features or splitting commits for review.
**Context**: Git, commits, chained PRs.

```rules
- ALWAYS group related changes in a single commit.
- ALWAYS write clear, descriptive commit messages.
- ALWAYS follow conventional commits.
- ALWAYS keep commits small and focused.
```

---

## User Skills

| Action | Skill |
|--------|-------|
| Editing `.form` files or Swing UI components in NetBeans | netbeans-form-safe-edit |
| Creating or updating pull requests | branch-pr |
| Changes exceeding 400 lines or requiring stacked PRs | chained-pr |
| Implementing features or splitting commits for review | work-unit-commits |

---

## References

- [AGENTS.md](./../AGENTS.md)
- [netbeans-form-safe-edit SKILL.md](./../.opencode/skills/netbeans-form-safe-edit/SKILL.md)
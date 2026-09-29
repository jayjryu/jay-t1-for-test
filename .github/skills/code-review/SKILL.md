---
name: code-review
description: "Review pull requests with GitHub Copilot. Use for code review, pull request review, re-review, or reviewing changed code."
---

# Code Review

Review the pull request diff and the relevant surrounding code, tests, configuration, and dependencies before reporting an issue. Do not infer defects from a diff fragment alone.

Report only actionable findings that can cause a bug, security risk, regression, compatibility problem, or missing test coverage. Ignore formatting, lint-only issues, subjective preferences, and unrelated existing code.

For every finding:

- Cite the file and line.
- Explain the concrete impact and reproduction path.
- Give a focused correction or a clarifying question.
- State the validation needed after the change.

Write all review summaries and comments in Korean. Preserve source-code identifiers, function names, API names, commands, paths, error messages, and technical terms in their original form.

Use `High`, `Medium`, or `Low` only when the severity reflects verified impact. Treat Copilot feedback as advisory: a human reviewer must validate findings and approve the pull request.

For a re-review, inspect only changes since the previous review and do not repeat resolved comments unless the issue remains present.
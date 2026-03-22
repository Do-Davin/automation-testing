# LAB01 — JUnit Jupiter Integration Tests for NestJS Login API

Tests the live NestJS API on Render using plain Java 17 + JUnit Jupiter 5.  
No Maven, no Gradle — JARs are auto-downloaded on first run.

## Target
`https://automation-testing-g37b.onrender.com`

## Structure
```
LAB01/
├── src/
│   └── App.java              # Entry point
├── test/
│   └── AuthLoginTest.java    # 7 JUnit Jupiter tests
├── lib/                      # JARs auto-downloaded here
├── out/                      # Compiled .class files
├── run_tests.sh              # One-command build + run
└── .github/
    └── workflows/
        └── ci.yml            # GitHub Actions pipeline
```

## How to Run

```bash
chmod +x run_tests.sh
./run_tests.sh
```

First run downloads ~3MB of JUnit JARs into `lib/`. Subsequent runs are instant.

## Tests

| # | Name | Expects |
|---|------|---------|
| T1 | API reachable | Any valid HTTP status |
| T2 | Register new user | 201 Created |
| T3 | Valid login | 200 + token in body |
| T4 | Wrong password | 401 Unauthorized |
| T5 | Unknown user | 401 Unauthorized |
| T6 | Empty body | 400 or 401 |
| T7 | Response is JSON | Content-Type: application/json |

## Requirements
- Java 17+
- `curl` (pre-installed on macOS/Linux)
- Internet connection (to reach Render + Maven Central on first run)

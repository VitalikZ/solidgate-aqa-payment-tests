# Solidgate AQA test framework

Java + Selenide + TestNG. Покриває створення Payment Page, оплату через UI та верифікацію ордера через `/status`.

## Стек

| Шар | Технологія |
|---|---|
| Мова | Java 25 LTS |
| Build | Gradle 9.x (Kotlin DSL) |
| UI | [Selenide](https://selenide.org/) 7.x |
| Test framework | TestNG 7.x |
| API | REST Assured |
| Звіти | Allure |
| Утиліти | Lombok, AssertJ, SLF4J + Logback |

## Швидкий старт

Потрібно: Java 25 LTS, Chrome (або Firefox/Edge), креденшіали Solidgate sandbox.

**1. Встановити Java 25**

macOS:
```bash
brew install openjdk@25
export JAVA_HOME=/opt/homebrew/opt/openjdk@25/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
java -version    # має показати: openjdk version "25.x.x"
```

Альтернатива - через sdkman: `sdk install java 25-tem`.

**2. Підставити ключі у `.env`**

```bash
git clone <repo-url>
cd solidgate-aqa-payment-tests
cp .env.example .env
```

Відкрити `.env` у редакторі і замінити placeholder значення на реальні:
```
SOLIDGATE_MERCHANT_ID=api_pk_xxxxxxxx_xxxx_xxxx_xxxx_xxxxxxxxxxxx
SOLIDGATE_SECRET_KEY=api_sk_xxxxxxxx_xxxx_xxxx_xxxx_xxxxxxxxxxxx
```

**3. Запустити тести**

```bash
./gradlew test
```

Gradle сам підхопить `.env` у тестове оточення. Очікуваний результат: **3 PASSED** (1 unit + 2 e2e параметризовані).

> Selenide завантажує WebDriver через Selenium Manager - окремо chromedriver встановлювати не потрібно.

## Тести

| Тест | Тип |
|---|---|
| `SignatureGeneratorTest` | unit - HMAC-SHA512 |
| `PaymentFlowTest.shouldPayOrderViaPaymentPage` | e2e, параметризований `@DataProvider` (USD, EUR) |
| `ApiSmokeTest.shouldQueryStatusOfExampleOrder` | API smoke - окремий suite `smoke.xml` |

## Команди запуску

| Команда | Що робить |
|---|---|
| `./gradlew test` | Усі тести у Chrome |
| `./gradlew test -Dheadless=true` | Headless mode |
| `./gradlew test -Dbrowser=firefox` | Firefox |
| `./gradlew test -Dbrowser=edge` | Edge |
| `./gradlew test --tests "*SignatureGenerator*"` | Лише unit |
| `./gradlew test -PsuiteFile=src/test/resources/suites/smoke.xml` | API smoke без UI |

## Звіти

```bash
./gradlew test allureReport
./gradlew allureServe
```

Або відкрити `build/reports/tests/test/index.html` (Gradle HTML report).

## Інші браузери

Chrome за замовчуванням. Перемикання через `-Dbrowser=`:

```bash
./gradlew test -Dbrowser=firefox -Dheadless=true
```

Підтримувані значення (`BrowserType` enum): `chrome`, `firefox`, `edge`, `safari`.

## CI (GitHub Actions)

Workflow `.github/workflows/test.yml` запускає тести на push/PR у `main`.

Секрети репозиторію (Settings -> Secrets and variables -> Actions):
- `SOLIDGATE_MERCHANT_ID`
- `SOLIDGATE_SECRET_KEY`

Ручний запуск з вибором браузера: вкладка Actions -> "Tests" -> Run workflow.

## Структура проєкту

```
src/
├── main/java/com/solidgate/aqa/
│   ├── api/              # SolidgateApiClient + SignatureGenerator + DTOs
│   ├── browser/          # BrowserType + WebDriverFactory
│   ├── config/           # TestConfig
│   ├── pages/            # PaymentPage, SuccessPage
│   └── util/             # CardData, TestCards, OrderIdGenerator
└── test/
    ├── java/com/solidgate/aqa/
    │   ├── BaseTest.java
    │   ├── fixtures/     # PaymentScenario + PaymentScenarios (@DataProvider)
    │   ├── listeners/    # @RequiresRetry + RetryListener
    │   └── tests/
    │       ├── PaymentFlowTest.java
    │       ├── ApiSmokeTest.java
    │       └── unit/SignatureGeneratorTest.java
    └── resources/
        ├── logback-test.xml
        ├── allure.properties
        └── suites/{testng,smoke}.xml
```

## Архітектурні рішення

- **Page Object Model з fluent API**: `paymentPage.openUrl(u).fillCard(c).submit().assertVisible()`.
- **`PaymentScenario` + `@DataProvider`**: параметризація валют/сум без дублювання коду.
- **`waitForOrderStatus` polling** замість фіксованих `sleep` - Solidgate persistить ордер асинхронно після UI submit.
- **`@RequiresRetry` маркер**: retry лише на UI-тестах, unit і API smoke лишаються deterministic.
- **`.env` auto-loading** у `build.gradle.kts`: запуск без ручного export.
- **Cross-browser layer** через `BrowserType` enum + `-Dbrowser=` flag.

## Що далі

- Cross-browser matrix у GitHub Actions
- 3DS challenge flow
- Selenium Grid / Moon для паралельного запуску
- Checkstyle / SpotBugs у CI

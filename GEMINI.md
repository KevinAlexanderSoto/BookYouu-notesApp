# Gemini CLI Project Instructions: BookYouu Notes App

This file contains foundational mandates for Gemini CLI while working on the BookYouu Notes App project. These instructions take absolute precedence over general workflows.

## 🏛️ Project Architecture
- **Multi-module Android App:** The project follows a modular architecture.
  - `:app`: Main entry point and UI navigation.
  - `:authentication`: User login and security (Biometrics).
  - `:core`: Common utilities, base classes, and shared logic.
  - `:db`: Data storage logic using Room.
  - `:permission`: Unified permission handling.
  - `:notification`: Alarm and notification management (Reminders).
  - `:ads`: AdMob integration.
  - `:moreMenu`: Settings and additional application options.
  - `:benchmark`: Performance testing.
- **MVVM + Clean Architecture:** Adhere to the Model-View-ViewModel architecture with clear separation between Data, Domain, and UI layers.
- **Dependency Injection:** Uses **Hilt** for dependency injection. Use `@AndroidEntryPoint`, `@HiltViewModel`, etc.
- **Navigation:** Uses **Compose Navigation** for all screen transitions.

## 🛠️ Engineering Standards
- **Kotlin & Jetpack Compose:** Built entirely with Kotlin and Jetpack Compose. All new UI should be Compose-based.
- **Coroutines & Flow:** Use Kotlin Coroutines for asynchronous tasks and Flow for reactive data streams.
- **Permissions:** Handle permissions through the `:permission` module. Common permissions include `POST_NOTIFICATIONS`, `RECORD_AUDIO`, `CAMERA`, and `USE_BIOMETRIC`.
- **String Resources:** Always use `strings.xml` for UI text to support localization (ES/EN).

## 🧪 Testing Strategy
- **Unit Tests:** Mandatory for all business logic (ViewModels, UseCases, Repositories).
- **Instrumented Tests:** Use for critical UI flows or database migrations in the `:db` and `:app` modules.
- **Benchmarks:** Utilize the `:benchmark` module for performance-critical code.
- **Validation:** Always run `./gradlew test` before considering a task complete.

## 🤖 AI Assistance Configuration
- **Skills:** Custom skills should be placed in `.gemini/skills/`.
- **MCP Servers:** Configure project-specific MCPs in `.gemini/settings.json`.
- **Context:** When investigating issues, focus on the relevant module (e.g., `:db` for database issues) to minimize context usage.

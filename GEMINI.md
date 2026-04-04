# Gemini CLI Project Instructions: BookYouu Notes App

This file contains foundational mandates for Gemini CLI while working on the BookYouu Notes App project. These instructions take absolute precedence over general workflows.

## 🏛️ Project Architecture
- **Multi-module Android App:** The project follows a modular architecture.
  - `:app`: Main entry point and UI navigation.
  - `:authentication`: User login and security (Biometrics).
  - `:core`: Common utilities, reusable UI components, and shared logic.
  - `:db`: Data storage logic using Room.
  - `:permission`: Unified permission handling.
  - `:notification`: Alarm and notification management (Reminders).
  - `:ads`: AdMob integration.
  - `:moreMenu`: Settings and additional application options.
  - `:payments`: Manage monthly obligations and scheduled payments.
  - `:benchmark`: Performance testing.
- **MVVM + Clean Architecture:** Adhere to the Model-View-ViewModel architecture with clear separation between Data, Domain, and UI layers.
- **Dependency Injection:** Uses **Koin** for dependency injection. Define modules per feature and use `koinViewModel()` in Composables.
- **Navigation:** Uses **Compose Navigation** for all screen transitions.

## 🎨 UI & UX Standards
- **Reusable Components:** Prioritize creating and using components in `:core:common:composables`.
  - **`SuccessStatusScreen`**: A generic screen for successful operations with title, message, and action buttons.
  - **`LabeledInput`**: Custom text field with thousands separator (dots) for numeric inputs using `NumberVisualTransformation`.
- **Performance:** Always use `remember` for complex UI state transformations like `VisualTransformation`. Re-use heavy objects like `DecimalFormat` outside of the composition loop.
- **Design:** Follow the project's dark green (`#004D40` or `#2D5D57`) and white aesthetic.

## 🛠️ Engineering Standards
- **Kotlin & Jetpack Compose:** Built entirely with Kotlin and Jetpack Compose. All new UI should be Compose-based.
- **Coroutines & Flow:** Use Kotlin Coroutines for asynchronous tasks and Flow for reactive data streams.
- **Visual Transformations:** Use `NumberVisualTransformation` for numeric input fields to ensure consistent formatting ($1.000, 10.000).
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
- **Configuration:** Maintain `.aider.conf.yml` and `.aiderignore` for external AI tool compatibility.

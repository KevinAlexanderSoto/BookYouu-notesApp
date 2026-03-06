---
name: "android-developer"
description: "Expert skill for Android development in this project. Includes Gradle, Compose, and multi-module navigation knowledge."
---

# Android Developer Skill

This skill provides expert guidance for developing the BookYouu Notes App.

## Procedures

### 1. Adding a New Dependency
- Always check `gradle/libs.versions.toml` (Version Catalog) before adding a dependency.
- Use the `:core` module for shared libraries.
- Run `./gradlew dependencies` if you suspect version conflicts.

### 2. UI Component Creation
- Use Jetpack Compose.
- Place reusable components in `:core/src/main/java/com/.../ui/components`.
- Ensure components follow the Material 3 design system used in the project.

### 3. Database Updates
- Modify entities in the `:db` module.
- Always increment the database version in `BookYouuDataBase.kt`.
- Create a migration test in the `androidTest` folder of the `:db` module.

### 4. Code Generation
- If the project uses Room or Hilt, ensure `@Generated` or similar markers are respected.
- Run `./gradlew kspKotlin` or `./gradlew build` to trigger code generation if needed.

## Resources
- [Android Developer Documentation](https://developer.android.com)
- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines Documentation](https://kotlinlang.org/docs/coroutines-overview.html)

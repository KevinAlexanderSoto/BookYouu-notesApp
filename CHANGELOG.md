# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.3.1] - 2026-05-15

### Fixed
- **AddExpenseScreen Date Offset**: Fixed a bug where selecting a date in the expense screen resulted in a one-day offset due to timezone conversion issues between DatePicker (UTC) and system local time.
- **Payment Obligations Edit Loop**: Fixed a bug where swiping to edit a payment obligation would trigger the navigation event multiple times per gesture. Implemented a guard mechanism and `launchSingleTop` to ensure the edit screen opens only once and allows proper back navigation.

### Changed
- **Bundle Naming**: Updated the build configuration to include the version name in the generated artifacts (APK/AAB), resulting in filenames like `BookYouu-2.3.1-release.aab`.

## [2.3.0] - 2026-05-04

### Added
- **Edit Expenses**: Ability to edit existing expenses by clicking on them in the list.
- **Edit Payments**: Replaced the edit button with a modern gesture-based system. Swiping a payment obligation from **left to right** now triggers the edit flow, while swiping from **right to left** remains for deletion.
- **Flexible Date Selection**: Allowed selecting any date (including previous months) when adding or editing an expense.

## [2.2.0] - 2026-05-04

### Added
- **ObligationsWidget**: A home screen widget to visualize upcoming payments and commitments using Glance.
- Centralized **Category System** in the core module for consistent icon and color mapping across features.
- Spanish localization and string resources for the home screen widget and total commitment summaries.
- UI redesign for `ObligationsWidget` with improved layout, better spacing, and readability.

### Changed
- **Journal Module Refactor**: Migrated "Subjects" and "Records" into a unified `:journal` module.
    - Renamed entities: `Subject` -> `Journal` and `Note` -> `JournalEntry`.
    - Updated terminology across the UI (e.g., "Subjects" to "Projects", "Classroom" to "Location").
    - Implemented a Room database migration (version 2 to 3) for the renamed tables.
- Updated application design to fully utilize **Material3** color schemes in Expenses and common components.
- Updated start destination and bottom navigation order to prioritize the **Expenses** feature.
- Consolidated and moved `CategorySelector` to the core module for wider reuse.

### Fixed
- Fixed unique ID handling in AdMob integration.

## [2.1.0] - 2026-04-13

### Added
- New **Expenses** feature module for tracking daily spending.
- **LargeAmountInput** component for handling high-value numeric inputs with formatting.
- Date selection and UI improvements for the "Add Expense" screen.
- Notification scheduling and cancellation for the payments feature.
- Boot receiver to automatically reschedule notifications after device restart.

### Changed
- Updated home category icons in `ExpenseRow`.
- Moved `NumberVisualTransformation` to core utilities.
- Enhanced category selector UI with improved visuals.
- Updated `minSdk` to 30 for modern API support.

### Removed
- Average calculation feature.

### Fixed
- Improved notification scheduling logic for reliability.

## [2.0.1] - 2026-04-04

### Added
- Success status screen integration for Subjects.

### Changed
- UI layout and styling improvements across payment and core components.

## [2.0.0] - 2026-04-04

### Added
- Comprehensive **Payments** feature module with data, domain, and presentation layers.
- Support for creating, deleting, and multi-selecting payment obligations.
- Type-safe navigation system with reusable components.
- Spanish localization for the payments feature.
- `SuccessStatusScreen` reusable component for operation feedback.
- `NumberVisualTransformation` for numeric inputs in `LabeledInput`.

### Changed
- Complete redesign of the app's visual system and bottom bar.
- Refactored database schema to version 1 for the new modular structure.

## [1.0.0] - 2026-01-10

### Added
- Initial release of BookYouu Notes App.
- Basic note-taking and record-keeping functionality.
- Koin dependency injection setup.
- Core module for shared UI and utilities.

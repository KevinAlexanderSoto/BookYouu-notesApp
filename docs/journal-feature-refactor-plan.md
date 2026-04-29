# Implementation Plan: Journal Feature Refactor (Hobby/Project Journal)

## Objective
Rebrand the existing "Subject" and "Record" features into a unified "Journal" feature focused on tracking hobbies, DIY projects, or personal progress. This includes moving the feature from the `:app` module into a new dedicated `:journal` module and updating the terminology across the codebase.

## Key Changes

### 1. Module Architecture
- Create a new Android Library module: `:journal`.
- Configure `build.gradle.kts` for `:journal` with dependencies on `:core`, `:db`, and `:permission`.
- Update `settings.gradle` to include the new module.

### 2. Data Layer Refactoring (`:db` & `:journal`)
- **Rename Entities**:
    - `Subject` -> `Journal` (represents a project like "Garden 2024").
    - `Note` -> `JournalEntry` (represents a specific update/photo in that project).
- **Database Migration**: Create a Room migration to rename tables/columns or handle the transition if data persistence is required.
- **Repository**: Move and rename `SubjectRepository` to `JournalRepository` within the `:journal` module.

### 3. Presentation Layer (`:journal`)
- **Rebranding UI**:
    - "Subject List" -> "My Journals" or "Projects".
    - "Subject Name" -> "Journal Title" (e.g., "Build Bookshelf").
    - "Classroom" -> "Location/Context" (or remove if irrelevant).
    - "Credits" -> "Priority" or "Estimated Time" (repurpose the integer field).
- **Relocation**:
    - Move all Composables from `app/.../subject` and `app/.../records` to the `:journal` module.
    - Move ViewModels and Mappers to the `:journal` module.
- **DI**: Define a Koin module for `:journal` (`journalModule`) and register it in `AppDI.kt`.

### 4. Navigation & Entry Point
- **Route Updates**: Update `Route.kt` constants (e.g., `ROUTE_JOURNAL`, `ROUTE_JOURNAL_ENTRY`).
- **Bottom Bar**: Re-enable the feature in `BottomNavigationScreens.kt` with a "Journal" label and an appropriate icon (e.g., `history_edu` or `collections_bookmark`).
- **Navigation Graph**: Create `JournalNavigationGraph.kt` within the `:journal` module and link it to the `RootNavigationGraph`.

### 5. String Resources (ES/EN)
- Update `strings.xml` in both English and Spanish to reflect the journal terminology:
    - `subject_list_title` -> `journal_list_title` ("My Projects").
    - `add_subject` -> `add_journal` ("New Project").
    - `subject_delete_title` -> `journal_delete_title` ("Delete Project?").

## Implementation Steps

### Phase 1: Infrastructure
1. Create `:journal` module.
2. Setup Gradle dependencies and Koin module skeleton.

### Phase 2: Data Migration
1. Rename Room entities and DAOs in `:db`.
2. Implement `JournalRepository` in the new module.

### Phase 3: Feature Migration
1. Move UI components from `:app` to `:journal`.
2. Perform global rename/refactor of "Subject" to "Journal" in the code.
3. Update all string resources.

### Phase 4: Integration
1. Wire navigation in `:app`.
2. Re-add the icon to the Bottom Navigation Bar.

## Verification & Testing
- **Unit Tests**: Migrate and update existing tests for ViewModels and Repositories to the new module.
- **Manual UI Check**: Ensure the flow from creating a Journal to adding photo/audio entries remains functional.
- **Database Verification**: Confirm existing data (if any) is correctly migrated to the new "Journal" structure.

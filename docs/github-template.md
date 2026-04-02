# GitHub Template Setup

This project is ready to be used as a reusable template without changing current implementation.

## 1) Enable "Template repository" on GitHub

1. Open: `https://github.com/AshikAzeez/CMP-Template/settings`
2. Go to `General`
3. Enable `Template repository`

After this, GitHub will show `Use this template` on the repository page.

## 2) Create a New App from This Template

1. Open repository page: `https://github.com/AshikAzeez/CMP-Template`
2. Click `Use this template`
3. Choose:
   - Owner
   - New repository name
   - Public/private
4. Click `Create repository from template`

## 3) Post-Create Bootstrap Checklist

1. Rename app identity:
   - `composeApp/build.gradle.kts` (`applicationId`)
   - iOS bundle identifier in Xcode project (`iosApp`)
2. Update branding:
   - app name and launcher assets
   - `README.md`
3. Update environment config:
   - Android: `composeApp/src/androidMain/assets/config/config.json`
   - iOS: `iosApp/iosApp/config.json`
4. Configure API/auth endpoints for staging/prod.
5. Validate build:
   - `./gradlew :composeApp:assembleDebug`

## 4) What Is Preserved As-Is

- Modular clean architecture structure
- MVI flow and feature implementation
- Ktor networking + auth + mock engine
- DataStore + database abstractions + migration registry
- Analytics abstraction and DI wiring
- Design system, theme, and Compose utilities
- Napier-backed logger abstraction

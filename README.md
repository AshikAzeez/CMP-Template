# CMP-Arch Template (Android + iOS)

Production-grade Compose Multiplatform template using strict Clean Architecture + MVI.

## Modules

- `:composeApp` - platform entry points, app-level navigation, DI bootstrap.
- `:core` - cross-cutting primitives (`AppResult`, `AppError`, MVI base, DataStore helpers, platform abstractions).
- `:core:network` - Ktor client factory + network error mapping.
- `:core:database` - cache abstraction + Android Room implementation + iOS in-memory fallback.
- `:core:ui` - reusable Compose UI primitives and theme.
- `:domain` - domain models, repository contracts, use cases.
- `:data` - repository implementation, local/remote data sources, DTO/cache/domain mapping.
- `:feature:home` - presentation layer (MVI contract, ViewModel, screen, feature nav graph).

## Dependency Direction

Compile-time dependencies are enforced inward:

- `composeApp -> feature + data + domain + core*`
- `feature -> domain + core + core:ui`
- `data -> domain + core + core:network + core:database`
- `domain -> core`
- `core* -> (no feature/domain/data dependencies)`

## Build

```bash
./gradlew :composeApp:assembleDebug
```

## iOS

Open `iosApp` in Xcode and run the `iosApp` scheme.

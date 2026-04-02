# Architecture Notes

## MVI Contract

Each feature owns:

- `Intent` - all user/system actions.
- `UiState` - immutable render state.
- `Effect` - one-off actions (navigation/snackbar).

`HomeViewModel` extends a shared `MviViewModel` in `:core`, guaranteeing unidirectional state updates.

## Data Flow

1. UI dispatches `Intent` to ViewModel.
2. ViewModel executes domain use case(s).
3. Use cases call repository interfaces from `:domain`.
4. Repository implementation in `:data` coordinates remote (`Ktor`) + local cache (`Room` on Android).
5. Local cache emits Flow updates back to presentation.

## Enforced Boundaries

- No framework types in `:domain`.
- No DTO/entity exposure outside `:data`.
- No feature module reaches into data internals.
- Platform API access isolated via `expect/actual` and target source sets.

# Veltrix

Veltrix is a client-side Fabric mod for Minecraft 26.2.

## Design direction

Veltrix takes inspiration from the qualities that make Lunar Client cohesive: clear visual hierarchy, consistent spacing, a focused module workspace, category navigation, and a polished configuration experience. Veltrix has its own identity and implementation; it does not reuse Lunar Client branding or proprietary code.

## Current foundation

- Fabric 26.2 project structure.
- Right Shift opens the in-game menu.
- Dark, glass-inspired panel layout and category navigation.
- Starter module cards for the UI foundation.

The starter cards are not yet functional modules. Each feature will be marked as implemented only after its behavior and settings are connected.

## Build

Requires JDK 25 and a compatible Gradle installation.

```bash
gradle build
```

The remapped mod JAR is produced in `build/libs/`.

# Veltrix

Veltrix is a client-side Fabric mod for Minecraft 26.2.

## Design direction

Veltrix takes inspiration from the qualities that make Lunar Client cohesive: clear visual hierarchy, consistent spacing, a focused module workspace, category navigation, and a polished configuration experience. Veltrix has its own identity and implementation; it does not reuse Lunar Client branding or proprietary code.

## Current foundation

- Fabric 26.2 project structure.
- Right Shift opens the in-game menu.
- Dark, glass-inspired panel layout and category navigation.
- Starter module cards for the UI foundation.

The catalogue includes planned modules; only features with connected behavior should be treated as implemented.\n\n## Implemented feature: Custom Block Highlight\n\n- Replaces the vanilla targeted-block outline with a configurable outline.\n- Optional translucent face fill.\n- Static colour presets and animated rainbow outline.\n- Smooth target movement interpolation.\n- Settings persist in `config/veltrix-block-highlight.properties`.\n- In the Visuals category: click the card to enable/disable, click the small `F` control to toggle fill, right-click to toggle rainbow, and middle-click to cycle outline colours.

## Build

Requires JDK 25 and a compatible Gradle installation.

```bash
gradle build
```

The remapped mod JAR is produced in `build/libs/`.

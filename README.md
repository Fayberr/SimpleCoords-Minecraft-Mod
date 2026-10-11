# SimpleCoords

A client-side Fabric mod that shows a small coordinate HUD on screen.

## Features

- XYZ coordinates with adjustable precision (0 to 5 decimals).
- Subchunk position (0 to 15 in each axis).
- Current biome, with a settings toggle.
- Facing direction, with optional intercardinal directions.
- Uses the camera position instead of the player position, so it also works with
  freecam and when riding.
- Optional background behind the text.
- Drag the HUD to a new position with the editor in the config screen.

## Commands

All commands are client-side.

- `/simplecoords` opens the config screen.
- `/simplecoords config` opens the config screen.
- `/simplecoords toggle` shows or hides the HUD.
- `/simplecoords set use_camera <true/false>` use the camera position.
- `/simplecoords set show_xyz <true/false>` show the coordinates.
- `/simplecoords set show_subchunk <true/false>` show the subchunk.
- `/simplecoords set show_facing <true/false>` show the facing direction.
- `/simplecoords set show_biome <true/false>` show the biome.
- `/simplecoords set precision <0-5>` set the number of decimals.

Settings use Modern Config when installed, otherwise the built-in screen. Modern Config is optional. Its Save button persists changes; Cancel restores them. HUD X and Y sliders position the HUD in Modern Config. The built-in screen retains the drag editor.

A ModMenu config screen is available if ModMenu is installed.

## Building

JDK 25, Gradle 9.7.0+, and a Minecraft 26.3 Fabric development environment.
Publish Modern Config 1.9.4 for 26.3 to Maven local first (`1.9.4+26.3`).

```bash
gradle build
```

The jar is in `build/libs/`.

Client regression tests (a display or Xvfb is required):

```bash
gradle runClientGameTest
gradle runClientGameTest -PtestModernConfig
```

The second run also needs Modern GUI 1.0.14 for 26.3 in Maven local.
The production jar contains neither the tests nor the optional libraries.

## License

GPL-3.0-or-later

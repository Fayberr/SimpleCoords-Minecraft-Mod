# SimpleCoords

[Modrinth](https://modrinth.com/mod/simple-coords)

A client-side Fabric mod for Minecraft 1.21.11 that shows a small coordinate HUD on screen.

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

Settings use the built-in screen, including the HUD drag editor.

A ModMenu config screen is available if ModMenu is installed.

## Building

Requires JDK 21 for Minecraft and compilation. The Gradle wrapper uses 9.8.2;
run Gradle itself with JDK 21 or newer.

```bash
./gradlew build
```

The Java 21 jar is `build/libs/simplecoords-2.2.0+1.21.11.jar`.

Client regression tests require a display or Xvfb:

```bash
ALSOFT_DRIVERS=null LIBGL_ALWAYS_SOFTWARE=1 xvfb-run -a ./gradlew runClientGameTest
```

The tests check config migration and persistence, settings, HUD positioning,
biome names, targeted block state and normal crosshair reach. Tests and optional
ModMenu are excluded from the production jar.

## License

GPL-3.0-or-later

## Targeted block

The targeted block's XYZ coordinates appear last in the HUD, beneath the biome.
Target Subchunk, Target Block Type, and Target Block State are optional and default off.
Subchunk values are local X/Y/Z coordinates from 0 to 15. Block types use registry IDs
such as `minecraft:oak_stairs`; states show properties such as `facing=north`.
The target section hides when the crosshair does not hit a block within normal reach.
Use the settings screen or `/simplecoords set show_target true` to toggle the section.
The additional command options are `show_target_subchunk`, `show_target_type`, and `show_target_state`.

# SimpleCoords

A client-side Fabric mod that shows a small coordinate HUD on screen.

## Features

- XYZ coordinates with adjustable precision (0 to 5 decimals).
- Subchunk position (0 to 15 in each axis).
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
- `/simplecoords set precision <0-5>` set the number of decimals.

A ModMenu config screen is available if ModMenu is installed.

## Building

JDK 21 and a Fabric 1.21.10 development environment.

```bash
./gradlew build
```

The jar is in `build/libs/`.

## License

GPL-3.0-or-later

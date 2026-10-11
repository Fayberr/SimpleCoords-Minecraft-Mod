# SimpleCoords - Modrinth page copy

Source of truth for https://modrinth.com/mod/simple-coords.

(Synced from the live page after an external edit during publication. Preserve this wording.)

## description

A lightweight client-side HUD showing coordinates, subchunk position, facing direction and biome.

## body

# SimpleCoords

Shows a small coordinate HUD on screen. Client-side only.

## Features

- XYZ coordinates with adjustable precision (0 to 5 decimals), subchunk position and facing direction.
- Current biome, with a toggle to show or hide it.
- Uses the camera position instead of the player position, so it also works with freecam and when riding.
- Optional background behind the text.
- Settings through Modern Config when installed, with a built-in screen otherwise. Modern Config is optional.
- HUD X and Y sliders in Modern Config, or a drag editor in the built-in screen.
- Biome display

## Commands

All commands are client-side.

- `/simplecoords config` opens the config screen.
- `/simplecoords toggle` shows or hides the HUD.
- `/simplecoords set <key> <value>` changes a setting.
- `/simplecoords set show_biome <true/false>` shows or hides the biome.

A Mod Menu config screen is available if Mod Menu is installed. Requires Fabric API on Fabric.

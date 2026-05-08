# SimpleCoords Minecraft Mod

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1-blue.svg)
![Mod Loader](https://img.shields.io/badge/Loader-NeoForge-orange.svg)
![License](https://img.shields.io/badge/License-GPLv3-green.svg)
![Author](https://img.shields.io/badge/Author-Fayber-purple.svg)

A highly configurable, lightweight client-side HUD for Minecraft 1.21.1, built with the NeoForge modding API.

## 🚀 Features

- **XYZ Coordinates:** Display your precise location with adjustable decimal precision (0-5).
- **Subchunk Info:** See your relative position (0-15) within the current subchunk.
- **Directional HUD:** Real-time facing direction (NORTH, SOUTH, etc.).
- **Interactive Positioning:** Drag and drop the HUD anywhere on your screen using the in-game editor.
- **Freecam Support:** Toggleable option to display the camera's coordinates instead of the player's body—perfect for spectator work or freecam mods.

## 🛠 Specifications

- **Version:** `1.0.0`
- **Minecraft Version:** `1.21.1`
- **Mod Loader:** `NeoForge` (javafml)
- **Minimum NeoForge Version:** `21.1.65`
- **Side:** `Client-only`

## 🎮 Usage

### Controls
- **/simplecoords toggle** - Quickly show or hide the HUD via chat.

### Configuration
1. Go to the **Mods** menu from the Minecraft main screen.
2. Select **SimpleCoords** and click the **Config** button.
3. Use the **Interactive Editor** to drag the HUD to your preferred location.
4. Use **Configure HUD Options** to toggle specific displays and adjust precision.

## 📥 Installation

1. Ensure you have the latest **NeoForge** installed for 1.21.1.
2. Download the `.jar` from the releases page (or build it yourself).
3. Drop the file into your `%appdata%\.minecraft\mods` folder.

## 🏗 Building

The project uses Gradle 9.2.1 and OpenJDK 21.
```powershell
./gradlew build
```
The compiled jar will be in `build/libs/`.

---
*Created by Fayber*

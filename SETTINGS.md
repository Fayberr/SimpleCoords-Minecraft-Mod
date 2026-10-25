# Settings screens

SimpleCoords chooses Cloth Config, then its built-in screen. Both libraries are optional.
All options in this Minecraft version are exposed by the selected screen. Changes
in Cloth Config apply on Save; Cancel keeps the previous settings.

Open settings with `/simplecoords`, `/simplecoords config`, or the NeoForge Mods menu.
Modern Config is not available for this Minecraft and loader target.
Use `/simplecoords hud` to open the drag editor with any settings library installed.
The existing config file and built-in screen remain supported.

## Verification

Build with `./gradlew build` using Java 21. Client settings checks require a display:

```bash
./gradlew runSettingsTest -PsettingsTest
./gradlew runSettingsTest -PsettingsTest -PtestCloth
```

The test mod is loaded only with `-PsettingsTest` and is excluded from the release jar.
On Linux ARM64, this old NeoForge launcher selects x86 native libraries. Supply the
matching LWJGL 3.3.3 ARM64 native directory through `org.lwjgl.librarypath` for testing.

Verification on Linux ARM64: production builds pass. The client checks exercise
screen priority, complete setting coverage and save persistence. Some 26.x clients
still hit the existing native OpenAL crash at JVM exit after assertions pass;
this is recorded separately from Java assertion failures.

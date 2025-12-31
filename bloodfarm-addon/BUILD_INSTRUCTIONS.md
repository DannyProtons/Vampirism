# Build Instructions

## Prerequisites

- Java 21 JDK
- Git (optional)

## Building the Mod

### Option 1: Using Gradle Wrapper (Recommended)

If you have the Gradle wrapper files, run:

```bash
./gradlew build
```

### Option 2: Manual Gradle Build

If you don't have the wrapper, you'll need Gradle 8.5+ installed:

```bash
gradle build
```

### First-Time Setup

The first build will take longer as Gradle downloads dependencies:
- NeoForge MDK
- Vampirism mod
- Minecraft sources
- Parchment mappings

## Output

The compiled mod will be located at:
```
build/libs/bloodfarm-1.0.0.jar
```

## Testing

To test the mod in a development environment:

```bash
./gradlew runClient
```

This will launch Minecraft with both Vampirism and Blood Farm loaded.

## Before Building

**IMPORTANT**: You need to add the Gradle wrapper files first! Run this command in the project directory:

```bash
gradle wrapper --gradle-version 8.10.2
```

This will create:
- `gradlew` (Linux/Mac wrapper script)
- `gradlew.bat` (Windows wrapper script)
- `gradle/wrapper/` directory

## Troubleshooting

### "Could not find vampirism"

If you get dependency errors for Vampirism, make sure you're online and the Teamlapen repository is accessible. The build.gradle is configured to pull from:
```
https://dl.cloudsmith.io/public/teamlapen/mods/maven/
```

### Java Version

Make sure you're using Java 21:
```bash
java -version
```

Should show: `openjdk version "21.x.x"` or similar

### Clean Build

If you encounter issues, try a clean build:
```bash
./gradlew clean build
```

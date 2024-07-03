# Flying Marioo

Flying Marioo is an exciting game inspired by the classic Flappy Bird, but with Marioo as the main character. In this game, Marioo flies through a series of poles, and the objective is to avoid hitting the poles. If Marioo hits a pole, the game is over. The score increases with each successful pass through the poles, and all scores are stored and listed using Room Database, including the time the game ended.

## Features

- **Flying Marioo**: Navigate Marioo through poles to avoid collisions.
- **Score Tracking**: Increase the score with each successful pass.
- **Game Over Detection**: Game ends when Marioo hits a pole.
- **Score History**: Store and display all scores with the timestamp of game over using Room Database.
- **Jetpack Compose**: UI is built using Jetpack Compose for a modern and declarative approach.
- **Clean Architecture**: Follows Clean Architecture with MVVM pattern for a clean, maintainable, and testable codebase.


## Architecture

- **Kotlin**: The programming language used.
- **Jetpack Compose**: Used for building the UI.
- **Room Database**: Used for storing and listing scores.
- **Clean Architecture**: The project follows Clean Architecture principles.
- **MVVM Pattern**: Implements the Model-View-ViewModel pattern for separation of concerns.

## Setup and Installation

1. **Clone the repository**:

2. **Open in Android Studio**: Open the project in Android Studio.

3. **Build the project**: Let Android Studio build and sync the project.

4. **Run the app**: Run the app on an emulator or a physical device.

## Code Overview

### Packages

- **data**: Contains Room database setup and data classes.
- **domain**: Contains use cases and repository interfaces.
- **presentation**: Contains the UI components built with Jetpack Compose and ViewModels.

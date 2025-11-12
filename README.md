# Arkanoid Game - Object-Oriented Programming Project

## Author
Group trà cũ vị xưa - Class INT2204 11
1. Nguyễn Hữu Trường - 21021445
2. Trịnh Tuấn Anh - 24020032
3. Nguyễn Tiến Huy - 24022663

**Instructor**: Kiều Văn Tuyên 
**Semester**: HK1 - Năm học 2025/2026

---

## Description
This is a classic Arkanoid game developed in Java as a final project for Object-Oriented Programming course. The project demonstrates the implementation of OOP principles and design patterns.

**Key features:**
1. The game is developed using Java 17+ with JavaFX for GUI.
2. Implements core OOP principles: Encapsulation, Inheritance, Polymorphism, and Abstraction.
3. Applies multiple design patterns: Singleton, Factory Method, Strategy, Observer, and State.
4. Features multithreading for smooth gameplay and responsive UI.
5. Includes sound effects, animations, and power-up systems.
6. Supports save/load game functionality and leaderboard system.
7. Event mode for entertainment.

**Game mechanics:**
- Control a paddle to bounce a ball and destroy bricks
- Collect power-ups for special abilities
- Progress through multiple levels with increasing difficulty
- Score points and compete on the leaderboard

---

## UML Diagram

### Class Diagram
![Class Diagram](docs/uml/class-diagram.png)

_Có thể sử dụng IntelliJ để generate ra Class Diagrams: https://www.youtube.com/watch?v=yCkTqNxZkbY_

*Complete UML diagrams are available in the `docs/uml/` folder*

---

## Design Patterns Implementation

_Có dùng hay không và dùng ở đâu_

### 1. Singleton Pattern
**Used in:**  `AudioManager`, 

**Purpose:** Ensure only one instance exists throughout the application.

---

## Multithreading Implementation
_Có dùng hay không và dùng như thế nào_

The game uses multiple threads to ensure smooth performance:

1. **Game Loop Thread**: Updates game logic at 60 FPS
2. **Rendering Thread**: Handles graphics rendering (EDT for JavaFX Application Thread)
3. **Audio Thread Pool**: Plays sound effects asynchronously
4. **I/O Thread**: Handles save/load operations without blocking UI

---

## Installation

1. Clone the project from the repository.
2. Open the project in the IDE.
3. Run the project.

## Usage

### Controls
| Key | Action |
|-----|--------|
| `←` or `A` | Move paddle left |
| `→` or `D` | Move paddle right |
| `SPACE` | Launch ball |
| `Q` or `E` | Control ball navigation |
| `P`  | Pause game |

### How to Play
1. **Select mode**: Click "Start Game" from the main menu.
2. **Start game**: Click one of four mode your want to play in select mode.
2. **Control the paddle**: Use arrow keys or A/D to move left and right.
3. **Launch the ball**: Press SPACE to launch the ball from the paddle.
4. **Destroy bricks**: Bounce the ball to hit and destroy bricks.
5. **Collect power-ups**: Catch falling power-ups for special abilities.
6. **Avoid losing the ball**: Keep the ball from falling below the paddle.
7. **Complete the level**: Destroy all destructible bricks to advance.

### Power-ups
| Icon | Name | Effect |
|------|------|--------|
| 🟦 | Expand Paddle | Increases paddle width for 10 seconds |
| 🐌 | Slow Ball | Decreases ball speed by 30% |
| 🎯 | Multi Ball | Spawns 2 additional balls |
| 💖 | Extra Life | Gain an additional life |


### Scoring System
- Normal Brick: 50 points
- Strong Brick: 150 points
- Strongest Brick: 300 points
---

## Demo

### Screenshots

**Main Menu**  
![Main Menu](src/screenshots/menu.png)

**Gameplay**  
![Gameplay](src/screenshots/gameplay.png)

**Power-ups in Action**  
![Power-ups](src/screenshots/powerups.png)

**Leaderboard**  
![Leaderboard](src/creenshots/leaderboard.png)

### Video Demo
[![Video Demo](src/screenshots/video-thumbnail.png)](src/demo/gameplay.mp4)

*Full gameplay video is available in `src/demo/gameplay.mp4`*

---

## Future Improvements

### Planned Features
1. **Additional game modes**
   - Time attack mode
   - Survival asian mode with endless levels
   - Co-op multiplayer mode
   - More language

2. **Enhanced gameplay**
   - Boss battles at end of worlds
   - More power-up varieties (freeze time, shield wall, etc.)
   - Achievements system

3. **Technical improvements**
   - Add particle effects and advanced animations
   - Implement AI opponent mode
   - Add online leaderboard with database backend

---

## Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Core language |
| JavaFX | 19.0.2 | GUI framework |
| Maven | 4.0+ | Build tool |
| Jackson | 2.15.0 | JSON processing |

---

## License

This project is developed for educational purposes only.

**Academic Integrity:** This code is provided as a reference. Please follow your institution's academic integrity policies.

---

## Notes

- The game was developed as part of the Object-Oriented Programming with Java course curriculum.
- All code is written by group members with guidance from the instructor.
- Some assets (images, sounds) may be used for educational purposes under fair use.
- The project demonstrates practical application of OOP concepts and design patterns.

---

*Last updated: [12/11/2025]*

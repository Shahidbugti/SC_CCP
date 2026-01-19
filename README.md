# Hotel Reservation System

This project is a Java-based Hotel Reservation System designed to manage rooms, guests, and reservations within a hotel chain. It emphasizes clean code, defensive programming, and unit testing.

## 🚀 How to Run the System

To quickly compile and run the system, follow these steps:

### 1. Compile Everything
Ensure you have the JUnit 5 standalone JAR in the `lib` directory. Then, compile all source files:
```powershell
javac -cp "lib/junit-platform-console-standalone-1.10.0.jar" -d bin src/main/java/com/hotel/**/*.java src/main/java/com/hotel/core/*.java src/main/java/com/hotel/domain/*.java src/main/java/com/hotel/exception/*.java
```
*(Note: If the `**/*.java` wildcard doesn't work in your shell, you can list the directories or use a build tool like Maven/Gradle).*

### 2. Run the Demonstration
```powershell
java -cp bin com.hotel.Main
```

### 3. Run the Test Suite (JUnit 5)
```powershell
java -jar "lib/junit-platform-console-standalone-1.10.0.jar" --class-path bin --scan-class-path
```

## 🌟 Key Features

1.  **Comprehensive Room Management**: Manage room inventory with different types (Single, Double, Family, Suite) and track real-time states.
2.  **Robust Reservation System**: Support for creating, checking availability, and canceling bookings with complex date logic.
3.  **Guest Life Cycle**: Full support for guest registration, identity verification, and check-in/check-out processes.
4.  **Defensive Programming**: Extensive input validation and state-transition logic to prevent system inconsistencies.
5.  **Unit Testing**: A complete suite of 160+ unit tests covering all domain and core logic using JUnit 5.

## 📁 Project Structure

- `src/main/java/com/hotel/domain`: Core business entities (Room, Guest, Reservation, etc.)
- `src/main/java/com/hotel/core`: Orchestration logic (Hotel, HotelChain)
- `src/main/java/com/hotel/exception`: Custom business exceptions
- `src/test/java`: Comprehensive JUnit test suite
- `lib`: External libraries (JUnit 5 console standalone JAR)
- `bin`: Compiled class files

# Hotel Reservation System - CCP Project

## Project Overview
This is a **Java-based Hotel Reservation System** implementing a UML class design.  
It manages hotels, rooms, guests, and reservations, demonstrating:
- Making and canceling reservations  
- Checking room availability  
- Guest check-in and check-out  
- Defensive programming and clean code principles  

---

## How to Build & Run

1. **Compile all classes**:

```powershell
javac -cp "bin;lib/junit-platform-console-standalone-1.10.0.jar" -d bin src/main/java/**/*.java src/test/java/**/*.java
Run the main program:

java -cp bin com.hotel.Main


Run all tests:

java -jar lib/junit-platform-console-standalone-1.10.0.jar -cp bin --scan-classpath


All tests should pass successfully.

Features

Implements UML design exactly

Defensive programming with input validation

Clean, modular, maintainable code

JUnit tests cover normal, boundary, and invalid cases

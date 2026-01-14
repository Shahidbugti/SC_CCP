# Hotel Reservation System - Complex Computing Problem (CCP)

## Project Overview
This project is a Java-based Hotel Reservation System developed for the Software Construction course. It translates a UML class diagram into a functional system using Object-Oriented Design (OOD), Clean Code principles, and Defensive Programming.

The system handles hotel management, room inventory, guest registration, and the full lifecycle of a reservation (Booking, Check-in, Check-out, and Cancellation).

## Key Features
- **Strict UML Compliance:** All relationships (Composition, Aggregation, Inheritance) are implemented as per the design.
- **Defensive Programming:** Robust validation for inputs and illegal state transitions (e.g., booking an occupied room).
- **Automated Testing:** Comprehensive JUnit test suite using the AAA (Arrange-Act-Assert) pattern.
- **Clean Code:** Adherence to meaningful naming, modularity, and DRY principles.

## Prerequisites
- **Java JDK 17** or higher
- **Gradle** (if using the wrapper provided)
- **VS Code** (recommended IDE)

## Instructions to Build and Run

### 1. Build the Project
Open your terminal in the project root and run:
```powershell
./gradlew build

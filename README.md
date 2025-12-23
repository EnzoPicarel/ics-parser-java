<div align="center">
  <h3 align="center">ICS Parser Java</h3>

  <p align="center">
    A command-line calendar application parsing <strong>iCalendar (ICS)</strong> files with filtering and multi-format output support.
    <br />
    <a href="#-getting-started"><strong>Get Started »</strong></a>
  </p>
  
  ![CI Status](https://img.shields.io/badge/build-passing-brightgreen)
  ![License](https://img.shields.io/badge/license-MIT-blue)
</div>

## 🔍 About The Project
This project implements a full-featured calendar client that parses RFC 5545–compliant iCalendar (ICS) files from local or remote sources. It extracts `VEVENT` and `VTODO` components, applies intelligent temporal and status-based filters, and exports results in multiple formats.

The project was built under strict constraints: **no external libraries** for either ICS parsing (`ical4j`) or CLI argument management (`picocli`). The primary engineering goal was to practice fundamental OOP pillars—**Encapsulation, Delegation, Inheritance, and Polymorphism**—to create a maintainable, loosely coupled architecture.

*Built as a Semester 7 project at ENSEIRB-MATMECA.*

### 🛠 Built With
* **Language:** Java 11+
* **Build System:** Gradle
* **Testing:** JUnit 5, JaCoCo

## 📐 Architecture

### Technical Highlights
* **Custom RFC 5545 Parser:** Implements a state machine for line unfolding (RFC §3.1) with character-level buffering to preserve lookahead semantics without external parsing libraries.
* **Manual CLI Argument Parsing:** A robust argument parser built from scratch (replacing `picocli`) that handles cumulative flags, detects invalid combinations, and enforces default modes.
* **Strategy Pattern (Output):** Polymorphic `Output` hierarchy allows runtime selection of export formats (`Text`, `HTML`, `ICS`) via a static factory, decoupling logic from presentation. 
* **Template Method (Parsing):** `AbstractParser` defines the skeletal parsing algorithm, while subclasses (`ParserFile`, `ParserURL`) implement specific I/O delegation logic.
* **Filter Chain:** Composition-based filtering logic. `EventFilter` and `TodoFilter` hierarchies allow cumulative constraints (e.g., "Incomplete tasks due Tomorrow") to be validated and applied sequentially.

### File Organization
```text
├── build.gradle               # Project dependencies and build tasks
├── DESIGN.md                  # Architecture rationale and pattern documentation
├── SUBJECT.md                 # Original academic subject
├── diagramme.png              # UML Class Diagram visualization
├── src/main/java/eirb/pg203/
│   ├── CalendarApplication.java   # Orchestrator: args → load → filter → output
│   ├── ArgumentParser.java        # CLI parsing & Filter Chain validation
│   └── model/
│       ├── AbstractParser.java    # Template Method for parsing logic
│       ├── IcsReader.java         # Low-level RFC 5545 Line Unfolding
│       ├── Output.java            # Strategy Pattern base (Txt, Html, Ics)
│       ├── Calendar.java          # Root container (Stream-based filtering)
│       ├── EventFilter.java       # Temporal filters (Today, Week, Range)
│       └── TodoFilter.java        # Status filters (Incomplete, Done, In-Process)
└── src/test/resources/        # RFC 5545 test samples (folded lines, mixed content)
```

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK)** (11 or later)
* **Gradle** (8.10+ or use bundled wrapper)

### Installation & Build
1. **Clone the repository**
   ```bash
   git clone https://github.com/EnzoPicarel/ics-parser-java.git
   cd ics-parser-java
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Verify the build**
   ```bash
   ./gradlew test
   ```

## ⚡ Execution

The application is executed via the Gradle wrapper.

**Syntax:** `./gradlew run --args "<file> <mode> [filters] [options]"`

### 1. Events (Text Output)
Parse a local file and show events for a specific date range (stdout).
```bash
./gradlew run --args "src/test/resources/events_minimal.ics events -text -from 20251106 -to 20251106"
```

### 2. Todos (HTML Export)
Extract all incomplete todos and save them as a styled HTML report.
```bash
./gradlew run --args "src/test/resources/todos.ics todos -all -html -o out_todos.html"
```

### 3. Full Export (ICS Format)
Read a mixed remote/local file and re-serialize it to a valid ICS file.
```bash
./gradlew run --args "src/test/resources/mixed.ics events -ics -o out_calendar.ics"
```

**Available Flags:**
* **Events Status:** `-today` (Default), `-tomorrow`, `-week`, `-from YYYYMMDD -to YYYYMMDD`.
* **Todos Status:** `-incomplete` (Default), `-all`, `-completed`, `-inprocess`, `-needsaction`.
* **Formats:** `-text` (Default), `-html`, `-ics`.
* **Output:** `-o <file>` (Defaults to stdout).

## 🧪 Tests

This project enforces strict code quality standards using **JUnit 5** and **JaCoCo**.

**Run Full Suite:**
```bash
./gradlew test
```

**Generate Coverage Report:**
```bash
./gradlew jacocoTestReport
# Open build/reports/jacoco/test/html/index.html to view coverage
```

## 👥 Authors
* **Enzo Picarel**
* **Thibault Abeille**
* **Raphaël Bely**
* **Numa Guiot**

---
*Original Project Specs: [SUBJECT.md](./SUBJECT.md)*

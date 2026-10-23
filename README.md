# En-Mode-J (EMJ) Compiler

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![ANTLR4](https://img.shields.io/badge/ANTLR-4.13.1-red.svg)](https://www.antlr.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Target](https://img.shields.io/badge/Target-MicroPython%20%7C%20BBC%20micro%3Abit-blue)](https://microbit.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

An end-to-end compiler for **En-Mode-J (EMJ)**, an emoji-based Domain-Specific Language (DSL) designed to program autonomous **Cutebot** robots on 2D grid maps. The compiler performs lexing, parsing, semantic verification with hierarchical symbol tables, and transpiles valid EMJ source code into optimized **MicroPython** ready to execute on the **BBC micro:bit** microcontroller or MakeCode simulator.

---

## Architecture Overview

The compilation pipeline is structured into three decoupled phases following standard compiler design principles:

```
                  +---------------------------+
                  |  EMJ Source Code (.moj)   |
                  |     & Map File (.map)     |
                  +---------------------------+
                                |
                                v
               +---------------------------------+
               |   Lexer & Parser (ANTLR 4)      |
               |     EMJLexer.g4 / EMJParser.g4  |
               +---------------------------------+
                                |
                                v
               +---------------------------------+
               | Abstract Syntax Tree (AST)      |
               +---------------------------------+
                                |
                                v
               +---------------------------------+
               | Semantic Analysis & Validation  |
               | - Hierarchical Symbol Table     |
               | - Lexical Scope Stack           |
               | - Type & Variable Init Checks   |
               +---------------------------------+
                                |
                                v
               +---------------------------------+
               | Python Code Generator           |
               | - AST Transformation            |
               | - Indentation Management        |
               | - Cutebot Hardware Binding      |
               +---------------------------------+
                                |
                                v
               +---------------------------------+
               | Target MicroPython Script (.py) |
               +---------------------------------+
```

### Key Technical Highlights

1. **ANTLR4 Lexical & Syntactic Analysis (`EMJLexer.g4`, `EMJParser.g4`)**:
   - Supports Unicode emoji tokenization using Unicode character classes (`\p{Emoji}`, `\uFE0F`, `\u200D`).
   - Grammars for statement definitions, arithmetic and boolean expressions with operator precedence, control flow (`if`/`else`, `while`, `for`), functions, and map configurations.

2. **Hierarchical Symbol Table & Scope Management (`SymbolTable.java`, `Symbol.java`)**:
   - Implemented as an n-ary scope tree combined with a runtime navigation stack (`LinkedList<Scope>`).
   - Handles lexical scoping: supports local scope lookups (preventing duplicate declarations), parent scope delegation (allowing nested block closures to resolve outer variables), and a global function namespace.
   - Enforces variable lifecycle tracking (declaration vs. initialization validation), static type checking, and function arity/signature matching.

3. **MicroPython Code Generation (`EMJVisitorForPythonGeneration.java`, `EMJPythonCodeGenerator.java`)**:
   - Dedicated AST visitor separating semantic validation from code emission.
   - Handles strict Python indentation levels across nested control blocks and functions.
   - Emits hardware calls interfacing with the Cutebot driver library (`cutebot_custom_librairy.py`): motor speeds, turning maneuvers, dual RGB headlights, and obstacle detection.

---

## Tech Stack

* **Language**: Java 8+
* **Grammar & Parsing**: ANTLR 4.13.1
* **Build Automation**: Apache Maven
* **Target Runtime**: MicroPython / BBC micro:bit / Elecfreaks Cutebot
* **Testing Framework**: JUnit 4 (comprehensive test suites for actions, loops, conditions, functions, and maps)

---

## Project Structure

```
├── pom.xml                               # Maven project configuration
├── src
│   ├── main
│   │   ├── antlr4                       # ANTLR4 lexer and parser grammars (.g4)
│   │   ├── java/be/unamur/info/b314/compiler
│   │   │   ├── emj                      # Visitor, Symbol Table, Code Generator
│   │   │   ├── exception                # Custom compiler exceptions
│   │   │   └── main                     # CLI entrypoint (Main.java)
│   │   └── resources                    # Cutebot MicroPython library & sample .moj files
│   └── test                             # JUnit test suites & sample test resources
└── docs                                 # Architectural documentation (Phase 1-3 reports)
```

---

## Getting Started

### Prerequisites

* **Java Development Kit (JDK) 8** or higher
* **Apache Maven 3.8+**

### 1. Build the Compiler

Clone the repository and compile the project using Maven:

```bash
mvn clean package
```

This compiles the ANTLR4 grammars, builds the Java classes, runs unit tests, and packages a self-contained executable JAR at:
`target/en-mode-j-1.0.0-jar-with-dependencies.jar`.

### 2. Compile an EMJ Program

Run the compiler by supplying an input `.moj` file and the desired `.py` output path:

```bash
java -jar target/en-mode-j-1.0.0-jar-with-dependencies.jar -i src/main/resources/source_files/demonstration.moj -o output.py
```

* **Exit Code / Status**:
  * Returns `OK` on stderr upon successful compilation and generates the target `.py` file.
  * Returns `KO` on stderr with contextual syntax/semantic errors if validation fails.

### 3. Run Unit Tests

Execute the automated test suite covering syntax, semantics, and map validation:

```bash
mvn test
```

### 4. Deploy to Cutebot / BBC micro:bit

1. Open the [MakeCode Micro:bit Python Editor](https://python.microbit.org/) (or [makecode.microbit.org](https://makecode.microbit.org/)).
2. Copy the contents of the generated `.py` file.
3. Flash the program directly to your BBC micro:bit connected via USB, or run it inside the web simulator.

---

## Documentation

Comprehensive design documents and technical reports are available in the [`docs/`](docs/) directory:
* [Phase 1: Lexer & Parser Architecture](docs/phase_1.md)
* [Phase 2: Semantic Analysis & Symbol Table](docs/phase_2.md)
* [Phase 3: Code Generation & Python Transpiler](docs/phase_3.md)
* [Complete Compiler Guide (French)](docs/compiler_guide_fr.md)

---

## Authors & Acknowledgments

This project was developed by:
* **Timothy Lefebvre** ([@studentMeta](https://github.com/studentMeta))
* **Charles Eykerman** ([@Ackimm](https://github.com/Ackimm))
* **Jérémy Dehaye** ([@Imregil](https://github.com/Imregil))
* **Sacha Pauwels**

Developed as part of the Bachelor's in Computer Science program at the **University of Namur (UNamur)**. Special thanks to the university faculty teaching team for providing the initial course project framework.

---

## License

This project is licensed under the [MIT License](LICENSE).

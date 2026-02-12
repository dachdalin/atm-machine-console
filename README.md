# ATM Machine Console

A simple Java console-based ATM application that simulates basic banking operations with input validation and transaction tracking.

## Features

- Create account with account holder name, 4-digit PIN, and opening balance
- Secure login with up to 3 PIN attempts
- Check current balance
- Deposit money
- Withdraw money with insufficient-funds protection
- Change PIN
- View mini statement with timestamped transactions

## Tech Stack

- Java (console application)
- Standard Java libraries (`java.util`, `java.time`)

## Project Structure

```text
Atm_machine_console/
├── src/
│   └── Main.java
└── README.md
```

## Prerequisites

- JDK 17 or later installed
- `java` and `javac` available in terminal `PATH`

Check:

```bash
java -version
javac -version
```

## Build and Run

From project root:

```bash
javac src/Main.java
java -cp src Main
```

## Usage Flow

1. Start app
2. Create account (name, PIN, opening balance)
3. Login using PIN
4. Choose actions from menu:
- `1` Check Balance
- `2` Deposit
- `3` Withdraw
- `4` Mini Statement
- `5` Change PIN
- `6` Exit

## Example Menu

```text
=== Main Menu ===
1. Check Balance
2. Deposit
3. Withdraw
4. Mini Statement
5. Change PIN
6. Exit
```

## Notes

- PIN must be exactly 4 digits.
- Amounts must be valid non-negative numbers.
- Failed withdrawal attempts are logged in transaction history.

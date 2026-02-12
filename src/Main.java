import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        printHeader("ATM Console");

        Account account = createAccount();
        runSession(account);

        System.out.println("\nThank you for using ATM Console.");
    }

    private static Account createAccount() {
        System.out.println("Create your account to begin.");
        String holderName = readNonBlank("Account holder name: ");
        String pin = readPin("Set a 4-digit PIN: ");
        double openingBalance = readAmount("Enter opening balance: ");

        Account account = new Account(holderName, pin, openingBalance);
        account.addTransaction("Account created with opening balance $" + formatMoney(openingBalance));

        return account;
    }

    private static void runSession(Account account) {
        if (!authenticate(account)) {
            System.out.println("Too many failed attempts. Session terminated.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int option = readIntInRange("Choose an option: ", 1, 6);

            switch (option) {
                case 1 -> showBalance(account);
                case 2 -> deposit(account);
                case 3 -> withdraw(account);
                case 4 -> printMiniStatement(account);
                case 5 -> changePin(account);
                case 6 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static boolean authenticate(Account account) {
        final int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String inputPin = readPin("Enter your PIN: ");
            if (account.checkPin(inputPin)) {
                System.out.println("\nWelcome, " + account.getHolderName() + ".");
                account.addTransaction("User logged in");
                return true;
            }

            int remaining = maxAttempts - attempt;
            if (remaining > 0) {
                System.out.println("Incorrect PIN. Attempts remaining: " + remaining);
            }
        }
        return false;
    }

    private static void showBalance(Account account) {
        System.out.println("Current balance: $" + formatMoney(account.getBalance()));
    }

    private static void deposit(Account account) {
        double amount = readAmount("Enter deposit amount: ");
        account.deposit(amount);
        System.out.println("Deposit successful. New balance: $" + formatMoney(account.getBalance()));
    }

    private static void withdraw(Account account) {
        double amount = readAmount("Enter withdrawal amount: ");
        if (account.withdraw(amount)) {
            System.out.println("Please collect your cash.");
            System.out.println("New balance: $" + formatMoney(account.getBalance()));
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private static void changePin(Account account) {
        String currentPin = readPin("Enter current PIN: ");
        if (!account.checkPin(currentPin)) {
            System.out.println("Current PIN does not match.");
            return;
        }

        String newPin = readPin("Enter new 4-digit PIN: ");
        account.setPin(newPin);
        account.addTransaction("PIN changed");
        System.out.println("PIN changed successfully.");
    }

    private static void printMiniStatement(Account account) {
        printHeader("Mini Statement");
        List<String> history = account.getTransactions();
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String entry : history) {
                System.out.println(entry);
            }
        }
        System.out.println("Available balance: $" + formatMoney(account.getBalance()));
    }

    private static void printMenu() {
        printHeader("Main Menu");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Mini Statement");
        System.out.println("5. Change PIN");
        System.out.println("6. Exit");
    }

    private static String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private static String readPin(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (input.matches("\\d{4}")) {
                return input;
            }
            System.out.println("PIN must be exactly 4 digits.");
        }
    }

    private static double readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= 0.0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid input.");
            }
            System.out.println("Enter a valid non-negative amount.");
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid input.");
            }
            System.out.println("Enter a number between " + min + " and " + max + ".");
        }
    }

    private static String formatMoney(double amount) {
        return String.format("%.2f", amount);
    }

    private static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    private static class Account {
        private final String holderName;
        private String pin;
        private double balance;
        private final List<String> transactions = new ArrayList<>();

        private Account(String holderName, String pin, double balance) {
            this.holderName = holderName;
            this.pin = pin;
            this.balance = balance;
        }

        private String getHolderName() {
            return holderName;
        }

        private double getBalance() {
            return balance;
        }

        private boolean checkPin(String inputPin) {
            return pin.equals(inputPin);
        }

        private void setPin(String newPin) {
            pin = newPin;
        }

        private void deposit(double amount) {
            balance += amount;
            addTransaction("Deposited $" + formatMoney(amount));
        }

        private boolean withdraw(double amount) {
            if (amount > balance) {
                addTransaction("Failed withdrawal attempt of $" + formatMoney(amount));
                return false;
            }
            balance -= amount;
            addTransaction("Withdrew $" + formatMoney(amount));
            return true;
        }

        private void addTransaction(String description) {
            String entry = "[" + LocalDateTime.now().format(TIME_FORMAT) + "] " + description;
            transactions.add(entry);
        }

        private List<String> getTransactions() {
            return transactions;
        }
    }
}

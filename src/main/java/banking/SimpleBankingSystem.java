package banking;

import java.util.Scanner;

public class SimpleBankingSystem {
    Account account;
    Scanner scanner = new Scanner(System.in);
    Database database;
    State state;

    public void openDatabase(String databaseFileName) {
        database = new Database();
        database.setFileName(databaseFileName);
        database.createDatabase();
    }

    public void run(String databaseFileName) {
        openDatabase(databaseFileName);

        printMainMenu();

        int userInput = getUserInput();
        while (userInput != 0) {
            handleUserInput(userInput);

            if (state == State.END) break;

            printMainMenu();
            userInput = getUserInput();
        }
        exit();

    }

    private void handleUserInput(int userInput) {
        switch (userInput) {
            case 1:
                createAccount();
                break;
            case 2:
                login();
                break;
            default:
                printMainMenu();
        }
    }

    private int getUserInput() {
        return scanner.nextInt();
    }

    private void exit() {
        System.out.println("Bye!");
    }

    private void logout() {
        account = null;
        System.out.println("You have successfully logged out!");
    }

    private void printMainMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    private void printLoggedInMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    private void accountBalance() {
        database.getBalance(account);
        System.out.println("Balance: " + account.getBalance());
    }

    private void addIncome() {
        System.out.println("Enter income:");
        long income = scanner.nextLong();
        database.addIncome(account, income);
        System.out.println("Income was added!");
    }

    private void createAccount() {
        account = AccountGenerator.generateAccount();
        database.createAccount(account);
        System.out.println("\nYour card has been created");
        System.out.println(account);
    }

    private void performLogin(String cardNumber, String pinNumber) {
        account = database.userLogin(cardNumber, pinNumber);
        if (account != null) {
            state = State.LOGIN;
            System.out.println("\nYou have successfully logged in!");
            printLoggedInMenu();
            int userInput = getUserInput();
            while (userInput != 0) {
                switch (userInput) {
                    case 1:
                        state = State.BALANCE;
                        accountBalance();
                        printLoggedInMenu();
                        userInput = getUserInput();
                        break;
                    case 2:
                        addIncome();
                        printLoggedInMenu();
                        userInput = getUserInput();
                        break;
                    case 3:
                        doTransfer();
                        printLoggedInMenu();
                        userInput = getUserInput();
                        break;
                    case 4:
                        closeAccount();
                        state = State.LOGOUT;
                        userInput = 0;
                        break;
                    case 5:
                        state = State.LOGOUT;
                        logout();
                        userInput = 0;
                        break;
                }
            }
            if (state != State.LOGOUT) state = State.END;
            //if (userInput == 0) state = State.END;
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    private void closeAccount() {
        database.closeAccount(account);
        account = null;
        System.out.println("The account has been closed!");
    }

    private void doTransfer() {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String destinationCard = scanner.next();
        // check if same card
        if (destinationCard.equals(account.getAccountNumber())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        // check if card number doesn't pass the Luhn algorithm
        if (!AccountGenerator.validateCard(destinationCard)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }
        if (database.checkCardExistent(destinationCard)) {
            // check if your money are enough for transfer
            System.out.println("Enter how much money you want to transfer:");
            long moneyToTransfer = scanner.nextLong();
            if (account.getBalance() < moneyToTransfer) {
                System.out.println("Not enough money!");
            } else {
                database.transferMoney(destinationCard, moneyToTransfer);
                database.addIncome(account, -moneyToTransfer);
                System.out.println("Success!");
            }
        } else {
            System.out.println("Such a card does not exist.");
        }

    }

    private void login() {
        System.out.println("Enter your card number:");
        String creditCardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();
        performLogin(creditCardNumber, pin);
    }
}